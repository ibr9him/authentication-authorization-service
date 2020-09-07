package com.example.managementsystem.clientmanagement.user

import com.example.managementsystem.clientmanagement.client.ClientEntity
import com.example.managementsystem.clientmanagement.client.ClientMapper
import com.example.managementsystem.clientmanagement.client.dto.ClientDto
import com.example.managementsystem.clientmanagement.role.RoleEntity
import com.example.managementsystem.clientmanagement.role.RoleMapper
import com.example.managementsystem.clientmanagement.role.dto.RoleDto
import com.example.managementsystem.clientmanagement.structure.StructureLevelEntity
import com.example.managementsystem.clientmanagement.structure.StructureLevelMapper
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelDto
import com.example.managementsystem.clientmanagement.user.dto.UserCreationDto
import com.example.managementsystem.clientmanagement.user.dto.UserDto
import com.example.managementsystem.clientmanagement.user.dto.UserUpdatingDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class UserMapperTest extends Specification {

    RoleMapper mockRoleMapper = Mock(RoleMapper.class)
    ClientMapper mockClientMapper = Mock(ClientMapper.class)
    StructureLevelMapper mockStructureLevelMapper = Mock(StructureLevelMapper.class)
    @Subject
    UserMapper userMapper = new UserMapperImpl(roleMapper: mockRoleMapper, clientMapper: mockClientMapper, structureLevelMapper: mockStructureLevelMapper)

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
        userEntity.structureLevel = new StructureLevelEntity()
        userEntity.createdBy = ''
        userEntity.createdDate = createdDate

        when:
        def userDto = userMapper.toUserDto(userEntity)

        then:
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()
        1 * mockStructureLevelMapper.toStructureLevelDto(_ as StructureLevelEntity) >> StructureLevelDto.builder().build()

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
        userDto.structureLevel != null
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
                .structureLevel(StructureLevelDto.builder().build())
                .createdBy('')
                .createdDate(createdDate)
                .build()

        when:
        def userEntity = userMapper.toEntity(userDto)

        then:
        1 * mockRoleMapper.toEntity(_ as RoleDto) >> new RoleEntity()
        1 * mockStructureLevelMapper.toEntity(_ as StructureLevelDto) >> new StructureLevelEntity()

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
        userEntity.structureLevel != null
        userEntity.createdBy == ''
        userEntity.createdDate == createdDate
    }
}
