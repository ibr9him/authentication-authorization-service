package com.estore.authenticationauthorizationservice.role;

import com.estore.authenticationauthorizationservice.role.dto.RoleCreationDto;
import com.estore.authenticationauthorizationservice.role.dto.RoleDto;
import com.estore.authenticationauthorizationservice.role.dto.RoleUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper {

    RoleEntity toEntity(RoleCreationDto userDto);

    RoleEntity toEntity(RoleUpdatingDto userDto);

    RoleEntity toEntity(RoleDto userDto);

    RoleDto toRoleDto(RoleEntity user);
}
