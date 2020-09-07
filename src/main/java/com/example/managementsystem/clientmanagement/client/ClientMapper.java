package com.example.managementsystem.clientmanagement.client;

import com.example.managementsystem.clientmanagement.activity.ActivityMapper;
import com.example.managementsystem.clientmanagement.client.dto.ClientCreationDto;
import com.example.managementsystem.clientmanagement.client.dto.ClientDto;
import com.example.managementsystem.clientmanagement.client.dto.ClientUpdatingDto;
import com.example.managementsystem.clientmanagement.subscription.SubscriptionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {SubscriptionMapper.class, ActivityMapper.class})
public interface ClientMapper {

    ClientEntity toEntity(ClientDto clientDto);

    ClientEntity toEntity(ClientCreationDto clientDto);

    ClientEntity toEntity(ClientUpdatingDto clientDto);

    ClientDto toClientDto(ClientEntity clientEntity);
}
