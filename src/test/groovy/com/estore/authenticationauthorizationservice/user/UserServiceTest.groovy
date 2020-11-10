package com.estore.authenticationauthorizationservice.user


import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser
import com.estore.authenticationauthorizationservice.client.ClientEntity
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

class UserServiceTest extends Specification {

    private AuthenticationUser authenticationUser = new AuthenticationUser('User', 'Password', Collections.emptyList())

    UserRepository mockUserRepository = Mock(UserRepository.class)
    UserMapper mockUserMapper = Mock(UserMapper.class)
    PasswordEncoder mockPasswordEncoder = Mock(PasswordEncoder.class)
    @Subject
    UserService userService = new UserService(mockUserRepository, mockUserMapper, mockPasswordEncoder)

    def setup() {
        authenticationUser.setClient(UUID.fromString('c1932a1c-0666-4832-90f4-06e2f3acbf1e'))
    }

    def "should save a new user"() {
        given:
        com.estore.authenticationauthorizationservice.user.dto.UserCreationDto user = com.estore.authenticationauthorizationservice.user.dto.UserCreationDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .username('TEST')
                .client(ClientDto.builder().build())
                .build()

        when:
        def savedUser = userService.save(user, authenticationUser)

        then:
        1 * mockUserRepository.findOneByUsernameIgnoreCase('TEST') >> Optional.empty()
        1 * mockUserMapper.toEntity(_ as com.estore.authenticationauthorizationservice.user.dto.UserCreationDto) >> { args ->
            def uDto = args.get(0) as com.estore.authenticationauthorizationservice.user.dto.UserCreationDto
            UserEntity u = new UserEntity()
            u.setName(uDto.name)
            u.setNameAr(uDto.nameAr)
            u.setClient(new ClientEntity())
            u.getClient().setId(uDto.client.id)
            u
        }
        1 * mockUserRepository.save(_ as UserEntity) >> { args ->
            def u = args.get(0) as UserEntity
            u.client.id.toString() == 'c1932a1c-0666-4832-90f4-06e2f3acbf1e'
            u.setId(UUID.randomUUID())
            u
        }
        1 * mockUserMapper.toUserDto(_ as UserEntity) >> { args ->
            def u = args.get(0) as UserEntity
            com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().id(u.id).name(u.name).nameAr(u.nameAr).build()
        }

        expect:
        savedUser.id != null
        savedUser.name == 'NAME'
        savedUser.name == 'NAME'
    }

    def "should not save a new user if username already exists"() {
        given:
        com.estore.authenticationauthorizationservice.user.dto.UserCreationDto user = com.estore.authenticationauthorizationservice.user.dto.UserCreationDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .username('TEST')
                .client(ClientDto.builder().build())
                .build()

        when:
        userService.save(user, authenticationUser)

        then:
        1 * mockUserRepository.findOneByUsernameIgnoreCase('TEST') >> Optional.of(new UserEntity())
        0 * mockUserMapper.toEntity(_ as com.estore.authenticationauthorizationservice.user.dto.UserCreationDto)
        0 * mockUserRepository.save(_ as UserEntity)
        0 * mockUserMapper.toUserDto(_ as UserEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException)
    }

    def "should update an existing user"() {
        given:
        def userId = UUID.randomUUID()
        com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto userDto = com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto.builder()
                .id(userId)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .username("USERNAME")
                .password('PASSWORD')
                .enabled(true)
                .build()

        when:
        def updatedUser = userService.update(userDto, authenticationUser)

        then:
        1 * mockUserRepository.findOneByIdAndClient_Id(userId, authenticationUser.client) >> Optional.of(new UserEntity())
        1 * mockPasswordEncoder.encode('PASSWORD') >> { args -> args.get(0) }
        1 * mockUserRepository.save(_ as UserEntity) >> { args ->
            assert args.get(0).password == 'PASSWORD'
            args.get(0)
        }
        1 * mockUserMapper.toUserDto(_ as UserEntity) >> { args ->
            def u = args.get(0) as UserEntity
            com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().name(u.name).nameAr(u.nameAr).contactInfo(u.contactInfo).properties(u.properties).username(u.username).enabled(u.enabled).build()
        }

        expect:
        updatedUser.name == 'NAME'
        updatedUser.nameAr == 'NAME_AR'
        updatedUser.contactInfo == '{}'
        updatedUser.properties == '{}'
        updatedUser.username == 'USERNAME'
        updatedUser.enabled
    }

