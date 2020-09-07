package com.example.managementsystem.clientmanagement.client

import com.example.managementsystem.clientmanagement.client.ClientEntity
import com.example.managementsystem.clientmanagement.client.ClientRepository
import com.example.managementsystem.config.DataAccessConfiguration
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
@Import(DataAccessConfiguration.class)
class ClientRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    private ClientRepository clientRepository

    void cleanup() {
        println('Cleaning up after a test!')
        clientRepository.deleteAll()
    }

    def "should save a client to the database with auditing info"() {
        given:
        def client = new ClientEntity()
        client.name = "NAME"
        client.nameAr = "NAME_AR"

        when:
        def persistedClient = clientRepository.save(client)

        then:
        def retrievedClient = entityManager.find(ClientEntity, persistedClient.id)
        retrievedClient.id == persistedClient.id
        retrievedClient.createdBy != null
        retrievedClient.createdDate != null
        retrievedClient.lastModifiedBy != null
        retrievedClient.lastModifiedDate != null
    }

    def "should retrieve clients page filtered by search query"() {
        given:
        def client1 = new ClientEntity()
        client1.setName("NAME")
        client1.setNameAr("NAME_AR")
        def client2 = new ClientEntity()
        client2.setName("TEST")
        client2.setNameAr("TEST_AR")
        entityManager.persist(client1)
        entityManager.persist(client2)

        when:
        def clientsPage = clientRepository.findBySearchQuery(PageRequest.of(0, 2), "NAME")

        then:
        clientsPage.totalElements == 1
        clientsPage.content[0].name == 'NAME'
    }
}
