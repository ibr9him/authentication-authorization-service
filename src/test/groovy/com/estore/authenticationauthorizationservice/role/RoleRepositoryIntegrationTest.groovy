package com.estore.authenticationauthorizationservice.role


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager

@ActiveProfiles("Test")
@DataJpaTest
@Import(com.estore.authenticationauthorizationservice.config.DataAccessConfiguration.class)
class RoleRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    RoleRepository roleRepository

    def cleanup() {
        println('Cleaning up after a test!')
        roleRepository.deleteAll()
    }

    def "should save a role to the database with auditing info"() {
        given:
        def role = new RoleEntity()
        role.setName('NAME')
        role.setNameAr('NAME_AR')
        role.setCode('NAME')

        when:
        def persistedRole = roleRepository.save(role)

        then:
        def retrievedRole = entityManager.find(RoleEntity, persistedRole.id)
        retrievedRole.id == persistedRole.id
        retrievedRole.createdBy == 'Dummy User'
        retrievedRole.createdDate != null
        retrievedRole.lastModifiedBy == 'Dummy User'
        retrievedRole.lastModifiedDate != null
    }

    def "should find role by id and clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName('TEST')
        setupClient.setNameAr('TEST_AR')
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupRole = new RoleEntity()
        setupRole.setName('NAME')
        setupRole.setNameAr('NAME_AR')
        setupRole.setClient(client)
        setupRole.setCode('NAME')
        entityManager.persist(setupRole)
        def role = entityManager.createQuery("select s from RoleEntity s where s.name='NAME'", RoleEntity.class).getSingleResult()

        when:
        def persistedRole = roleRepository.findOneByIdAndClient_Id(role.id, client.id).get()

        then:
        persistedRole.id == role.id
        persistedRole.name == 'NAME'
        persistedRole.nameAr == 'NAME_AR'
        persistedRole.client.id == client.id
    }

    def "should find role by name and clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName('TEST')
        setupClient.setNameAr('TEST_AR')
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupRole = new RoleEntity()
        setupRole.setName('NAME')
        setupRole.setNameAr('NAME_AR')
        setupRole.setClient(client)
        setupRole.setCode('NAME')
        entityManager.persist(setupRole)
        def role = entityManager.createQuery("select s from RoleEntity s where s.name='NAME'", RoleEntity.class).getSingleResult()

        when:
        def persistedRole = roleRepository.findOneByNameIgnoreCaseAndClient_Id(role.name, client.id).get()

        then:
        persistedRole.id == role.id
        persistedRole.name == 'NAME'
        persistedRole.nameAr == 'NAME_AR'
        persistedRole.client.id == client.id
    }

    def "should find role by nameAr and clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName('TEST')
        setupClient.setNameAr('TEST_AR')
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupRole = new RoleEntity()
        setupRole.setName('NAME')
        setupRole.setNameAr('NAME_AR')
        setupRole.setClient(client)
        setupRole.setCode('NAME')
        entityManager.persist(setupRole)
        def role = entityManager.createQuery("select s from RoleEntity s where s.name='NAME'", RoleEntity.class).getSingleResult()

        when:
        def persistedRole = roleRepository.findOneByNameArIgnoreCaseAndClient_Id(role.nameAr, client.id).get()

        then:
        persistedRole.id == role.id
        persistedRole.name == 'NAME'
        persistedRole.nameAr == 'NAME_AR'
        persistedRole.client.id == client.id
    }

    def "should find all roles by search query and clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName("TEST1")
        setupClient.setNameAr("TEST_AR1")
        entityManager.persist(setupClient)
        def client1 = entityManager.createQuery("select c from ClientEntity c where c.name='TEST1'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName("TEST2")
        setupClient.setNameAr("TEST_AR2")
        entityManager.persist(setupClient)
        def client2 = entityManager.createQuery("select c from ClientEntity c where c.name='TEST2'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupRole = new RoleEntity()
        setupRole.setName('NAME1')
        setupRole.setNameAr('NAME_AR1')
        setupRole.setCode('CODE1')
        setupRole.setClient(client1)
        entityManager.persist(setupRole)
        setupRole = new RoleEntity()
        setupRole.setName('NAME2')
        setupRole.setNameAr('NAME_AR2')
        setupRole.setCode('CODE2')
        setupRole.setClient(client2)
        entityManager.persist(setupRole)
        def pageable = PageRequest.of(0, 10)

        when:
        def rolesPageFilteredByName = roleRepository.findAllBySearchQueryAndClient_Id(pageable, 'ME1', client1.id)
        def rolesPageFilteredByNameAr = roleRepository.findAllBySearchQueryAndClient_Id(pageable, 'AR', client2.id)

        then:
        rolesPageFilteredByName.totalElements == 1
        rolesPageFilteredByName.content[0].name == 'NAME1'
        rolesPageFilteredByName.content[0].client.id == client1.id
        rolesPageFilteredByNameAr.totalElements == 1
        rolesPageFilteredByNameAr.content[0].nameAr == 'NAME_AR2'
        rolesPageFilteredByNameAr.content[0].client.id == client2.id
    }

    def "should find all roles by clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName("TEST1")
        setupClient.setNameAr("TEST_AR1")
        entityManager.persist(setupClient)
        def client1 = entityManager.createQuery("select c from ClientEntity c where c.name='TEST1'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName("TEST2")
        setupClient.setNameAr("TEST_AR2")
        entityManager.persist(setupClient)
        def client2 = entityManager.createQuery("select c from ClientEntity c where c.name='TEST2'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupRole = new RoleEntity()
        setupRole.setName('NAME1')
        setupRole.setNameAr('NAME_AR1')
        setupRole.setCode('CODE1')
        setupRole.setClient(client1)
        entityManager.persist(setupRole)
        setupRole = new RoleEntity()
        setupRole.setName('NAME2')
        setupRole.setNameAr('NAME_AR2')
        setupRole.setCode('CODE2')
        setupRole.setClient(client2)
        entityManager.persist(setupRole)
        def pageable = PageRequest.of(0, 10)

        when:
        def rolesPage = roleRepository.findAllByClient_Id(pageable, client1.id)

        then:
        rolesPage.totalElements == 1
        rolesPage.content[0].name == 'NAME1'
        rolesPage.content[0].client.id == client1.id
    }
}
