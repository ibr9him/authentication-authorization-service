package com.estore.authenticationauthorizationservice.user;

import com.estore.authenticationauthorizationservice.client.ClientMapper;
import com.estore.authenticationauthorizationservice.role.RoleMapper;
import com.estore.authenticationauthorizationservice.structure.StructureLevelMapper;
import com.estore.authenticationauthorizationservice.user.dto.UserCreationDto;
import com.estore.authenticationauthorizationservice.user.dto.UserDto;
import com.estore.authenticationauthorizationservice.user.dto.UserDto.UserDtoBuilder;
import com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private StructureLevelMapper structureLevelMapper;

    @Override
    public UserEntity toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( userDto.getId() );
        userEntity.setName( userDto.getName() );
        userEntity.setNameAr( userDto.getNameAr() );
        userEntity.setContactInfo( userDto.getContactInfo() );
        userEntity.setProperties( userDto.getProperties() );
        userEntity.setUsername( userDto.getUsername() );
        userEntity.setEnabled( userDto.isEnabled() );
        userEntity.setStructureLevel( structureLevelMapper.toEntity( userDto.getStructureLevel() ) );
        userEntity.setRole( roleMapper.toEntity( userDto.getRole() ) );
        userEntity.setCreatedBy( userDto.getCreatedBy() );
        userEntity.setCreatedDate( userDto.getCreatedDate() );

        return userEntity;
    }

    @Override
    public UserEntity toEntity(UserCreationDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setName( userDto.getName() );
        userEntity.setNameAr( userDto.getNameAr() );
        userEntity.setContactInfo( userDto.getContactInfo() );
        userEntity.setProperties( userDto.getProperties() );
        userEntity.setUsername( userDto.getUsername() );
        userEntity.setPassword( userDto.getPassword() );
        userEntity.setEnabled( userDto.isEnabled() );
        userEntity.setClient( clientMapper.toEntity( userDto.getClient() ) );

        return userEntity;
    }

    @Override
    public UserEntity toEntity(UserUpdatingDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( userDto.getId() );
        userEntity.setName( userDto.getName() );
        userEntity.setNameAr( userDto.getNameAr() );
        userEntity.setContactInfo( userDto.getContactInfo() );
        userEntity.setProperties( userDto.getProperties() );
        userEntity.setUsername( userDto.getUsername() );
        userEntity.setPassword( userDto.getPassword() );
        userEntity.setEnabled( userDto.isEnabled() );

        return userEntity;
    }

    @Override
    public UserDto toUserDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( userEntity.getId() );
        userDto.name( userEntity.getName() );
        userDto.nameAr( userEntity.getNameAr() );
        userDto.contactInfo( userEntity.getContactInfo() );
        userDto.properties( userEntity.getProperties() );
        userDto.username( userEntity.getUsername() );
        userDto.enabled( userEntity.isEnabled() );
        userDto.role( roleMapper.toRoleDto( userEntity.getRole() ) );
        userDto.structureLevel( structureLevelMapper.toStructureLevelDto( userEntity.getStructureLevel() ) );
        userDto.createdBy( userEntity.getCreatedBy() );
        userDto.createdDate( userEntity.getCreatedDate() );

        return userDto.build();
    }
}
