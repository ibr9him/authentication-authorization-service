package com.example.managementsystem.clientmanagement.subscription;

import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.bundle.BundleMapper;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionCreationDto;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionDto;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionUpdatingDto;
import com.example.managementsystem.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final BundleMapper bundleMapper;

    @Transactional
    public SubscriptionDto save(SubscriptionCreationDto subscriptionDto, AuthenticationUser user) {
        log.info("Saving subscription....");
        subscriptionDto.getClient().setId(user.getClient());
        SubscriptionEntity subscription = subscriptionRepository.save(subscriptionMapper.toEntity(subscriptionDto));
        log.info("Saving subscription done. Subscription id: {}", subscription.getId());
        return subscriptionMapper.toSubscriptionDto(subscription);
    }

    @Transactional
    public SubscriptionDto update(SubscriptionUpdatingDto subscriptionDto, AuthenticationUser user) {
        log.info("Updating subscription paused status....");
        return subscriptionRepository.findOneByIdAndClient_Id(subscriptionDto.getId(), user.getClient())
                .map((existingSubscription) -> {
                    existingSubscription.setStartDate(subscriptionDto.getStartDate());
                    existingSubscription.setEndDate(subscriptionDto.getEndDate());
                    existingSubscription.setBundle(bundleMapper.toEntity(subscriptionDto.getBundle()));

                    //TODO fix the logic for paused subscriptions when they are un-paused
                    existingSubscription.setPaused(subscriptionDto.isPaused());
                    if (subscriptionDto.isPaused() && !existingSubscription.isExpired()) {
                        existingSubscription.setPausedSince(Instant.now());
                    }

                    existingSubscription = subscriptionRepository.save(existingSubscription);
                    log.info("Updating subscription done.");
                    return existingSubscription;
                })
                .map(subscriptionMapper::toSubscriptionDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionDto> getAllForClient(Pageable pageable, AuthenticationUser user) {
        log.info("Getting roles page: {} ....", pageable);
        Page<SubscriptionDto> subscriptionDtoPage = subscriptionRepository.findAllByClient_Id(pageable, user.getClient()).map(subscriptionMapper::toSubscriptionDto);
        log.info("Getting all roles done found: {}.", subscriptionDtoPage.getTotalElements());
        return subscriptionDtoPage;
    }

    @Transactional(readOnly = true)
    public SubscriptionDto get(UUID id, AuthenticationUser user) {
        log.info("Getting subscription by id: {}....", id);

        return subscriptionRepository.findOneByIdAndClient_Id(id, user.getClient())
                .map(subscriptionEntity -> {
                    log.info("Getting subscription by id found: {}.", subscriptionEntity);
                    return subscriptionEntity;
                })
                .map(subscriptionMapper::toSubscriptionDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void delete(UUID subscriptionId, AuthenticationUser user) {
        log.info("Deleting subscription with id: {} ....", subscriptionId);
        subscriptionRepository.findOneByIdAndClient_Id(subscriptionId, user.getClient())
                .map(subscriptionEntity -> {
                    subscriptionRepository.delete(subscriptionEntity);
                    log.info("Deleting subscription: {} done.", subscriptionEntity);
                    return subscriptionEntity;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
