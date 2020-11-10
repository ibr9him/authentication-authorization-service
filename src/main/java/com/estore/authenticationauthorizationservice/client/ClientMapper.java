package com.estore.authenticationauthorizationservice.client;

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto;
import com.estore.authenticationauthorizationservice.subscription.SubscriptionMapper;
import com.estore.authenticationauthorizationservice.activity.ActivityMapper;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {SubscriptionMapper.class, ActivityMapper.class})
public interface ClientMapper {

    ClientEntity toEntity(ClientDto clientDto);

    ClientEntity toEntity(ClientCreationDto clientDto);

    ClientEntity toEntity(ClientUpdatingDto clientDto);

    ClientDto toClientDto(ClientEntity clientEntity);
}
