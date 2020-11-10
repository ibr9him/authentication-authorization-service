package com.estore.authenticationauthorizationservice.structure;

import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelCreationDto;
import com.estore.authenticationauthorizationservice.client.ClientMapper;
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto;
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelUpdatingDto;
import com.estore.authenticationauthorizationservice.user.UserMapper;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ClientMapper.class})
public interface StructureLevelMapper {

    StructureLevelEntity toEntity(StructureLevelCreationDto structureLevelDto);

    StructureLevelEntity toEntity(StructureLevelUpdatingDto structureLevelDto);

    StructureLevelEntity toEntity(StructureLevelDto structureLevelDto);

    default Set<StructureLevelEntity> toEntity(Set<StructureLevelDto> structureLevelDtos) {
        return structureLevelDtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    StructureLevelDto toStructureLevelDto(StructureLevelEntity structureLevelEntity);
}
