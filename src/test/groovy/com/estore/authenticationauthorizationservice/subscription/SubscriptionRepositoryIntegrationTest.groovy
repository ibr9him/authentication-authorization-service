package com.estore.authenticationauthorizationservice.subscription


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager
import java.time.Instant

@ActiveProfiles("Test")
@DataJpaTest
@Import(com.estore.authenticationauthorizationservice.config.DataAccessConfiguration.class)
class SubscriptionRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    SubscriptionRepository subscriptionRepository

    def cleanup() {
        println('Cleaning up after a test!')
        subscriptionRepository.deleteAll()
    }

    def "should find all subscriptions for clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName("TEST1")
        setupClient.setNameAr("TEST_AR1")
        entityManager.persist(setupClient)
        setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName("TEST2")
        setupClient.setNameAr("TEST_AR2")
        entityManager.persist(setupClient)
        def setupBundle = new com.estore.authenticationauthorizationservice.bundle.BundleEntity()
        setupBundle.setName("TEST")
        setupBundle.setNameAr("TEST_AR")
        entityManager.persist(setupBundle)
        def client1 = entityManager.createQuery("select c from ClientEntity c where c.name='TEST1'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def client2 = entityManager.createQuery("select c from ClientEntity c where c.name='TEST2'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def bundle = entityManager.createQuery("select b from BundleEntity b where b.name='TEST'", com.estore.authenticationauthorizationservice.bundle.BundleEntity.class).getSingleResult()
        def s = new SubscriptionEntity()
        s.setStartDate(Instant.now())
        s.setEndDate(Instant.now())
        s.setBundle(bundle)
        s.setClient(client1)
        entityManager.persist(s)
        s = new SubscriptionEntity()
        s.setStartDate(Instant.now())
        s.setEndDate(Instant.now())
        s.setBundle(bundle)
        s.setClient(client2)
        entityManager.persist(s)
        def pageable = PageRequest.of(0, 10)

        when:
        def persistedSubscription = subscriptionRepository.findAllByClient_Id(pageable, client1.id)

        then:
        persistedSubscription.totalElements == 1
        persistedSubscription[0].client.name == 'TEST1'
        persistedSubscription[0].bundle.name == 'TEST'
    }

    def "should find subscription by id and clientId"() {
        given:
        def setupClient = new com.estore.authenticationauthorizationservice.client.ClientEntity()
        setupClient.setName('TEST')
        setupClient.setNameAr('TEST_AR')
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", com.estore.authenticationauthorizationservice.client.ClientEntity.class).getSingleResult()
        def setupSubscription = new SubscriptionEntity()
        setupSubscription.setStartDate(Instant.now())
        setupSubscription.setEndDate(Instant.now())
        setupSubscription.setClient(client)
        entityManager.persist(setupSubscription)
        def subscription = entityManager.createQuery("select s from SubscriptionEntity s where s.client.name ='TEST'", SubscriptionEntity.class).getSingleResult()

        when:
        def persistedSubscription = subscriptionRepository.findOneByIdAndClient_Id(subscription.id, client.id).get()

        then:
        persistedSubscription.id == subscription.id
        persistedSubscription.client.id == client.id
    }
}
