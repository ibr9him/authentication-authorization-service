package com.example.managementsystem.clientmanagement.structure

import com.example.managementsystem.clientmanagement.client.ClientEntity
import com.example.managementsystem.clientmanagement.client.ClientMapper
import com.example.managementsystem.clientmanagement.client.dto.ClientDto
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelCreationDto
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelDto
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelUpdatingDto
import com.example.managementsystem.clientmanagement.user.UserEntity
import com.example.managementsystem.clientmanagement.user.UserMapper
import com.example.managementsystem.clientmanagement.user.dto.UserDto
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
        structureLevelEntity.manager = new UserEntity()
        structureLevelEntity.users = dummySet as Set<UserEntity>
        structureLevelEntity.createdBy = 'TEST'
        structureLevelEntity.createdDate = createdDate

        when:
        def structureLevelDto = structureLevelMapper.toStructureLevelDto(structureLevelEntity)

        then:
        1 * mockUserMapper.toUserDto(_ as UserEntity) >> UserDto.builder().build()

        expect:
        assert structureLevelDto instanceof StructureLevelDto
        structureLevelDto.id == id
        structureLevelDto.name == 'NAME'
        structureLevelDto.nameAr == 'NAME_AR'
        structureLevelDto.properties == '{}'
        structureLevelEntity.parent == new StructureLevelEntity()
        structureLevelEntity.children == Set.of()
        structureLevelEntity.manager == new UserEntity()
        structureLevelEntity.users == Set.of()
        structureLevelDto.createdBy == 'TEST'
        structureLevelDto.createdDate == createdDate
    }

    def "should convert from structure level creation dto to entity"() {
        given:
        def structureLevelDto = StructureLevelCreationDto.builder().name('NAME').nameAr('NAME_AR').properties('{}')
                .parent(StructureLevelDto.builder().build())
                .manager(UserDto.builder().build())
                .children(Set.of(StructureLevelDto.builder().build()))
                .client(ClientDto.builder().build())
                .users(Set.of(UserDto.builder().build()))
                .build()

        when:
        def structureLevelEntity = structureLevelMapper.toEntity(structureLevelDto)

        then:
        1 * mockUserMapper.toEntity(_ as UserDto) >> new UserEntity()
        3 * mockUserMapper.toEntity(_ as Set<UserDto>) >> Set.of(new UserEntity())
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
                .parent(StructureLevelDto.builder().build())
                .manager(UserDto.builder().build())
                .build()
        structureLevelDto.children.add(StructureLevelDto.builder().build())
        structureLevelDto.users.add(UserDto.builder().build())

        when:
        def structureLevelEntity = structureLevelMapper.toEntity(structureLevelDto)

        then:
        1 * mockUserMapper.toEntity(_ as UserDto) >> new UserEntity()
        3 * mockUserMapper.toEntity(_ as Set<UserDto>) >> Set.of(new UserEntity())

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
        def structureLevelDto = StructureLevelDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .properties('{}')
                .parent(StructureLevelDto.builder().build())
                .children(Set.of())
                .manager(UserDto.builder().build())
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
