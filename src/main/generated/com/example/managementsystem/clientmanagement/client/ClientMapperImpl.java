package com.estore.authenticationauthorizationservice.client;

import com.estore.authenticationauthorizationservice.activity.ActivityMapper;
import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto.ClientDtoBuilder;
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto;
import com.estore.authenticationauthorizationservice.subscription.SubscriptionMapper;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Autowired
    private SubscriptionMapper subscriptionMapper;
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public ClientEntity toEntity(ClientDto clientDto) {
        if ( clientDto == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setId( clientDto.getId() );
        clientEntity.setName( clientDto.getName() );
        clientEntity.setNameAr( clientDto.getNameAr() );
        clientEntity.setContactInfo( clientDto.getContactInfo() );
        clientEntity.setProperties( clientDto.getProperties() );
        clientEntity.setEnabled( clientDto.isEnabled() );
        clientEntity.setActivity( activityMapper.toEntity( clientDto.getActivity() ) );
        clientEntity.setSubscriptions( subscriptionMapper.toEntity( clientDto.getSubscriptions() ) );
        clientEntity.setCreatedBy( clientDto.getCreatedBy() );
        clientEntity.setCreatedDate( clientDto.getCreatedDate() );

        return clientEntity;
    }

    @Override
    public ClientEntity toEntity(ClientCreationDto clientDto) {
        if ( clientDto == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setName( clientDto.getName() );
        clientEntity.setNameAr( clientDto.getNameAr() );
        clientEntity.setContactInfo( clientDto.getContactInfo() );
        clientEntity.setProperties( clientDto.getProperties() );
        clientEntity.setEnabled( clientDto.isEnabled() );
        clientEntity.setActivity( activityMapper.toEntity( clientDto.getActivity() ) );
        clientEntity.setSubscriptions( subscriptionMapper.toEntity( clientDto.getSubscriptions() ) );

        return clientEntity;
    }

    @Override
    public ClientEntity toEntity(ClientUpdatingDto clientDto) {
        if ( clientDto == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setId( clientDto.getId() );
        clientEntity.setName( clientDto.getName() );
        clientEntity.setNameAr( clientDto.getNameAr() );
        clientEntity.setContactInfo( clientDto.getContactInfo() );
        clientEntity.setProperties( clientDto.getProperties() );
        clientEntity.setEnabled( clientDto.isEnabled() );
        clientEntity.setActivity( activityMapper.toEntity( clientDto.getActivity() ) );
        clientEntity.setSubscriptions( subscriptionMapper.toEntity( clientDto.getSubscriptions() ) );

        return clientEntity;
    }

    @Override
    public ClientDto toClientDto(ClientEntity clientEntity) {
        if ( clientEntity == null ) {
            return null;
        }

        ClientDtoBuilder clientDto = ClientDto.builder();

        clientDto.id( clientEntity.getId() );
        clientDto.name( clientEntity.getName() );
        clientDto.nameAr( clientEntity.getNameAr() );
        clientDto.contactInfo( clientEntity.getContactInfo() );
        clientDto.properties( clientEntity.getProperties() );
        clientDto.enabled( clientEntity.isEnabled() );
        clientDto.subscriptions( subscriptionMapper.toDto( clientEntity.getSubscriptions() ) );
        clientDto.activity( activityMapper.toActivityDto( clientEntity.getActivity() ) );
        clientDto.createdBy( clientEntity.getCreatedBy() );
        clientDto.createdDate( clientEntity.getCreatedDate() );

        return clientDto.build();
    }
}
