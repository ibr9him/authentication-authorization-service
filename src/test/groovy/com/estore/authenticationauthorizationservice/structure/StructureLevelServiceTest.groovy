package com.estore.authenticationauthorizationservice.structure


import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelUpdatingDto
import spock.lang.Specification
import spock.lang.Subject

class StructureLevelServiceTest extends Specification {

    private com.estore.authenticationauthorizationservice.authentication.AuthenticationUser authenticationUser = new com.estore.authenticationauthorizationservice.authentication.AuthenticationUser("User", "Password", Collections.emptyList())

    StructureLevelRepository mockStructureLevelRepository = Mock(StructureLevelRepository.class)
    StructureLevelMapper mockStructureLevelMapper = Mock(StructureLevelMapper.class)
    com.estore.authenticationauthorizationservice.user.UserMapper mockUserMapper = Mock(com.estore.authenticationauthorizationservice.user.UserMapper.class)
    @Subject
    StructureLevelService structureLevelService = new StructureLevelService(mockStructureLevelRepository, mockStructureLevelMapper, mockUserMapper)

    def setup() {
        authenticationUser.setClient(UUID.fromString("c1932a1c-0666-4832-90f4-06e2f3acbf1e"))
    }

    def "should save a new structure level"() {
        given:
        def structureLevelCreationDto = com.estore.authenticationauthorizationservice.structure.dto.StructureLevelCreationDto.builder().name("NAME").nameAr("NAME_AR").client(ClientDto.builder().build()).build()

        when:
        structureLevelService.save(structureLevelCreationDto, authenticationUser)

        then:
        1 * mockStructureLevelMapper.toEntity(_ as com.estore.authenticationauthorizationservice.structure.dto.StructureLevelCreationDto) >> { args ->
            assert args.get(0).client.id.toString() == "c1932a1c-0666-4832-90f4-06e2f3acbf1e"
            new StructureLevelEntity()
        }
        1 * mockStructureLevelRepository.save(_ as StructureLevelEntity) >> { args ->
            def s = args.get(0) as StructureLevelEntity
            s.setId(UUID.randomUUID())
            s
        }
        1 * mockStructureLevelMapper.toStructureLevelDto(_ as StructureLevelEntity)
    }

    def "should update an existing structure level"() {
        given:
        def parentId = UUID.randomUUID()
        def parent = com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().id(parentId).build()
        def childId = UUID.randomUUID()
        def child = com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().id(childId).build()
        def userId = UUID.randomUUID()
        def user = com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().id(userId).build()

        def structureLevelDtoId = UUID.randomUUID()
        def structureLevelDto = StructureLevelUpdatingDto.builder().id(structureLevelDtoId).name('NAME').nameAr('NAME_AR').properties('{"test":123}')
                .parent(parent).children(Set.of(child)).users(Set.of(user)).manager(user).client(ClientDto.builder().build()).build()

        when:
        def updatedStructureLevel = structureLevelService.update(structureLevelDto, authenticationUser)

        then:
        1 * mockStructureLevelRepository.findOneByIdAndClient_Id(structureLevelDtoId, authenticationUser.client) >> Optional.of(new StructureLevelEntity())
        1 * mockStructureLevelMapper.toEntity(_ as com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto) >> { args ->
            def s = new StructureLevelEntity()
            s.id = parentId
            s
        }
        1 * mockStructureLevelMapper.toEntity(_ as Set<com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto>) >> { args ->
            def s = new StructureLevelEntity()
            s.id = childId
            Set.of(s)
        }
        1 * mockUserMapper.toEntity(_ as com.estore.authenticationauthorizationservice.user.dto.UserDto) >> { args ->
            def u = new com.estore.authenticationauthorizationservice.user.UserEntity()
            u.setId(userId)
            u
        }
        1 * mockUserMapper.toEntity(_ as Set<com.estore.authenticationauthorizationservice.user.dto.UserDto>) >> { args ->
            def u1 = new com.estore.authenticationauthorizationservice.user.UserEntity()
            u1.setId(userId)
            Set.of(u1)
        }
        1 * mockStructureLevelRepository.save(_ as StructureLevelEntity) >> { args -> args.get(0) }
        1 * mockStructureLevelMapper.toStructureLevelDto(_ as StructureLevelEntity) >> { args ->
            def s = args.get(0) as StructureLevelEntity
            com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().id(s.id).name(s.name).nameAr(s.nameAr).properties(s.properties)
                    .parent(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().id(s.parent.id).build())
                    .children(Set.of(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().id(s.children[0].id).build()))
                    .users(Set.of(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().id(s.users[0].id).build()))
                    .manager(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().id(s.manager.id).build())
                    .build()
        }

        expect:
        updatedStructureLevel.name == 'NAME'
        updatedStructureLevel.nameAr == 'NAME_AR'
        updatedStructureLevel.properties == '{"test":123}'
        updatedStructureLevel.parent.id == parentId
        updatedStructureLevel.children.size() == 1
        updatedStructureLevel.users.size() == 1
        updatedStructureLevel.manager.id == userId
    }

