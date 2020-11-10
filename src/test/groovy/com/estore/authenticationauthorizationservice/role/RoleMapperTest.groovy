package com.estore.authenticationauthorizationservice.role


import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.role.RoleMapperImpl
import com.estore.authenticationauthorizationservice.role.authority.Authority
import com.estore.authenticationauthorizationservice.role.dto.RoleDto
import spock.lang.Specification
import spock.lang.Subject

class RoleMapperTest extends Specification {

    @Subject
    RoleMapper roleMapper = new RoleMapperImpl()

    def "should convert from role dto to entity"() {
        given:
        def id = UUID.randomUUID()
        def roleDto = RoleDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .code('CODE')
                .authorities(Set.of(new Authority()))
                .build()

        when:
        def roleEntity = roleMapper.toEntity(roleDto)

        then:
        assert roleEntity instanceof RoleEntity
        roleEntity.id == id
        roleEntity.name == 'NAME'
        roleEntity.nameAr == 'NAME_AR'
        roleEntity.code == 'CODE'
        roleEntity.authorities != null
    }

    def "should convert from role creation dto to entity"() {
        given:
        def roleDto = com.estore.authenticationauthorizationservice.role.dto.RoleCreationDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .code('CODE')
                .authorities(Set.of(new Authority()))
                .client(ClientDto.builder().build())
                .build()

        when:
        def roleEntity = roleMapper.toEntity(roleDto)

        then:
        assert roleEntity instanceof RoleEntity
        roleEntity.name == 'NAME'
        roleEntity.nameAr == 'NAME_AR'
        roleEntity.code == 'CODE'
        roleEntity.authorities != null
        roleEntity.client != null
    }

    def "should convert from role updating dto to entity"() {
        given:
        def roleDto = com.estore.authenticationauthorizationservice.role.dto.RoleUpdatingDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .code('CODE')
                .authorities(Set.of(new Authority()))
                .build()

        when:
        def roleEntity = roleMapper.toEntity(roleDto)

        then:
        assert roleEntity instanceof RoleEntity
        roleEntity.name == 'NAME'
        roleEntity.nameAr == 'NAME_AR'
        roleEntity.code == 'CODE'
        roleEntity.authorities != null
    }

    def "should convert from role entity to dto"() {
        given:
        def id = UUID.randomUUID()
        def roleEntity = new RoleEntity()
        roleEntity.id = id
        roleEntity.name = 'NAME'
        roleEntity.nameAr = 'NAME_AR'
        roleEntity.code = 'CODE'
        roleEntity.authorities = Set.of(new Authority())

        when:
        def roleDto = roleMapper.toRoleDto(roleEntity)

        then:
        assert roleDto instanceof RoleDto
        roleDto.id == id
        roleDto.name == 'NAME'
        roleDto.nameAr == 'NAME_AR'
        roleDto.code == 'CODE'
        roleDto.authorities != null
    }
}
