package com.estore.authenticationauthorizationservice.bundle;

import com.estore.authenticationauthorizationservice.bundle.dto.BundleCreationDto;
import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto;
import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto.BundleDtoBuilder;
import com.estore.authenticationauthorizationservice.bundle.dto.BundleUpdatingDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class BundleMapperImpl implements BundleMapper {

    @Override
    public BundleEntity toEntity(BundleUpdatingDto bundleDto) {
        if ( bundleDto == null ) {
            return null;
        }

        BundleEntity bundleEntity = new BundleEntity();

        bundleEntity.setId( bundleDto.getId() );
        bundleEntity.setName( bundleDto.getName() );
        bundleEntity.setNameAr( bundleDto.getNameAr() );
        bundleEntity.setPeriod( bundleDto.getPeriod() );
        bundleEntity.setPrice( bundleDto.getPrice() );
        bundleEntity.setLimitedPeriod( bundleDto.isLimitedPeriod() );
        bundleEntity.setCurrency( bundleDto.getCurrency() );
        bundleEntity.setLimitedToNumberOfUsers( bundleDto.isLimitedToNumberOfUsers() );
        bundleEntity.setNumberOfUsersLimit( bundleDto.getNumberOfUsersLimit() );
        bundleEntity.setNumberOfUsers( bundleDto.getNumberOfUsers() );
        bundleEntity.setLimitedToNumberOfClients( bundleDto.isLimitedToNumberOfClients() );
        bundleEntity.setNumberOfClientsLimit( bundleDto.getNumberOfClientsLimit() );
        bundleEntity.setNumberOfClients( bundleDto.getNumberOfClients() );
        bundleEntity.setEnabled( bundleDto.isEnabled() );

        return bundleEntity;
    }

    @Override
    public BundleEntity toEntity(BundleCreationDto bundleDto) {
        if ( bundleDto == null ) {
            return null;
        }

        BundleEntity bundleEntity = new BundleEntity();

        bundleEntity.setName( bundleDto.getName() );
        bundleEntity.setNameAr( bundleDto.getNameAr() );
        bundleEntity.setPeriod( bundleDto.getPeriod() );
        bundleEntity.setPrice( bundleDto.getPrice() );
        bundleEntity.setLimitedPeriod( bundleDto.isLimitedPeriod() );
        bundleEntity.setCurrency( bundleDto.getCurrency() );
        bundleEntity.setLimitedToNumberOfUsers( bundleDto.isLimitedToNumberOfUsers() );
        bundleEntity.setNumberOfUsersLimit( bundleDto.getNumberOfUsersLimit() );
        bundleEntity.setNumberOfUsers( bundleDto.getNumberOfUsers() );
        bundleEntity.setLimitedToNumberOfClients( bundleDto.isLimitedToNumberOfClients() );
        bundleEntity.setNumberOfClientsLimit( bundleDto.getNumberOfClientsLimit() );
        bundleEntity.setNumberOfClients( bundleDto.getNumberOfClients() );
        bundleEntity.setEnabled( bundleDto.isEnabled() );

        return bundleEntity;
    }

    @Override
    public BundleEntity toEntity(BundleDto bundleDto) {
        if ( bundleDto == null ) {
            return null;
        }

        BundleEntity bundleEntity = new BundleEntity();

        bundleEntity.setId( bundleDto.getId() );
        bundleEntity.setName( bundleDto.getName() );
        bundleEntity.setNameAr( bundleDto.getNameAr() );
        bundleEntity.setPeriod( bundleDto.getPeriod() );
        bundleEntity.setPrice( bundleDto.getPrice() );
        bundleEntity.setLimitedPeriod( bundleDto.isLimitedPeriod() );
        bundleEntity.setCurrency( bundleDto.getCurrency() );
        bundleEntity.setLimitedToNumberOfUsers( bundleDto.isLimitedToNumberOfUsers() );
        bundleEntity.setNumberOfUsersLimit( bundleDto.getNumberOfUsersLimit() );
        bundleEntity.setNumberOfUsers( bundleDto.getNumberOfUsers() );
        bundleEntity.setLimitedToNumberOfClients( bundleDto.isLimitedToNumberOfClients() );
        bundleEntity.setNumberOfClientsLimit( bundleDto.getNumberOfClientsLimit() );
        bundleEntity.setNumberOfClients( bundleDto.getNumberOfClients() );
        bundleEntity.setEnabled( bundleDto.isEnabled() );

        return bundleEntity;
    }

    @Override
    public BundleDto toBundleDto(BundleEntity bundle) {
        if ( bundle == null ) {
            return null;
        }

        BundleDtoBuilder bundleDto = BundleDto.builder();

        bundleDto.id( bundle.getId() );
        bundleDto.name( bundle.getName() );
        bundleDto.nameAr( bundle.getNameAr() );
        bundleDto.enabled( bundle.isEnabled() );
        bundleDto.period( bundle.getPeriod() );
        bundleDto.limitedPeriod( bundle.isLimitedPeriod() );
        bundleDto.price( bundle.getPrice() );
        bundleDto.currency( bundle.getCurrency() );
        bundleDto.limitedToNumberOfUsers( bundle.isLimitedToNumberOfUsers() );
        bundleDto.numberOfUsersLimit( bundle.getNumberOfUsersLimit() );
        bundleDto.numberOfUsers( bundle.getNumberOfUsers() );
        bundleDto.limitedToNumberOfClients( bundle.isLimitedToNumberOfClients() );
        bundleDto.numberOfClientsLimit( bundle.getNumberOfClientsLimit() );
        bundleDto.numberOfClients( bundle.getNumberOfClients() );

        return bundleDto.build();
    }
}
