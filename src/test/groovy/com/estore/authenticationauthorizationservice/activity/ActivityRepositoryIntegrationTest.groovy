package com.estore.authenticationauthorizationservice.activity


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
class ActivityRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    ActivityRepository activityRepository
    @Subject
    @Autowired
    com.estore.authenticationauthorizationservice.activity.tagname.TagNameRepository tagNameRepository

    def cleanup() {
        println('Cleaning up after a test!')
        activityRepository.deleteAll()
    }

    def "should save a activity to the database with auditing info"() {
        given:
        def tagNameEntity = new com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity()
        tagNameEntity.setKey("KEY")
        tagNameEntity.setMessage("MESSAGE")
        tagNameEntity.setLocale("LOCALE")
        def activityEntity = new ActivityEntity()
        activityEntity.setName('NAME')
        activityEntity.setNameAr('NAME_AR')
        activityEntity.setEnabled(true)
        activityEntity.setTagNames(Set.of(tagNameEntity))

        when:
        def persistedActivity = activityRepository.save(activityEntity)

        then:
        def retrievedActivity = entityManager.find(ActivityEntity, persistedActivity.id)
        retrievedActivity.id == persistedActivity.id
        retrievedActivity.name == 'NAME'
        retrievedActivity.nameAr == 'NAME_AR'
        retrievedActivity.enabled
        retrievedActivity.tagNames[0].key == 'KEY'
        retrievedActivity.tagNames[0].message == 'MESSAGE'
        retrievedActivity.tagNames[0].locale == 'LOCALE'
        retrievedActivity.createdBy == 'Dummy User'
        retrievedActivity.createdDate != null
        retrievedActivity.lastModifiedBy == 'Dummy User'
        retrievedActivity.lastModifiedDate != null
    }

    def "should find activity by name"() {
        given:
        def activityEntity = new ActivityEntity()
        activityEntity.setName('NAME1')
        activityEntity.setNameAr('NAME_AR1')
        entityManager.persist(activityEntity)
        activityEntity = new ActivityEntity()
        activityEntity.setName('NAME2')
        activityEntity.setNameAr('NAME_AR2')
        entityManager.persist(activityEntity)

        when:
        def retrievedActivity = activityRepository.findOneByNameIgnoreCase('name1').get()

        then:
        retrievedActivity.name == 'NAME1'
        retrievedActivity.nameAr == 'NAME_AR1'
    }

    def "should find activity by nameAr"() {
        given:
        def activityEntity = new ActivityEntity()
        activityEntity.setName('NAME1')
        activityEntity.setNameAr('NAME_AR1')
        entityManager.persist(activityEntity)
        activityEntity = new ActivityEntity()
        activityEntity.setName('NAME2')
        activityEntity.setNameAr('NAME_AR2')
        entityManager.persist(activityEntity)

        when:
        def retrievedActivity = activityRepository.findOneByNameArIgnoreCase('name_AR1').get()

        then:
        retrievedActivity.name == 'NAME1'
        retrievedActivity.nameAr == 'NAME_AR1'
    }

    def "should find all activities by search query"() {
        given:
        def activityEntity = new ActivityEntity()
        activityEntity.setName('NAME1')
        activityEntity.setNameAr('NAME_AR1')
        entityManager.persist(activityEntity)
        activityEntity = new ActivityEntity()
        activityEntity.setName('NAME2')
        activityEntity.setNameAr('NAME_AR2')
        entityManager.persist(activityEntity)
        def pageable = PageRequest.of(0, 10)

        when:
        def retrievedActivitiesPage = activityRepository.findAllBySearchQuery('2', pageable)

        then:
        retrievedActivitiesPage.totalElements == 1
        retrievedActivitiesPage.content[0].name == 'NAME2'
        retrievedActivitiesPage.content[0].nameAr == 'NAME_AR2'
    }

    def "should find all activities by enabled is true"() {
        given:
        def activityEntity = new ActivityEntity()
        activityEntity.setName('NAME1')
        activityEntity.setNameAr('NAME_AR1')
        activityEntity.setEnabled(false)
        entityManager.persist(activityEntity)
        activityEntity = new ActivityEntity()
        activityEntity.setName('NAME2')
        activityEntity.setNameAr('NAME_AR2')
        activityEntity.setEnabled(true)
        entityManager.persist(activityEntity)

        when:
        def retrievedActivities = activityRepository.findAllByEnabledIsTrue()

        then:
        retrievedActivities.size() == 1
        retrievedActivities[0].name == 'NAME2'
        retrievedActivities[0].nameAr == 'NAME_AR2'
    }

    def "should find all tag-names by activity id"() {
        given:
        def activityEntity = new ActivityEntity()
        activityEntity.setName('NAME')
        activityEntity.setNameAr('NAME_AR')
        activityEntity.setEnabled(false)
        entityManager.persist(activityEntity)
        def setupActivity = entityManager.createQuery("select c from ActivityEntity c where c.name='NAME'", ActivityEntity.class).getSingleResult()
        def tagNameEntity1 = new com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity()
        tagNameEntity1.setKey("KEY1")
        tagNameEntity1.setMessage("MESSAGE")
        tagNameEntity1.setLocale("LOCALE")
        tagNameEntity1.setActivity(setupActivity)
        entityManager.persist(tagNameEntity1)
        def tagNameEntity2 = new com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity()
        tagNameEntity2.setKey("KEY2")
        tagNameEntity2.setMessage("MESSAGE")
        tagNameEntity2.setLocale("LOCALE")
        tagNameEntity2.setActivity(setupActivity)
        entityManager.persist(tagNameEntity2)

        when:
        def tagNames = tagNameRepository.findAllByActivity_Id(setupActivity.id)

        then:
        tagNames.size() == 2
        tagNames[0].key == 'KEY1'
        tagNames[0].message == 'MESSAGE'
        tagNames[0].locale == 'LOCALE'
    }
}
