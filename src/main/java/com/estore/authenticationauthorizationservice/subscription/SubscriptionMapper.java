package com.estore.authenticationauthorizationservice.subscription;

import com.estore.authenticationauthorizationservice.bundle.BundleMapper;
import com.estore.authenticationauthorizationservice.client.ClientMapper;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionCreationDto;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ClientMapper.class, BundleMapper.class})
public interface SubscriptionMapper {

    SubscriptionEntity toEntity(SubscriptionCreationDto subscriptionDto);

    SubscriptionEntity toEntity(SubscriptionUpdatingDto subscriptionDto);

    SubscriptionEntity toEntity(SubscriptionDto subscriptionDto);

    SubscriptionDto toSubscriptionDto(SubscriptionEntity subscription);

    default Set<SubscriptionEntity> toEntity(Set<SubscriptionDto> subscriptionDtos) {
        return subscriptionDtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    default Set<SubscriptionDto> toDto(Set<SubscriptionEntity> subscriptionEntities) {
        return subscriptionEntities.stream().map(this::toSubscriptionDto).collect(Collectors.toSet());
    }
}
