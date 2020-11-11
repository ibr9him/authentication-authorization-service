package com.estore.authenticationauthorizationservice.client;

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ClientMapper {

    ClientEntity toEntity(ClientDto clientDto);

    ClientEntity toEntity(ClientCreationDto clientDto);

    ClientEntity toEntity(ClientUpdatingDto clientDto);

    ClientDto toClientDto(ClientEntity clientEntity);
}
