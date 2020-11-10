package com.estore.authenticationauthorizationservice.client


import com.estore.authenticationauthorizationservice.activity.ActivityEntity
import com.estore.authenticationauthorizationservice.activity.ActivityMapper
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.subscription.SubscriptionEntity
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class ClientMapperTest extends Specification {

    com.estore.authenticationauthorizationservice.subscription.SubscriptionMapper mockSubscriptionMapper = Mock(com.estore.authenticationauthorizationservice.subscription.SubscriptionMapper.class)
    ActivityMapper mockActivityMapper = Mock(ActivityMapper.class)
    @Subject
    ClientMapper clientMapper = new ClientMapperImpl(subscriptionMapper: mockSubscriptionMapper, activityMapper: mockActivityMapper)

    def "should convert from entity to dto"() {
        given:
        def id = UUID.randomUUID()
        def createdDate = Instant.now()
        def clientEntity = new ClientEntity()
        clientEntity.id = id
        clientEntity.name = 'NAME'
        clientEntity.nameAr = 'NAME_AR'
        clientEntity.contactInfo = '{}'
        clientEntity.properties = '{}'
        clientEntity.subscriptions = Set.of(new SubscriptionEntity())
        clientEntity.activity = new ActivityEntity()
        clientEntity.createdDate = createdDate
        clientEntity.enabled = true

        when:
        def clientDto = clientMapper.toClientDto(clientEntity)

        then:
        1 * mockSubscriptionMapper.toDto(_ as Set<SubscriptionEntity>) >> Set.of(new SubscriptionDto())
        1 * mockActivityMapper.toActivityDto(_ as ActivityEntity) >> com.estore.authenticationauthorizationservice.activity.dto.ActivityDto.builder().build()

        expect:
        assert clientDto instanceof ClientDto
        clientDto.id == id
        clientDto.name == 'NAME'
        clientDto.nameAr == 'NAME_AR'
        clientDto.contactInfo == '{}'
        clientDto.properties == '{}'
        clientDto.subscriptions != null
        clientDto.activity != null
        clientDto.createdDate == createdDate
        clientDto.enabled
    }

    def "should convert from creation dto to entity"() {
        given:
        def clientDto = com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .subscriptions(Set.of(SubscriptionDto.builder().build()))
                .activity(com.estore.authenticationauthorizationservice.activity.dto.ActivityDto.builder().build())
                .build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        1 * mockSubscriptionMapper.toEntity(_ as Set<SubscriptionDto>) >> Set.of(new SubscriptionEntity())
        1 * mockActivityMapper.toEntity(_ as com.estore.authenticationauthorizationservice.activity.dto.ActivityDto) >> new ActivityEntity()

        expect:
        assert clientEntity instanceof ClientEntity
        clientEntity.name == 'NAME'
        clientEntity.nameAr == 'NAME_AR'
        clientEntity.contactInfo == '{}'
        clientEntity.properties == '{}'
        clientEntity.enabled
        clientEntity.subscriptions != null
        clientEntity.activity != null
    }

    def "should convert from updating dto to entity"() {
        def id = UUID.randomUUID()
        given:
        def clientDto = com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .subscriptions(Set.of(new SubscriptionDto()))
                .activity(com.estore.authenticationauthorizationservice.activity.dto.ActivityDto.builder().build())
                .build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        1 * mockSubscriptionMapper.toEntity(_ as Set<SubscriptionDto>) >> Set.of(new SubscriptionEntity())
        1 * mockActivityMapper.toEntity(_ as com.estore.authenticationauthorizationservice.activity.dto.ActivityDto) >> new ActivityEntity()

        expect:
        assert clientEntity instanceof ClientEntity
        clientEntity.id == id
        clientEntity.name == 'NAME'
        clientEntity.nameAr == 'NAME_AR'
        clientEntity.contactInfo == '{}'
        clientEntity.properties == '{}'
        clientEntity.enabled
        clientEntity.subscriptions != null
        clientEntity.activity != null
    }

    def "should convert from details dto to entity"() {
        given:
        def createdDate = Instant.now()
        def id = UUID.randomUUID()
        def clientDto = ClientDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .subscriptions(Set.of(new SubscriptionDto()))
                .activity(com.estore.authenticationauthorizationservice.activity.dto.ActivityDto.builder().build())
                .createdDate(createdDate)
                .build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        1 * mockSubscriptionMapper.toEntity(_ as Set<SubscriptionDto>) >> Set.of(new SubscriptionEntity())
        1 * mockActivityMapper.toEntity(_ as com.estore.authenticationauthorizationservice.activity.dto.ActivityDto) >> new ActivityEntity()

        expect:
        assert clientEntity instanceof ClientEntity
        clientEntity.id == id
        clientEntity.name == 'NAME'
        clientEntity.nameAr == 'NAME_AR'
        clientEntity.contactInfo == '{}'
        clientEntity.properties == '{}'
        clientEntity.subscriptions != null
        clientEntity.activity != null
        clientEntity.createdDate == createdDate
        clientEntity.enabled
    }
}
