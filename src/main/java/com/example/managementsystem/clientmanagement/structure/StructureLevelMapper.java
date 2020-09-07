package com.example.managementsystem.clientmanagement.structure;

import com.example.managementsystem.clientmanagement.client.ClientMapper;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelCreationDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelUpdatingDto;
import com.example.managementsystem.clientmanagement.user.UserMapper;
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
