package com.estore.authenticationauthorizationservice.user

import com.estore.authenticationauthorizationservice.client.ClientEntity
import com.estore.authenticationauthorizationservice.client.ClientMapper
import com.estore.authenticationauthorizationservice.role.RoleEntity
import com.estore.authenticationauthorizationservice.role.RoleMapper
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.role.dto.RoleDto
import com.estore.authenticationauthorizationservice.user.dto.UserCreationDto
import com.estore.authenticationauthorizationservice.user.dto.UserDto
import com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class UserMapperTest extends Specification {

    RoleMapper mockRoleMapper = Mock(RoleMapper.class)
    ClientMapper mockClientMapper = Mock(ClientMapper.class)
    @Subject
    UserMapper userMapper = new UserMapperImpl(roleMapper: mockRoleMapper, clientMapper: mockClientMapper)

    def "should convert from entity to dto"() {
        given:
        def id = UUID.randomUUID()
        def createdDate = Instant.now()
        def userEntity = new UserEntity()
        userEntity.id = id
        userEntity.name = 'NAME'
        userEntity.nameAr = 'NAME_AR'
        userEntity.contactInfo = '{}'
        userEntity.properties = '{}'
        userEntity.username = 'USERNAME'
        userEntity.password = 'PASSWORD'
        userEntity.enabled = true
        userEntity.role = new RoleEntity()
        userEntity.client = new ClientEntity()
        userEntity.createdBy = ''
        userEntity.createdDate = createdDate

        when:
        def userDto = userMapper.toUserDto(userEntity)

        then:
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()

        expect:
        assert userDto instanceof UserDto
        userDto.id == id
        userDto.name == 'NAME'
        userDto.nameAr == 'NAME_AR'
        userDto.contactInfo == '{}'
        userDto.properties == '{}'
        userDto.username == 'USERNAME'
        userDto.enabled
        userDto.role != null
        userDto.createdBy == ''
        userDto.createdDate == createdDate
    }

    def "should convert from creation dto to entity"() {
        given:
        def userDto = UserCreationDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .username('USERNAME')
                .password('PASSWORD')
                .enabled(true)
                .client(ClientDto.builder().build())
                .build()

        when:
        def userEntity = userMapper.toEntity(userDto)

        then:
        1 * mockClientMapper.toEntity(_ as ClientDto) >> new ClientEntity()

        expect:
        assert userEntity instanceof UserEntity
        userEntity.name == 'NAME'
        userEntity.nameAr == 'NAME_AR'
        userEntity.contactInfo == '{}'
        userEntity.properties == '{}'
        userEntity.username == 'USERNAME'
        userEntity.password == 'PASSWORD'
        userEntity.enabled
        userEntity.client != null
    }

    def "should convert from updating dto to entity"() {
        def id = UUID.randomUUID()
        given:
        def userDto = UserUpdatingDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .username('USERNAME')
                .password('PASSWORD')
                .enabled(true)
                .build()

        when:
        def userEntity = userMapper.toEntity(userDto)

        then:
        assert userEntity instanceof UserEntity
        userEntity.id == id
        userEntity.name == 'NAME'
        userEntity.nameAr == 'NAME_AR'
        userEntity.contactInfo == '{}'
        userEntity.properties == '{}'
        userEntity.username == 'USERNAME'
        userEntity.password == 'PASSWORD'
        userEntity.enabled
    }

    def "should convert from dto to entity"() {
        given:
        def id = UUID.randomUUID()
        def createdDate = Instant.now()
        def userDto = UserDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .username('USERNAME')
                .enabled(true)
                .role(RoleDto.builder().build())
                .createdBy('')
                .createdDate(createdDate)
                .build()

        when:
        def userEntity = userMapper.toEntity(userDto)

        then:
        1 * mockRoleMapper.toEntity(_ as RoleDto) >> new RoleEntity()

        expect:
        assert userEntity instanceof UserEntity
        userEntity.id == id
        userEntity.name == 'NAME'
        userEntity.nameAr == 'NAME_AR'
        userEntity.contactInfo == '{}'
        userEntity.properties == '{}'
        userEntity.username == 'USERNAME'
        userEntity.enabled
        userEntity.role != null
        userEntity.createdBy == ''
        userEntity.createdDate == createdDate
    }
}