    def "should not update a non existing structure level and throw ResourceNotFoundException"() {
        given:
        def structureLevelDtoId = UUID.randomUUID()
        def structureLevelDto = StructureLevelUpdatingDto.builder().id(structureLevelDtoId).build()

        when:
        structureLevelService.update(structureLevelDto, authenticationUser)

        then:
        1 * mockStructureLevelRepository.findOneByIdAndClient_Id(structureLevelDtoId, authenticationUser.client) >> Optional.empty()
        0 * mockStructureLevelMapper.toEntity(_ as com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto)
        0 * mockStructureLevelMapper.toEntity(_ as Set<com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto>)
        0 * mockUserMapper.toEntity(_ as com.estore.authenticationauthorizationservice.user.dto.UserDto)
        0 * mockUserMapper.toEntity(_ as Set<com.estore.authenticationauthorizationservice.user.dto.UserDto>)
        0 * mockStructureLevelRepository.save(_ as StructureLevelEntity)
        0 * mockStructureLevelMapper.toStructureLevelDto(_ as StructureLevelEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

    def "should get an existing structure level by id"() {
        given:
        def structureLevelId = UUID.randomUUID()

        when:
        def structureLevel = structureLevelService.get(structureLevelId, authenticationUser)

        then:
        1 * mockStructureLevelRepository.findOneByIdAndClient_Id(structureLevelId, authenticationUser.client) >> Optional.of(new StructureLevelEntity())
        1 * mockStructureLevelMapper.toStructureLevelDto(_ as StructureLevelEntity) >> com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().id(structureLevelId).build()

        expect:
        structureLevel.id == structureLevelId
    }

    def "should not get a non existing structure level by id and throw ResourceNotFoundException"() {
        given:
        def structureLevelId = UUID.randomUUID()

        when:
        structureLevelService.get(structureLevelId, authenticationUser)

        then:
        1 * mockStructureLevelRepository.findOneByIdAndClient_Id(structureLevelId, authenticationUser.client) >> Optional.empty()
        0 * mockStructureLevelMapper.toStructureLevelDto(_ as StructureLevelEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

    def "should delete an existing structure level by id"() {
        given:
        def structureLevelId = UUID.randomUUID()

        when:
        structureLevelService.delete(structureLevelId, authenticationUser)

        then:
        1 * mockStructureLevelRepository.findOneByIdAndClient_Id(structureLevelId, authenticationUser.client) >> Optional.of(new StructureLevelEntity())
        1 * mockStructureLevelRepository.delete(_ as StructureLevelEntity)
    }

    def "should not delete a non existing structure level by id and throw ResourceNotFoundException"() {
        given:
        def structureLevelId = UUID.randomUUID()

        when:
        structureLevelService.delete(structureLevelId, authenticationUser)

        then:
        1 * mockStructureLevelRepository.findOneByIdAndClient_Id(structureLevelId, authenticationUser.client) >> Optional.empty()
        0 * mockStructureLevelRepository.delete(_ as StructureLevelEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

}
