package com.estore.authenticationauthorizationservice.subscription


import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto
import com.estore.authenticationauthorizationservice.client.ClientEntity
import com.estore.authenticationauthorizationservice.client.ClientMapper
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionCreationDto
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionUpdatingDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class SubscriptionMapperTest extends Specification {

    ClientMapper mockClientMapper = Mock(ClientMapper.class)
    com.estore.authenticationauthorizationservice.bundle.BundleMapper mockBundleMapper = Mock(com.estore.authenticationauthorizationservice.bundle.BundleMapper.class)
    @Subject
    SubscriptionMapper subscriptionMapper = new SubscriptionMapperImpl(clientMapper: mockClientMapper, bundleMapper: mockBundleMapper)

    def "should convert from creation dto to entity"() {
        given:
        def time = Instant.now()
        def subscriptionDto = SubscriptionCreationDto.builder()
                .expired(true)
                .paused(true)
                .pausedSince(time)
                .startDate(time)
                .endDate(time)
                .bundle(BundleDto.builder().build())
                .client(ClientDto.builder().build())
                .build()

        when:
        def subscriptionEntity = subscriptionMapper.toEntity(subscriptionDto)

        then:
        1 * mockClientMapper.toEntity(_ as ClientDto) >> new ClientEntity()
        1 * mockBundleMapper.toEntity(_ as BundleDto) >> new com.estore.authenticationauthorizationservice.bundle.BundleEntity()

        expect:
        assert subscriptionEntity instanceof SubscriptionEntity
        subscriptionEntity.expired
        subscriptionEntity.paused
        subscriptionEntity.pausedSince == time
        subscriptionEntity.startDate == time
        subscriptionEntity.endDate == time
        subscriptionEntity.bundle != null
        subscriptionEntity.client != null
    }

    def "should convert from updating dto to entity"() {
        given:
        def id = UUID.randomUUID()
        def time = Instant.now()
        def subscriptionDto = SubscriptionUpdatingDto.builder()
                .id(id)
                .expired(true)
                .paused(true)
                .pausedSince(time)
                .startDate(time)
                .endDate(time)
                .bundle(BundleDto.builder().build())
                .build()

        when:
        def subscriptionEntity = subscriptionMapper.toEntity(subscriptionDto)

        then:
        1 * mockBundleMapper.toEntity(_ as BundleDto) >> new com.estore.authenticationauthorizationservice.bundle.BundleEntity()

        expect:
        assert subscriptionEntity instanceof SubscriptionEntity
        subscriptionEntity.id == id
        subscriptionEntity.expired
        subscriptionEntity.paused
        subscriptionEntity.pausedSince == time
        subscriptionEntity.startDate == time
        subscriptionEntity.endDate == time
        subscriptionEntity.bundle != null
    }

    def "should convert from entity to dto"() {
        given:
        def id = UUID.randomUUID()
        def time = Instant.now()
        def subscriptionEntity = new SubscriptionEntity()
        subscriptionEntity.setId(id)
        subscriptionEntity.setExpired(true)
        subscriptionEntity.setPaused(true)
        subscriptionEntity.setPausedSince(time)
        subscriptionEntity.setStartDate(time)
        subscriptionEntity.setEndDate(time)
        subscriptionEntity.setBundle(new com.estore.authenticationauthorizationservice.bundle.BundleEntity())
        subscriptionEntity.setCreatedBy('TEST')
        subscriptionEntity.setCreatedDate(time)
        subscriptionEntity.setClient(new ClientEntity())

        when:
        def subscriptionDto = subscriptionMapper.toSubscriptionDto(subscriptionEntity)

        then:
        1 * mockBundleMapper.toBundleDto(_ as com.estore.authenticationauthorizationservice.bundle.BundleEntity) >> BundleDto.builder().build()

        expect:
        assert subscriptionDto instanceof SubscriptionDto
        subscriptionDto.id == id
        subscriptionDto.expired
        subscriptionDto.paused
        subscriptionDto.pausedSince == time
        subscriptionDto.startDate == time
        subscriptionDto.endDate == time
        subscriptionDto.bundle != null
        subscriptionDto.createdBy == 'TEST'
        subscriptionDto.createdDate == time
    }

    def "should convert from entity set to dto set"() {
        given:
        def s1 = new SubscriptionEntity()
        s1.setId(UUID.randomUUID())
        s1.setClient(new ClientEntity())
        s1.setBundle(new com.estore.authenticationauthorizationservice.bundle.BundleEntity())
        def s2 = new SubscriptionEntity()
        s2.setId(UUID.randomUUID())
        s2.setClient(new ClientEntity())
        s2.setBundle(new com.estore.authenticationauthorizationservice.bundle.BundleEntity())
        def subscriptionEntitySet = Set.of(s1, s2)

        when:
        def subscriptionDtoSet = subscriptionMapper.toDto(subscriptionEntitySet)

        then:
        subscriptionDtoSet.size() == 2
        subscriptionDtoSet[0] != null
        subscriptionDtoSet[1] != null
    }
}