package com.example.managementsystem.clientmanagement.activity

import com.example.managementsystem.clientmanagement.activity.dto.ActivityCreationDto
import com.example.managementsystem.clientmanagement.activity.dto.ActivityDto
import com.example.managementsystem.clientmanagement.activity.dto.ActivityTagNameDto
import com.example.managementsystem.clientmanagement.activity.dto.ActivityUpdatingDto
import com.example.managementsystem.clientmanagement.activity.tagname.TagNameEntity
import com.example.managementsystem.clientmanagement.activity.tagname.TagNameRepository
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException
import com.example.managementsystem.util.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class ActivityServiceTest extends Specification {

    ActivityMapper mockActivityMapper = Mock(ActivityMapper.class)
    ActivityRepository mockActivityRepository = Mock(ActivityRepository.class)
    TagNameRepository mockTagNameRepository = Mock(TagNameRepository.class)
    @Subject
    ActivityService activityService = new ActivityService(mockActivityRepository, mockActivityMapper, mockTagNameRepository)

    void setup() {
    }

    def "should save a new activity"() {
        given:
        def activityDto = ActivityCreationDto.builder().name('name').nameAr('name_ar').build()

        when:
        activityService.save(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.empty()
        1 * mockActivityMapper.toEntity(_ as ActivityCreationDto) >> new ActivityEntity()
        1 * mockActivityRepository.save(_ as ActivityEntity) >> { args ->
            def a = args.get(0) as ActivityEntity
            a.id = UUID.randomUUID()
            a
        }
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity)
    }

    def "should not save a new activity if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def activityDto = ActivityCreationDto.builder().name('name').nameAr('name_ar').build()

        when:
        activityService.save(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> Optional.of(new ActivityEntity())
        0 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar')
        0 * mockActivityMapper.toEntity(_ as ActivityCreationDto)
        0 * mockActivityRepository.save(_ as ActivityEntity)
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not save a new activity if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def activityDto = ActivityCreationDto.builder().name('name').nameAr('name_ar').build()

        when:
        activityService.save(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.of(new ActivityEntity())
        0 * mockActivityMapper.toEntity(_ as ActivityCreationDto)
        0 * mockActivityRepository.save(_ as ActivityEntity)
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should update an existing activity"() {
        given:
        def id = UUID.randomUUID()
        def activityDto = ActivityUpdatingDto.builder()
                .id(id)
                .name('name')
                .nameAr('name_ar')
                .enabled(true)
                .tagNames(Set.of(ActivityTagNameDto.builder().key('KEY').build()))
                .build()

        when:
        def updatedActivity = activityService.update(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.empty()
        1 * mockActivityRepository.findById(id) >> Optional.of(new ActivityEntity())
        1 * mockActivityRepository.save(_ as ActivityEntity) >> { args -> args.get(0) }
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> { args ->
            def a = args.get(0) as ActivityEntity
            ActivityDto.builder()
                    .name(a.name)
                    .nameAr(a.nameAr)
                    .enabled(a.enabled)
                    .build()
        }
        1 * mockActivityMapper.toEntity(_ as Set<ActivityTagNameDto>)

        expect:
        updatedActivity.name == 'name'
        updatedActivity.nameAr == 'name_ar'
        updatedActivity.enabled
        updatedActivity.tagNames != null
    }

    def "should not update an existing activity if name and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        def activityDto = ActivityUpdatingDto.builder().id(id).name('name').nameAr('name_ar').build()

        when:
        activityService.update(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> {
            def entity = new ActivityEntity()
            entity.setId(UUID.randomUUID())
            Optional.of(entity)
        }
        0 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar')
        0 * mockActivityRepository.findById(id)
        0 * mockActivityRepository.save(_ as ActivityEntity)
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().id(UUID.randomUUID()).build()
        0 * mockActivityMapper.toEntity(_ as Set<ActivityTagNameDto>)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not update an existing activity if nameAr and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        def activityDto = ActivityUpdatingDto.builder().id(id).name('name').nameAr('name_ar').build()

        when:
        activityService.update(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.of(new ActivityEntity())
        0 * mockActivityRepository.findById(id)
        0 * mockActivityRepository.save(_ as ActivityEntity)
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().id(UUID.randomUUID()).build()
        0 * mockActivityMapper.toEntity(_ as Set<ActivityTagNameDto>)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not update a non existing activity and throw ResourceNotFoundException"() {
        given:
        def id = UUID.randomUUID()
        def activityDto = ActivityUpdatingDto.builder().id(id).name('name').nameAr('name_ar').build()

        when:
        activityService.update(activityDto)

        then:
        1 * mockActivityRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockActivityRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.empty()
        1 * mockActivityRepository.findById(id) >> Optional.empty()
        0 * mockActivityRepository.save(_ as ActivityEntity)
        0 * mockActivityMapper.toActivityDto(_ as ActivityEntity)
        0 * mockActivityMapper.toEntity(_ as Set<ActivityTagNameDto>)
        thrown(ResourceNotFoundException)
    }

    def "should get a page of activities"() {
        given:
        def searchQuery = ''
        def pageable = PageRequest.of(0, 10)

        when:
        def activitiesPage = activityService.getAll(pageable, searchQuery)

        then:
        1 * mockActivityRepository.findAll(pageable) >> {
            def activity1 = new ActivityEntity()
            def activity2 = new ActivityEntity()
            new PageImpl<>(List.of(activity1, activity2))
        }
        2 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().build()

        expect:
        activitiesPage.size == 2
        activitiesPage.number == 0
        activitiesPage.totalElements == 2
    }

    def "should get a page of activities filtered bu search query"() {
        given:
        def searchQuery = 'TEST'
        def pageable = PageRequest.of(0, 10)

        when:
        def activitiesPage = activityService.getAll(pageable, searchQuery)

        then:
        1 * mockActivityRepository.findAllBySearchQuery(searchQuery, pageable) >> {
            def activity1 = new ActivityEntity()
            def activity2 = new ActivityEntity()
            new PageImpl<>(List.of(activity1, activity2))
        }
        2 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().build()

        expect:
        activitiesPage.size == 2
        activitiesPage.number == 0
        activitiesPage.totalElements == 2
    }

    def "should get a existing activity by id"() {
        given:
        def activityId = UUID.randomUUID()

        when:
        activityService.get(activityId)

        then:
        1 * mockActivityRepository.findById(activityId) >> Optional.of(new ActivityEntity())
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> ActivityDto.builder().build()
    }

    def "should not get a non existing activity by id and throw ResourceNotFoundException"() {
        given:
        def activityId = UUID.randomUUID()

        when:
        activityService.get(activityId)

        then:
        1 * mockActivityRepository.findById(activityId) >> Optional.empty()
        0 * mockActivityMapper.toActivityDto(_ as ActivityEntity)
        thrown(ResourceNotFoundException)
    }

    def "should get all allowed for client assignment"() {
        when:
        def activities = activityService.getAllAllowedForClientAssignment()

        then:
        1 * mockActivityRepository.findAllByEnabledIsTrue() >> {
            def activity1 = new ActivityEntity()
            activity1.setId(UUID.randomUUID())
            def activity2 = new ActivityEntity()
            activity2.setId(UUID.randomUUID())
            Set.of(activity1, activity2)
        }
        2 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> { args -> ActivityDto.builder().id(args.get(0).id as UUID).build() }

        expect:
        activities.size() == 2
    }

    def "should get activity tag-names"() {
        given:
        def activityId = UUID.randomUUID()

        when:
        activityService.getActivityTagNames(activityId)

        then:
        1 * mockTagNameRepository.findAllByActivity_Id(activityId) >> Set.of(new TagNameEntity())
        1 * mockActivityMapper.toActivityTagNameDto(_ as TagNameEntity) >> ActivityTagNameDto.builder().build()
    }

    def "should delete an existing activity by id"() {
        given:
        def activityId = UUID.randomUUID()

        when:
        activityService.delete(activityId)

        then:
        1 * mockActivityRepository.findById(activityId) >> Optional.of(new ActivityEntity())
        1 * mockActivityRepository.delete(_ as ActivityEntity)
    }

    def "should not delete a non existing activity by id and throw ResourceNotFoundException"() {
        given:
        def activityId = UUID.randomUUID()

        when:
        activityService.delete(activityId)

        then:
        1 * mockActivityRepository.findById(activityId) >> Optional.empty()
        0 * mockActivityRepository.delete(_ as ActivityEntity)
        thrown(ResourceNotFoundException)
    }
}
