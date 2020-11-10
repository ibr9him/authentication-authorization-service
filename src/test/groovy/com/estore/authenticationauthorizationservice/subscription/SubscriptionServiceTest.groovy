package com.estore.authenticationauthorizationservice.subscription


import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser
import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionCreationDto
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionUpdatingDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class SubscriptionServiceTest extends Specification {

    private AuthenticationUser authenticationUser = new AuthenticationUser("User", "Password", Collections.emptyList())

    SubscriptionMapper mockSubscriptionMapper = Mock(SubscriptionMapper.class)
    com.estore.authenticationauthorizationservice.bundle.BundleMapper mockBundleMapper = Mock(com.estore.authenticationauthorizationservice.bundle.BundleMapper.class)
    SubscriptionRepository mockSubscriptionRepository = Mock(SubscriptionRepository.class)
    @Subject
    SubscriptionService subscriptionService = new SubscriptionService(mockSubscriptionRepository, mockSubscriptionMapper, mockBundleMapper)

    def setup() {
        authenticationUser.setClient(UUID.fromString("c1932a1c-0666-4832-90f4-06e2f3acbf1e"))
    }

    def "should save a new subscription"() {
        given:
        def time = Instant.now()
        def subscriptionDto = SubscriptionCreationDto.builder()
                .expired(false)
                .paused(false)
                .pausedSince(time)
                .startDate(time)
                .endDate(time)
                .bundle(BundleDto.builder().build())
                .client(ClientDto.builder().build())
                .build()

        when:
        subscriptionService.save(subscriptionDto, authenticationUser)

        then:
        1 * mockSubscriptionMapper.toEntity(_ as SubscriptionCreationDto) >> { args ->
            def s = args.get(0) as SubscriptionCreationDto
            assert s.client.id.toString() == 'c1932a1c-0666-4832-90f4-06e2f3acbf1e'
            new SubscriptionEntity()
        }
        1 * mockSubscriptionRepository.save(_ as SubscriptionEntity) >> { args -> args.get(0) }
        1 * mockSubscriptionMapper.toSubscriptionDto(_ as SubscriptionEntity) >> SubscriptionDto.builder().build()
    }

    def "should update an existing subscription"() {
        given:
        def id = UUID.randomUUID()
        def time = Instant.now()
        def subscriptionDto = SubscriptionUpdatingDto.builder()
                .id(id)
                .expired(false)
                .paused(false)
                .pausedSince(time)
                .startDate(time)
                .endDate(time)
                .bundle(BundleDto.builder().build())
                .build()

        when:
        def updatedSubscription = subscriptionService.update(subscriptionDto, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.of(new SubscriptionEntity())
        1 * mockSubscriptionRepository.save(_ as SubscriptionEntity) >> { args -> args.get(0) }
        1 * mockSubscriptionMapper.toSubscriptionDto(_ as SubscriptionEntity) >> { args ->
            def s = args.get(0) as SubscriptionEntity
            SubscriptionDto.builder().expired(s.expired).paused(s.paused).pausedSince(s.pausedSince).startDate(s.startDate).endDate(s.endDate).bundle(BundleDto.builder().build()).build()
        }
        1 * mockBundleMapper.toEntity(_ as BundleDto) >> new com.estore.authenticationauthorizationservice.bundle.BundleEntity()

        expect:
        !updatedSubscription.expired
        !updatedSubscription.paused
        updatedSubscription.startDate == time
        updatedSubscription.endDate == time
        updatedSubscription.bundle != BundleDto.builder().build()
    }

    def "should not update a non existing subscription and throw ResourceNotFoundException"() {
        given:
        def id = UUID.randomUUID()
        def subscriptionDto = SubscriptionUpdatingDto.builder().id(id).build()

        when:
        subscriptionService.update(subscriptionDto, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.empty()
        0 * mockSubscriptionRepository.save(_ as SubscriptionEntity)
        0 * mockSubscriptionMapper.toSubscriptionUpdatingDto(_ as SubscriptionEntity)
        0 * mockBundleMapper.toEntity(_ as BundleDto)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

    def "should update an existing subscription pausedSince field when paused"() {
        given:
        def id = UUID.randomUUID()
        def subscriptionDto = SubscriptionUpdatingDto.builder()
                .id(id)
                .expired(false)
                .paused(true)
                .pausedSince(null)
                .build()

        when:
        def updatedSubscription = subscriptionService.update(subscriptionDto, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.of(new SubscriptionEntity())
        1 * mockSubscriptionRepository.save(_ as SubscriptionEntity) >> { args -> args.get(0) }
        1 * mockSubscriptionMapper.toSubscriptionDto(_ as SubscriptionEntity) >> { args ->
            def s = args.get(0) as SubscriptionEntity
            SubscriptionDto.builder().expired(s.expired).paused(s.paused).pausedSince(s.pausedSince).startDate(s.startDate).endDate(s.endDate).bundle(BundleDto.builder().build()).build()
        }

        expect:
        !updatedSubscription.expired
        updatedSubscription.paused
        updatedSubscription.pausedSince != null
    }

    def "should get a page of subscriptions for client"() {
        given:
        def pageable = PageRequest.of(0, 10)

        when:
        def subscriptionsPage = subscriptionService.getAllForClient(pageable, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findAllByClient_Id(pageable, authenticationUser.getClient()) >> {
            def s1 = new SubscriptionEntity()
            def s2 = new SubscriptionEntity()
            new PageImpl<>(List.of(s1, s2))
        }
        2 * mockSubscriptionMapper.toSubscriptionDto(_ as SubscriptionEntity) >> SubscriptionDto.builder().build()

        expect:
        subscriptionsPage.size == 2
        subscriptionsPage.number == 0
        subscriptionsPage.totalElements == 2
    }

    def "should get a existing subscription by id"() {
        given:
        def id = UUID.randomUUID()

        when:
        subscriptionService.get(id, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.of(new SubscriptionEntity())
        1 * mockSubscriptionMapper.toSubscriptionDto(_ as SubscriptionEntity) >> SubscriptionDto.builder().build()
    }

    def "should not get a non existing subscription by id and throw ResourceNotFoundException"() {
        given:
        def id = UUID.randomUUID()

        when:
        subscriptionService.get(id, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(id, authenticationUser.client) >> Optional.empty()
        0 * mockSubscriptionMapper.toSubscriptionDto(_ as SubscriptionEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }

    def "should delete an existing subscription by id"() {
        given:
        def subscriptionId = UUID.randomUUID()

        when:
        subscriptionService.delete(subscriptionId, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(subscriptionId, authenticationUser.client) >> Optional.of(new SubscriptionEntity())
        1 * mockSubscriptionRepository.delete(_ as SubscriptionEntity)
    }

    def "should not delete a non existing subscription by id and throw ResourceNotFoundException"() {
        given:
        def subscriptionId = UUID.randomUUID()

        when:
        subscriptionService.delete(subscriptionId, authenticationUser)

        then:
        1 * mockSubscriptionRepository.findOneByIdAndClient_Id(subscriptionId, authenticationUser.client) >> Optional.empty()
        0 * mockSubscriptionRepository.delete(_ as SubscriptionEntity)
        thrown(com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException)
    }
}