    def "should not update a non existing user and throw ResourceNotFoundException"() {
        given:
        def userId = UUID.randomUUID()
        com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto userDto = com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto.builder()
                .id(userId)
                .build()

        when:
        userService.update(userDto, authenticationUser)

        then:
        1 * mockUserRepository.findOneByIdAndClient_Id(userId, authenticationUser.client) >> Optional.empty()
        0 * mockUserRepository.save(_ as UserEntity)
        0 * mockUserMapper.toUserUpdatingDto(_ as UserEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

    def "should get a page of users for client"() {
        given:
        def searchQuery = ''
        def pageable = PageRequest.of(0, 10)

        when:
        def usersPage = userService.getAllForClient(pageable, searchQuery, authenticationUser)

        then:
        1 * mockUserRepository.findAllByClient_Id(pageable, authenticationUser.getClient()) >> {
            def user1 = new UserEntity()
            def user2 = new UserEntity()
            new PageImpl<>(List.of(user1, user2))
        }
        2 * mockUserMapper.toUserDto(_ as UserEntity) >> com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build()

        expect:
        usersPage.size == 2
        usersPage.number == 0
        usersPage.totalElements == 2
    }

    def "should get a page of users filtered by search query"() {
        given:
        def searchQuery = 'TEST'
        def pageable = PageRequest.of(0, 10)

        when:
        def usersPage = userService.getAllForClient(pageable, searchQuery, authenticationUser)

        then:
        1 * mockUserRepository.findAllBySearchQueryAndClient_Id(pageable, searchQuery, authenticationUser.getClient()) >> {
            def user1 = new UserEntity()
            def user2 = new UserEntity()
            new PageImpl<>(List.of(user1, user2))
        }
        2 * mockUserMapper.toUserDto(_ as UserEntity) >> com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build()

        expect:
        usersPage.size == 2
        usersPage.totalElements == 2
    }

    def "should get a existing user by id"() {
        given:
        def userId = UUID.randomUUID()

        when:
        def user = userService.get(userId, authenticationUser)

        then:
        1 * mockUserRepository.findOneByIdAndClient_Id(userId, authenticationUser.client) >> { args ->
            def u = new UserEntity()
            u.id = userId
            Optional.of(u)
        }
        1 * mockUserMapper.toUserDto(_ as UserEntity) >> { args ->
            def u = args.get(0) as UserEntity
            com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().id(u.id).build()
        }

        expect:
        user.id == userId
    }

    def "should not get a non existing user by id and throw ResourceNotFoundException"() {
        given:
        def userId = UUID.randomUUID()

        when:
        userService.get(userId, authenticationUser)

        then:
        1 * mockUserRepository.findOneByIdAndClient_Id(userId, authenticationUser.client) >> Optional.empty()
        0 * mockUserMapper.toUserDto(_ as UserEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

    def "should delete an existing user by id"() {
        given:
        def userId = UUID.randomUUID()

        when:
        userService.delete(userId, authenticationUser)

        then:
        1 * mockUserRepository.findOneByIdAndClient_Id(userId, authenticationUser.client) >> { args ->
            def u = new UserEntity()
            u.setId(userId)
            Optional.of(u)
        }
        1 * mockUserRepository.delete(_ as UserEntity) >> { args ->
            args.get(0).id == userId
        }
    }

    def "should not delete a non existing user by id and throw ResourceNotFoundException"() {
        given:
        def userId = UUID.randomUUID()

        when:
        userService.delete(userId, authenticationUser)

        then:
        1 * mockUserRepository.findOneByIdAndClient_Id(userId, authenticationUser.client) >> Optional.empty()
        0 * mockUserRepository.delete(_ as UserEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }
}
