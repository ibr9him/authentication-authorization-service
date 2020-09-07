package com.example.managementsystem.clientmanagement.subscription;

import com.example.managementsystem.clientmanagement.bundle.BundleMapper;
import com.example.managementsystem.clientmanagement.client.ClientMapper;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionCreationDto;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionDto;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionUpdatingDto;
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
