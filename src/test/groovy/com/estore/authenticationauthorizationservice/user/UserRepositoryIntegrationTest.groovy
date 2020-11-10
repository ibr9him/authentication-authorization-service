package com.estore.authenticationauthorizationservice.user


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
class UserRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    UserRepository userRepository

    def cleanup() {
        println('Cleaning up after a test!')
        userRepository.deleteAll()
    }

    def "should save a user to the database with auditing info"() {
        given:
        def user = new UserEntity()
        user.setName('NAME')
        user.setNameAr('NAME_AR')
        user.setContactInfo('{}')
        user.setProperties('{}')
        user.setUsername('USERNAME')
        user.setPassword('PASSWORD')
        user.setEnabled(true)

        when:
        def persistedUser = userRepository.save(user)

        then:
        def retrievedUser = entityManager.find(UserEntity, persistedUser.id)
        retrievedUser.id == persistedUser.id
        retrievedUser.name == "NAME"
        retrievedUser.nameAr == "NAME_AR"
        retrievedUser.contactInfo == '{}'
        retrievedUser.properties == '{}'
        retrievedUser.username == 'USERNAME'
        retrievedUser.password == 'PASSWORD'
        retrievedUser.enabled
        retrievedUser.createdBy == 'Dummy User'
        retrievedUser.createdDate != null
        retrievedUser.lastModifiedBy == 'Dummy User'
        retrievedUser.lastModifiedDate != null
    }

    def "should find user by id and clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName('TEST')
        setupClient.setNameAr('TEST_AR')
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupUser = new UserEntity()
        setupUser.setName('NAME')
        setupUser.setNameAr('NAME_AR')
        setupUser.setClient(client)
        entityManager.persist(setupUser)
        def user = entityManager.createQuery("select s from UserEntity s where s.name='NAME'", UserEntity.class).getSingleResult()

        when:
        def persistedUser = userRepository.findOneByIdAndClient_Id(user.id, client.id).get()

        then:
        persistedUser.id == user.id
        persistedUser.name == 'NAME'
        persistedUser.nameAr == 'NAME_AR'
        persistedUser.client.id == client.id
    }

    def "should find user by username"() {
        given:
        def setupUser = new UserEntity()
        setupUser.setName('NAME1')
        setupUser.setNameAr('NAME_AR1')
        setupUser.setUsername('USERNAME1')
        entityManager.persist(setupUser)
        setupUser = new UserEntity()
        setupUser.setName("NAME2")
        setupUser.setNameAr("NAME_AR2")
        setupUser.setUsername('USERNAME2')
        entityManager.persist(setupUser)

        when:
        def persistedUser = userRepository.findOneByUsernameIgnoreCase('USERNAME1').get()

        then:
        persistedUser.name == 'NAME1'
        persistedUser.nameAr == 'NAME_AR1'
        persistedUser.username == 'USERNAME1'
    }

    def "should find all users by search query and clientId"() {
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
        def setupUser = new UserEntity()
        setupUser.setName('NAME1')
        setupUser.setNameAr('NAME_AR1')
        setupUser.setUsername('USERNAME1')
        setupUser.setClient(client1)
        entityManager.persist(setupUser)
        setupUser = new UserEntity()
        setupUser.setName('NAME2')
        setupUser.setNameAr('NAME_AR2')
        setupUser.setUsername('USERNAME2')
        setupUser.setClient(client2)
        entityManager.persist(setupUser)
        def pageable = PageRequest.of(0, 10)

        when:
        def usersPageFilteredByName = userRepository.findAllBySearchQueryAndClient_Id(pageable, 'ME1', client1.id)
        def usersPageFilteredByNameAr = userRepository.findAllBySearchQueryAndClient_Id(pageable, 'E_AR1', client2.id)
        def usersPageFilteredByUsername = userRepository.findAllBySearchQueryAndClient_Id(pageable, 'USERNAME', client2.id)

        then:
        usersPageFilteredByName.totalElements == 1
        usersPageFilteredByName.content[0].name == 'NAME1'
        usersPageFilteredByName.content[0].client.id == client1.id
        usersPageFilteredByNameAr.totalElements == 0
        usersPageFilteredByUsername.totalElements == 1
        usersPageFilteredByUsername.content[0].username == 'USERNAME2'
        usersPageFilteredByUsername.content[0].client.id == client2.id
    }

    def "should find all users by clientId"() {
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
        def setupUser = new UserEntity()
        setupUser.setName('NAME1')
        setupUser.setNameAr('NAME_AR1')
        setupUser.setUsername('USERNAME1')
        setupUser.setClient(client1)
        entityManager.persist(setupUser)
        setupUser = new UserEntity()
        setupUser.setName('NAME2')
        setupUser.setNameAr('NAME_AR2')
        setupUser.setUsername('USERNAME2')
        setupUser.setClient(client2)
        entityManager.persist(setupUser)
        def pageable = PageRequest.of(0, 10)

        when:
        def usersPage = userRepository.findAllByClient_Id(pageable, client1.id)

        then:
        usersPage.totalElements == 1
        usersPage.content[0].name == 'NAME1'
        usersPage.content[0].client.id == client1.id
    }
}
