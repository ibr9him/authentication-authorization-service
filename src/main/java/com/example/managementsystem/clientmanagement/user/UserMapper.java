package com.example.managementsystem.clientmanagement.user;

import com.example.managementsystem.clientmanagement.client.ClientMapper;
import com.example.managementsystem.clientmanagement.role.RoleMapper;
import com.example.managementsystem.clientmanagement.structure.StructureLevelMapper;
import com.example.managementsystem.clientmanagement.user.dto.UserCreationDto;
import com.example.managementsystem.clientmanagement.user.dto.UserDto;
import com.example.managementsystem.clientmanagement.user.dto.UserUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ClientMapper.class, RoleMapper.class, StructureLevelMapper.class})
public
interface UserMapper {

    UserEntity toEntity(UserDto userDto);

    UserEntity toEntity(UserCreationDto userDto);

    UserEntity toEntity(UserUpdatingDto userDto);

    default Set<UserEntity> toEntity(Set<UserDto> userDtos) {
        return userDtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    UserDto toUserDto(UserEntity userEntity);
}
