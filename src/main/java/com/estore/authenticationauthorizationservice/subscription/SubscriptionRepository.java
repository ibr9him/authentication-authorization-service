package com.estore.authenticationauthorizationservice.subscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

    Page<SubscriptionEntity> findAllByClient_Id(Pageable pageable, UUID clientId);

    Optional<SubscriptionEntity> findOneByIdAndClient_Id(UUID subscriptionId, UUID clientId);
}
