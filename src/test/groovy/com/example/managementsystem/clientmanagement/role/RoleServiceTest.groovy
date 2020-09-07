package com.example.managementsystem.clientmanagement.role

import com.example.managementsystem.authentication.AuthenticationUser
import com.example.managementsystem.clientmanagement.client.dto.ClientDto
import com.example.managementsystem.clientmanagement.role.authority.Authority
import com.example.managementsystem.clientmanagement.role.dto.RoleCreationDto
import com.example.managementsystem.clientmanagement.role.dto.RoleDto
import com.example.managementsystem.clientmanagement.role.dto.RoleUpdatingDto
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException
import com.example.managementsystem.util.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class RoleServiceTest extends Specification {

    AuthenticationUser authenticationUser = new AuthenticationUser("USERNAME", "PASSWORD", Collections.emptyList())

    RoleRepository mockRoleRepository = Mock(RoleRepository.class)
    RoleMapper mockRoleMapper = Mock(RoleMapper)
    @Subject
    RoleService roleService = new RoleService(mockRoleRepository, mockRoleMapper)

    def setup() {
        authenticationUser.setClient(UUID.fromString("c1932a1c-0666-4832-90f4-06e2f3acbf1e"))
    }

    def "should save a new role"() {
        given:
        def roleDto = RoleCreationDto.builder().name('name test').nameAr('name_ar').client(ClientDto.builder().build()).build()

        when:
        roleService.save(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id(_ as String, _ as UUID) >> Optional.empty()
        1 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id(_ as String, _ as UUID) >> Optional.empty()
        1 * mockRoleMapper.toEntity(_ as RoleCreationDto) >> { args ->
            assert args.get(0).client.id.toString() == 'c1932a1c-0666-4832-90f4-06e2f3acbf1e'
            def r = new RoleEntity()
            r
        }
        1 * mockRoleRepository.save(_ as RoleEntity) >> { args ->
            def r = args.get(0) as RoleEntity
            r.id = UUID.randomUUID()
            r
        }
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity)
    }

    def "should not save a new role if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def roleDto = RoleCreationDto.builder().name('NAME').nameAr('NAME_AR').client(ClientDto.builder().build()).build()

        when:
        roleService.save(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id('NAME', authenticationUser.client) >> Optional.of(new RoleEntity())
        0 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id('NAME_AR', authenticationUser.client)
        0 * mockRoleMapper.toEntity(_ as RoleDto)
        0 * mockRoleRepository.save(_ as RoleEntity)
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not save a new role if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def roleDto = RoleCreationDto.builder().name('NAME').nameAr('NAME_AR').client(ClientDto.builder().build()).build()

        when:
        roleService.save(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id('NAME', authenticationUser.client) >> Optional.empty()
        1 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id('NAME_AR', authenticationUser.client) >> Optional.of(new RoleEntity())
        0 * mockRoleMapper.toEntity(_ as RoleDto)
        0 * mockRoleRepository.save(_ as RoleEntity)
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should update an existing role"() {
        given:
        def id = UUID.randomUUID()
        def roleDto = RoleUpdatingDto.builder().id(id).name('name 1').nameAr('name_ar').authorities(Set.of(new Authority())).build()

        when:
        def role = roleService.update(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id('name 1', authenticationUser.client) >> Optional.empty()
        1 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id('name_ar', authenticationUser.client) >> Optional.empty()
        1 * mockRoleRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.of(new RoleEntity())
        1 * mockRoleRepository.save(_ as RoleEntity) >> { args -> args.get(0) }
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> { args ->
            def r = args.get(0) as RoleEntity
            RoleDto.builder().id(r.id).name(r.name).nameAr(r.nameAr).code(r.code).authorities(r.authorities).build()
        }
//TODO TEST UPDATING AUTHORISES
        expect:
        role.name == 'name 1'
        role.nameAr == 'name_ar'
        role.code == 'NAME_1'
    }

    def "should not update a non existing role and throw ResourceNotFoundException"() {
        given:
        def id = UUID.randomUUID()
        def roleDto = RoleUpdatingDto.builder().id(id).name('NAME').nameAr('NAME_AR').authorities(Set.of(new Authority())).build()

        when:
        roleService.update(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id('NAME', authenticationUser.client) >> Optional.empty()
        1 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id('NAME_AR', authenticationUser.client) >> Optional.empty()
        1 * mockRoleRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.empty()
        0 * mockRoleRepository.save(_ as RoleEntity)
        0 * mockRoleMapper.toRoleDto(_ as RoleEntity)
        thrown(ResourceNotFoundException)
    }

    def "should not update an existing role if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        def roleDto = RoleUpdatingDto.builder().id(id).name('NAME').nameAr('NAME_AR').authorities(Set.of(new Authority())).build()

        when:
        roleService.update(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id('NAME', authenticationUser.client) >> Optional.of(new RoleEntity())
        0 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id('NAME_AR', authenticationUser.client)
        0 * mockRoleRepository.findOneByIdAndClient_Id(id, authenticationUser.client)
        0 * mockRoleRepository.save(_ as RoleEntity)
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not update an existing role if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        def roleDto = RoleUpdatingDto.builder().id(id).name('NAME').nameAr('NAME_AR').authorities(Set.of(new Authority())).build()

        when:
        roleService.update(roleDto, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByNameIgnoreCaseAndClient_Id('NAME', authenticationUser.client) >> Optional.empty()
        1 * mockRoleRepository.findOneByNameArIgnoreCaseAndClient_Id('NAME_AR', authenticationUser.client) >> Optional.of(new RoleEntity())
        0 * mockRoleRepository.findOneByIdAndClient_Id(id, authenticationUser.client)
        0 * mockRoleRepository.save(_ as RoleEntity)
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should get a page of roles for client"() {
        given:
        def searchQuery = ''
        def pageable = PageRequest.of(0, 10)

        when:
        def rolesPage = roleService.getAllForClient(pageable, searchQuery, authenticationUser)

        then:
        1 * mockRoleRepository.findAllByClient_Id(pageable, authenticationUser.getClient()) >> {
            def role1 = new RoleEntity()
            def role2 = new RoleEntity()
            new PageImpl<>(List.of(role1, role2))
        }
        2 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()

        expect:
        rolesPage.size == 2
        rolesPage.number == 0
        rolesPage.totalElements == 2
    }

    def "should get a page of roles filtered by search query"() {
        given:
        def searchQuery = 'TEST'
        def pageable = PageRequest.of(0, 10)

        when:
        def rolesPage = roleService.getAllForClient(pageable, searchQuery, authenticationUser)

        then:
        1 * mockRoleRepository.findAllBySearchQueryAndClient_Id(pageable, searchQuery, authenticationUser.getClient()) >> {
            def role1 = new RoleEntity()
            def role2 = new RoleEntity()
            new PageImpl<>(List.of(role1, role2))
        }
        2 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()

        expect:
        rolesPage.size == 2
        rolesPage.number == 0
        rolesPage.totalElements == 2
    }

    def "should get a existing role by id"() {
        given:
        def roleId = UUID.randomUUID()

        when:
        roleService.get(roleId, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByIdAndClient_Id(roleId, authenticationUser.client) >> Optional.of(new RoleEntity())
        1 * mockRoleMapper.toRoleDto(_ as RoleEntity) >> RoleDto.builder().build()
    }

    def "should not get a non existing role by id and throw ResourceNotFoundException"() {
        given:
        def roleId = UUID.randomUUID()

        when:
        roleService.get(roleId, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByIdAndClient_Id(roleId, authenticationUser.client) >> Optional.empty()
        0 * mockRoleMapper.toRoleDto(_ as RoleEntity)
        thrown(ResourceNotFoundException)
    }

    def "should delete an existing role by id"() {
        given:
        def roleId = UUID.randomUUID()

        when:
        roleService.delete(roleId, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByIdAndClient_Id(roleId, authenticationUser.client) >> Optional.of(new RoleEntity())
        1 * mockRoleRepository.delete(_ as RoleEntity)
    }

    def "should not delete a non existing role by id and throw ResourceNotFoundException"() {
        given:
        def roleId = UUID.randomUUID()

        when:
        roleService.delete(roleId, authenticationUser)

        then:
        1 * mockRoleRepository.findOneByIdAndClient_Id(roleId, authenticationUser.client) >> Optional.empty()
        0 * mockRoleRepository.delete(_ as RoleEntity)
        thrown(ResourceNotFoundException)
    }
}
