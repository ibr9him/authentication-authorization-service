package com.estore.authenticationauthorizationservice.user


import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class UserMapperTest extends Specification {

    com.estore.authenticationauthorizationservice.role.RoleMapper mockRoleMapper = Mock(com.estore.authenticationauthorizationservice.role.RoleMapper.class)
    com.estore.authenticationauthorizationservice.client.ClientMapper mockClientMapper = Mock(com.estore.authenticationauthorizationservice.client.ClientMapper.class)
    com.estore.authenticationauthorizationservice.structure.StructureLevelMapper mockStructureLevelMapper = Mock(com.estore.authenticationauthorizationservice.structure.StructureLevelMapper.class)
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
        userEntity.role = new com.estore.authenticationauthorizationservice.role.RoleEntity()
        userEntity.client = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        userEntity.structureLevel = new com.estore.authenticationauthorizationservice.structure.StructureLevelEntity()
        userEntity.createdBy = ''
        userEntity.createdDate = createdDate

        when:
        def userDto = userMapper.toUserDto(userEntity)

        then:
        1 * mockRoleMapper.toRoleDto(_ as com.estore.authenticationauthorizationservice.role.RoleEntity) >> com.estore.authenticationauthorizationservice.role.dto.RoleDto.builder().build()
        1 * mockStructureLevelMapper.toStructureLevelDto(_ as com.estore.authenticationauthorizationservice.structure.StructureLevelEntity) >> com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build()

        expect:
        assert userDto instanceof com.estore.authenticationauthorizationservice.user.dto.UserDto
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
        def userDto = com.estore.authenticationauthorizationservice.user.dto.UserCreationDto.builder()
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
        1 * mockClientMapper.toEntity(_ as ClientDto) >> new com.estore.authenticationauthorizationservice.client.ClientEntity()

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
        def userDto = com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto.builder()
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
        def userDto = com.estore.authenticationauthorizationservice.user.dto.UserDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .username('USERNAME')
                .enabled(true)
                .role(com.estore.authenticationauthorizationservice.role.dto.RoleDto.builder().build())
                .structureLevel(com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.builder().build())
                .createdBy('')
                .createdDate(createdDate)
                .build()

        when:
        def userEntity = userMapper.toEntity(userDto)

        then:
        1 * mockRoleMapper.toEntity(_ as com.estore.authenticationauthorizationservice.role.dto.RoleDto) >> new com.estore.authenticationauthorizationservice.role.RoleEntity()
        1 * mockStructureLevelMapper.toEntity(_ as com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto) >> new com.estore.authenticationauthorizationservice.structure.StructureLevelEntity()

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
