package com.estore.authenticationauthorizationservice.user;

import com.estore.authenticationauthorizationservice.client.ClientMapper;
import com.estore.authenticationauthorizationservice.role.RoleMapper;
import com.estore.authenticationauthorizationservice.user.dto.UserCreationDto;
import com.estore.authenticationauthorizationservice.user.dto.UserDto;
import com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ClientMapper.class, RoleMapper.class})
public interface UserMapper {

    UserEntity toEntity(UserDto userDto);

    UserEntity toEntity(UserCreationDto userDto);

    UserEntity toEntity(UserUpdatingDto userDto);

    default Set<UserEntity> toEntity(Set<UserDto> userDtos) {
        return userDtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }

    UserDto toUserDto(UserEntity userEntity);
}
