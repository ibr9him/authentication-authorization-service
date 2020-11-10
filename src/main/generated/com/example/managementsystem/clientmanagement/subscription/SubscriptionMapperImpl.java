package com.estore.authenticationauthorizationservice.subscription;

import com.estore.authenticationauthorizationservice.bundle.BundleMapper;
import com.estore.authenticationauthorizationservice.client.ClientMapper;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionCreationDto;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto.SubscriptionDtoBuilder;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionUpdatingDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private BundleMapper bundleMapper;

    @Override
    public SubscriptionEntity toEntity(SubscriptionCreationDto subscriptionDto) {
        if ( subscriptionDto == null ) {
            return null;
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        subscriptionEntity.setStartDate( subscriptionDto.getStartDate() );
        subscriptionEntity.setEndDate( subscriptionDto.getEndDate() );
        subscriptionEntity.setExpired( subscriptionDto.isExpired() );
        subscriptionEntity.setPaused( subscriptionDto.isPaused() );
        subscriptionEntity.setPausedSince( subscriptionDto.getPausedSince() );
        subscriptionEntity.setBundle( bundleMapper.toEntity( subscriptionDto.getBundle() ) );
        subscriptionEntity.setClient( clientMapper.toEntity( subscriptionDto.getClient() ) );

        return subscriptionEntity;
    }

    @Override
    public SubscriptionEntity toEntity(SubscriptionUpdatingDto subscriptionDto) {
        if ( subscriptionDto == null ) {
            return null;
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        subscriptionEntity.setId( subscriptionDto.getId() );
        subscriptionEntity.setStartDate( subscriptionDto.getStartDate() );
        subscriptionEntity.setEndDate( subscriptionDto.getEndDate() );
        subscriptionEntity.setExpired( subscriptionDto.isExpired() );
        subscriptionEntity.setPaused( subscriptionDto.isPaused() );
        subscriptionEntity.setPausedSince( subscriptionDto.getPausedSince() );
        subscriptionEntity.setBundle( bundleMapper.toEntity( subscriptionDto.getBundle() ) );

        return subscriptionEntity;
    }

    @Override
    public SubscriptionEntity toEntity(SubscriptionDto subscriptionDto) {
        if ( subscriptionDto == null ) {
            return null;
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        subscriptionEntity.setId( subscriptionDto.getId() );
        subscriptionEntity.setStartDate( subscriptionDto.getStartDate() );
        subscriptionEntity.setEndDate( subscriptionDto.getEndDate() );
        subscriptionEntity.setExpired( subscriptionDto.isExpired() );
        subscriptionEntity.setPaused( subscriptionDto.isPaused() );
        subscriptionEntity.setPausedSince( subscriptionDto.getPausedSince() );
        subscriptionEntity.setBundle( bundleMapper.toEntity( subscriptionDto.getBundle() ) );
        subscriptionEntity.setCreatedBy( subscriptionDto.getCreatedBy() );
        subscriptionEntity.setCreatedDate( subscriptionDto.getCreatedDate() );

        return subscriptionEntity;
    }

    @Override
    public SubscriptionDto toSubscriptionDto(SubscriptionEntity subscription) {
        if ( subscription == null ) {
            return null;
        }

        SubscriptionDtoBuilder subscriptionDto = SubscriptionDto.builder();

        subscriptionDto.id( subscription.getId() );
        subscriptionDto.expired( subscription.isExpired() );
        subscriptionDto.paused( subscription.isPaused() );
        subscriptionDto.pausedSince( subscription.getPausedSince() );
        subscriptionDto.startDate( subscription.getStartDate() );
        subscriptionDto.endDate( subscription.getEndDate() );
        subscriptionDto.bundle( bundleMapper.toBundleDto( subscription.getBundle() ) );
        subscriptionDto.createdBy( subscription.getCreatedBy() );
        subscriptionDto.createdDate( subscription.getCreatedDate() );

        return subscriptionDto.build();
    }
}
