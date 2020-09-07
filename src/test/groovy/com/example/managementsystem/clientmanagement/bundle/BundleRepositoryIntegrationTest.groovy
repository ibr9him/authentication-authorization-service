package com.example.managementsystem.clientmanagement.bundle


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
class BundleRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    BundleRepository bundleRepository

    def cleanup() {
        println('Cleaning up after a test!')
        bundleRepository.deleteAll()
    }

    def "should save a bundle to the database with auditing info"() {
        given:
        def bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME')
        bundleEntity.setNameAr('NAME_AR')
        bundleEntity.setPeriod('3')
        bundleEntity.setLimitedPeriod(true)
        bundleEntity.setPrice('100.00')
        bundleEntity.setCurrency('SAR')
        bundleEntity.setLimitedToNumberOfUsers(true)
        bundleEntity.setNumberOfUsersLimit(5)
        bundleEntity.setNumberOfUsers(3)
        bundleEntity.setLimitedToNumberOfClients(true)
        bundleEntity.setNumberOfClientsLimit(5)
        bundleEntity.setNumberOfClients(2)
        bundleEntity.setEnabled(true)

        when:
        def persistedBundle = bundleRepository.save(bundleEntity)

        then:
        def retrievedBundle = entityManager.find(BundleEntity, persistedBundle.id)
        retrievedBundle.id == persistedBundle.id
        retrievedBundle.name == 'NAME'
        retrievedBundle.nameAr == 'NAME_AR'
        retrievedBundle.period == '3'
        retrievedBundle.limitedPeriod
        retrievedBundle.price == '100.00'
        retrievedBundle.currency == 'SAR'
        retrievedBundle.limitedToNumberOfUsers
        retrievedBundle.numberOfUsersLimit == 5
        retrievedBundle.numberOfUsers == 3
        retrievedBundle.limitedToNumberOfClients
        retrievedBundle.numberOfClientsLimit == 5
        retrievedBundle.numberOfClients == 2
        retrievedBundle.enabled
        retrievedBundle.createdBy == 'Dummy User'
        retrievedBundle.createdDate != null
        retrievedBundle.lastModifiedBy == 'Dummy User'
        retrievedBundle.lastModifiedDate != null
    }

    def "should find bundle by name"() {
        given:
        def bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME1')
        bundleEntity.setNameAr('NAME_AR1')
        entityManager.persist(bundleEntity)
        bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME2')
        bundleEntity.setNameAr('NAME_AR2')
        entityManager.persist(bundleEntity)

        when:
        def retrievedBundle = bundleRepository.findOneByNameIgnoreCase('name1').get()

        then:
        retrievedBundle.name == 'NAME1'
        retrievedBundle.nameAr == 'NAME_AR1'
    }

    def "should find bundle by nameAr"() {
        given:
        def bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME1')
        bundleEntity.setNameAr('NAME_AR1')
        entityManager.persist(bundleEntity)
        bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME2')
        bundleEntity.setNameAr('NAME_AR2')
        entityManager.persist(bundleEntity)

        when:
        def retrievedBundle = bundleRepository.findOneByNameArIgnoreCase('name_AR1').get()

        then:
        retrievedBundle.name == 'NAME1'
        retrievedBundle.nameAr == 'NAME_AR1'
    }

    def "should find all bundles by search query"() {
        given:
        def bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME1')
        bundleEntity.setNameAr('NAME_AR1')
        entityManager.persist(bundleEntity)
        bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME2')
        bundleEntity.setNameAr('NAME_AR2')
        entityManager.persist(bundleEntity)
        def pageable = PageRequest.of(0, 10)

        when:
        def retrievedBundlesPage = bundleRepository.findAllBySearchQuery('2', pageable)

        then:
        retrievedBundlesPage.totalElements == 1
        retrievedBundlesPage.content[0].name == 'NAME2'
        retrievedBundlesPage.content[0].nameAr == 'NAME_AR2'
    }

    def "should find all bundles by enabled is true"() {
        given:
        def bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME1')
        bundleEntity.setNameAr('NAME_AR1')
        bundleEntity.setEnabled(false)
        entityManager.persist(bundleEntity)
        bundleEntity = new BundleEntity()
        bundleEntity.setName('NAME2')
        bundleEntity.setNameAr('NAME_AR2')
        bundleEntity.setEnabled(true)
        entityManager.persist(bundleEntity)

        when:
        def retrievedBundles = bundleRepository.findAllByEnabledIsTrue()

        then:
        retrievedBundles.size() == 1
        retrievedBundles[0].name == 'NAME2'
        retrievedBundles[0].nameAr == 'NAME_AR2'
    }
}
