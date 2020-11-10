package com.estore.authenticationauthorizationservice.activity

import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto
import com.estore.authenticationauthorizationservice.activity.dto.ActivityTagNameDto
import com.estore.authenticationauthorizationservice.activity.dto.ActivityUpdatingDto
import com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class ActivityMapperTest extends Specification {

    @Subject
    ActivityMapper activityMapper = new ActivityMapperImpl()

    def "should convert from dto to entity"() {
        given:
        def id = UUID.randomUUID()
        def time = Instant.now()
        def dto = ActivityDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .enabled(true)
                .tagNames(Set.of(ActivityTagNameDto.builder().build()))
                .createdBy('NOW')
                .createdDate(time)
                .build()

        when:
        def entity = activityMapper.toEntity(dto)

        then:
        entity.id == id
        entity.name == 'NAME'
        entity.nameAr == 'NAME_AR'
        entity.enabled
        entity.tagNames != null
        entity.createdBy == 'NOW'
        entity.createdDate == time
    }

    def "should convert from creation dto to entity"() {
        given:
        def dto = ActivityDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .enabled(true)
                .tagNames(Set.of(ActivityTagNameDto.builder().build()))
                .build()

        when:
        def entity = activityMapper.toEntity(dto)

        then:
        entity.name == 'NAME'
        entity.nameAr == 'NAME_AR'
        entity.enabled
        entity.tagNames != null
    }

    def "should convert from updating dto to entity"() {
        given:
        def id = UUID.randomUUID()
        def dto = ActivityUpdatingDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .enabled(true)
                .tagNames(Set.of(ActivityTagNameDto.builder().build()))
                .build()

        when:
        def entity = activityMapper.toEntity(dto)

        then:
        entity.id == id
        entity.name == 'NAME'
        entity.nameAr == 'NAME_AR'
        entity.enabled
        entity.tagNames != null
    }

    def "should convert from tag-name dto to entity"() {
        given:
        def dto = ActivityTagNameDto.builder()
                .locale('AR')
                .message('NAME_AR')
                .key('NOW')
                .build()

        when:
        def entity = activityMapper.toEntity(dto)

        then:
        entity.locale == 'AR'
        entity.message == 'NAME_AR'
        entity.key == 'NOW'
    }

    def "should convert from base set dto to entity set"() {
        given:
        def dtos = Set.of(
                ActivityTagNameDto.builder().locale('AR').message('NAME_AR1').key('NOW1').build(),
                ActivityTagNameDto.builder().locale('AR').message('NAME_AR2').key('NOW2').build()
        )

        when:
        def entities = activityMapper.toEntity(dtos)

        then:
        entities.size() == 2
        entities[0].message == 'NAME_AR1'
        entities[0].key == 'NOW1'
    }

    def "should convert from entity to tag-name dto"() {
        given:
        def entity = new TagNameEntity()
        entity.setKey('KEY')
        entity.setLocale('LOCAL')
        entity.setMessage('MESSAGE')
        entity.setActivity(new ActivityEntity())

        when:
        def dto = activityMapper.toActivityTagNameDto(entity)

        then:
        dto.key == 'KEY'
        dto.locale == 'LOCAL'
        dto.message == 'MESSAGE'
    }

    def "should convert from entity to details dto"() {
        given:
        def id = UUID.randomUUID()
        def time = Instant.now()
        def entity = new ActivityEntity()
        entity.setId(id)
        entity.setName('NAME')
        entity.setNameAr('NAME_AR')
        entity.setEnabled(true)
        entity.setTagNames(Set.of(new TagNameEntity()))
        entity.setCreatedBy('TEST')
        entity.setCreatedDate(time)

        when:
        def dto = activityMapper.toActivityDto(entity)

        then:
        dto.id == id
        dto.name == 'NAME'
        dto.nameAr == 'NAME_AR'
        dto.enabled
        dto.tagNames != null
        dto.createdBy == 'TEST'
        dto.createdDate == time
    }

    def "should convert from entity to base dto"() {
        given:
        def id = UUID.randomUUID()
        def entity = new ActivityEntity()
        entity.setId(id)
        entity.setName('NAME')
        entity.setNameAr('NAME_AR')

        when:
        def dto = activityMapper.toActivityDto(entity)

        then:
        dto.id == id
        dto.name == 'NAME'
        dto.nameAr == 'NAME_AR'
    }
}
