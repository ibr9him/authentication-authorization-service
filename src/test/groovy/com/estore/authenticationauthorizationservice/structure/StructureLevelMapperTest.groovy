package com.estore.authenticationauthorizationservice.structure


import com.estore.authenticationauthorizationservice.client.ClientEntity
import com.estore.authenticationauthorizationservice.client.ClientMapper
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelUpdatingDto
import com.estore.authenticationauthorizationservice.user.UserMapper
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class StructureLevelMapperTest extends Specification {

    UserMapper mockUserMapper = Mock(UserMapper.class)
    ClientMapper mockClientMapper = Mock(ClientMapper.class)
    @Subject
    StructureLevelMapper structureLevelMapper = new StructureLevelMapperImpl(userMapper: mockUserMapper, clientMapper: mockClientMapper)

    def "should convert from structure level entity to dto"() {
        given:
        def id = UUID.randomUUID()
        def dummySet = Set.of()
        def createdDate = Instant.now()
        def structureLevelEntity = new StructureLevelEntity()
        structureLevelEntity.id = id
        structureLevelEntity.name = 'NAME'
        structureLevelEntity.nameAr = 'NAME_AR'
        structureLevelEntity.properties = '{}'
        structureLevelEntity.parent = new StructureLevelEntity()
        structureLevelEntity.children = dummySet as Set<StructureLevelEntity>
        structureLevelEntity.manager = new com.estore.authenticationauthorizationservice.user.UserEntity()
        structureLevelEntity.users = dummySet as Set<com.estore.authenticationauthorizationservice.user.UserEntity>
        structureLevelEntity.createdBy = 'TEST'
        structureLevelEntity.createdDate = createdDate

        when:
        def structureLevelDto = structureLevelMapper.toStructureLevelDto(structureLevelEntity)

        then:
        1 * mockUserMapper.toUserDto(_ as com.estore.authenticationauthorizationservice.user.UserEntity) >> com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build()

        expect:
        assert structureLevelDto instanceof com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto
        structureLevelDto.id == id
        structureLevelDto.name == 'NAME'
        structureLevelDto.nameAr == 'NAME_AR'
        structureLevelDto.properties == '{}'
        structureLevelEntity.parent == new StructureLevelEntity()
        structureLevelEntity.children == Set.of()
        structureLevelEntity.manager == new com.estore.authenticationauthorizationservice.user.UserEntity()
        structureLevelEntity.users == Set.of()
        structureLevelDto.createdBy == 'TEST'
        structureLevelDto.createdDate == createdDate
    }

    def "should convert from structure level creation dto to entity"() {
        given:
        def structureLevelDto = com.estore.authenticationauthorizationservice.structure.dto.StructureLevelCreationDto.builder().name('NAME').nameAr('NAME_AR').properties('{}')
                .parent(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build())
                .manager(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build())
                .children(Set.of(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build()))
                .client(ClientDto.builder().build())
                .users(Set.of(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build()))
                .build()

        when:
        def structureLevelEntity = structureLevelMapper.toEntity(structureLevelDto)

        then:
        1 * mockUserMapper.toEntity(_ as com.estore.authenticationauthorizationservice.user.dto.UserDto) >> new com.estore.authenticationauthorizationservice.user.UserEntity()
        3 * mockUserMapper.toEntity(_ as Set<com.estore.authenticationauthorizationservice.user.dto.UserDto>) >> Set.of(new com.estore.authenticationauthorizationservice.user.UserEntity())
        1 * mockClientMapper.toEntity(_ as ClientDto) >> new ClientEntity()

        expect:
        assert structureLevelEntity instanceof StructureLevelEntity
        structureLevelEntity.name == 'NAME'
        structureLevelEntity.nameAr == 'NAME_AR'
        structureLevelEntity.properties == '{}'
        structureLevelEntity.parent != null
        structureLevelEntity.children != null
        structureLevelEntity.manager != null
        structureLevelEntity.users != null
        structureLevelEntity.client != null
    }

    def "should convert from structure level updating dto to entity"() {
        def id = UUID.randomUUID()
        given:
        def structureLevelDto = StructureLevelUpdatingDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .properties('{}')
                .parent(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build())
                .manager(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build())
                .build()
        structureLevelDto.children.add(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build())
        structureLevelDto.users.add(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build())

        when:
        def structureLevelEntity = structureLevelMapper.toEntity(structureLevelDto)

        then:
        1 * mockUserMapper.toEntity(_ as com.estore.authenticationauthorizationservice.user.dto.UserDto) >> new com.estore.authenticationauthorizationservice.user.UserEntity()
        3 * mockUserMapper.toEntity(_ as Set<com.estore.authenticationauthorizationservice.user.dto.UserDto>) >> Set.of(new com.estore.authenticationauthorizationservice.user.UserEntity())

        expect:
        assert structureLevelEntity instanceof StructureLevelEntity
        structureLevelEntity.id == id
        structureLevelEntity.name == 'NAME'
        structureLevelEntity.nameAr == 'NAME_AR'
        structureLevelEntity.properties == '{}'
        structureLevelEntity.parent != null
        structureLevelEntity.children != null
        structureLevelEntity.manager != null
        structureLevelEntity.users != null
    }

    def "should convert from structure level dto to entity"() {
        given:
        def createdDate = Instant.now()
        def id = UUID.randomUUID()
        def structureLevelDto = com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .properties('{}')
                .parent(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build())
                .children(Set.of())
                .manager(com.estore.authenticationauthorizationservice.user.dto.UserDto.builder().build())
                .users(Set.of())
                .createdBy('TEST')
                .createdDate(createdDate)
                .build()

        when:
        def structureLevelEntity = structureLevelMapper.toEntity(structureLevelDto)

        then:
        assert structureLevelEntity instanceof StructureLevelEntity
        structureLevelEntity.id == id
        structureLevelEntity.name == 'NAME'
        structureLevelEntity.nameAr == 'NAME_AR'
        structureLevelEntity.properties == '{}'
        structureLevelDto.parent != null
        structureLevelDto.children != null
        structureLevelDto.manager != null
        structureLevelDto.users != null
        structureLevelEntity.createdBy == 'TEST'
        structureLevelEntity.createdDate == createdDate
    }
}
