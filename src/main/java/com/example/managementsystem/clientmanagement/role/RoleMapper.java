package com.example.managementsystem.clientmanagement.role;

import com.example.managementsystem.clientmanagement.role.dto.RoleCreationDto;
import com.example.managementsystem.clientmanagement.role.dto.RoleDto;
import com.example.managementsystem.clientmanagement.role.dto.RoleUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper {

    RoleEntity toEntity(RoleCreationDto userDto);

    RoleEntity toEntity(RoleUpdatingDto userDto);

    RoleEntity toEntity(RoleDto userDto);

    RoleDto toRoleDto(RoleEntity user);
}
