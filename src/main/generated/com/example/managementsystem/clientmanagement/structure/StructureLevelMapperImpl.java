package com.estore.authenticationauthorizationservice.structure;

import com.estore.authenticationauthorizationservice.client.ClientMapper;
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelCreationDto;
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto;
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelDto.StructureLevelDtoBuilder;
import com.estore.authenticationauthorizationservice.structure.dto.StructureLevelUpdatingDto;
import com.estore.authenticationauthorizationservice.user.UserEntity;
import com.estore.authenticationauthorizationservice.user.UserMapper;
import com.estore.authenticationauthorizationservice.user.dto.UserDto;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class StructureLevelMapperImpl implements StructureLevelMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public StructureLevelEntity toEntity(StructureLevelCreationDto structureLevelDto) {
        if ( structureLevelDto == null ) {
            return null;
        }

        StructureLevelEntity structureLevelEntity = new StructureLevelEntity();

        structureLevelEntity.setName( structureLevelDto.getName() );
        structureLevelEntity.setNameAr( structureLevelDto.getNameAr() );
        structureLevelEntity.setProperties( structureLevelDto.getProperties() );
        structureLevelEntity.setParent( toEntity( structureLevelDto.getParent() ) );
        structureLevelEntity.setChildren( toEntity( structureLevelDto.getChildren() ) );
        structureLevelEntity.setUsers( userMapper.toEntity( structureLevelDto.getUsers() ) );
        structureLevelEntity.setManager( userMapper.toEntity( structureLevelDto.getManager() ) );
        structureLevelEntity.setClient( clientMapper.toEntity( structureLevelDto.getClient() ) );

        return structureLevelEntity;
    }

    @Override
    public StructureLevelEntity toEntity(StructureLevelUpdatingDto structureLevelDto) {
        if ( structureLevelDto == null ) {
            return null;
        }

        StructureLevelEntity structureLevelEntity = new StructureLevelEntity();

        structureLevelEntity.setId( structureLevelDto.getId() );
        structureLevelEntity.setName( structureLevelDto.getName() );
        structureLevelEntity.setNameAr( structureLevelDto.getNameAr() );
        structureLevelEntity.setProperties( structureLevelDto.getProperties() );
        structureLevelEntity.setParent( toEntity( structureLevelDto.getParent() ) );
        structureLevelEntity.setChildren( toEntity( structureLevelDto.getChildren() ) );
        structureLevelEntity.setUsers( userMapper.toEntity( structureLevelDto.getUsers() ) );
        structureLevelEntity.setManager( userMapper.toEntity( structureLevelDto.getManager() ) );
        structureLevelEntity.setClient( clientMapper.toEntity( structureLevelDto.getClient() ) );

        return structureLevelEntity;
    }

    @Override
    public StructureLevelEntity toEntity(StructureLevelDto structureLevelDto) {
        if ( structureLevelDto == null ) {
            return null;
        }

        StructureLevelEntity structureLevelEntity = new StructureLevelEntity();

        structureLevelEntity.setId( structureLevelDto.getId() );
        structureLevelEntity.setName( structureLevelDto.getName() );
        structureLevelEntity.setNameAr( structureLevelDto.getNameAr() );
        structureLevelEntity.setProperties( structureLevelDto.getProperties() );
        structureLevelEntity.setParent( toEntity( structureLevelDto.getParent() ) );
        structureLevelEntity.setChildren( toEntity( structureLevelDto.getChildren() ) );
        structureLevelEntity.setUsers( userMapper.toEntity( structureLevelDto.getUsers() ) );
        structureLevelEntity.setManager( userMapper.toEntity( structureLevelDto.getManager() ) );
        structureLevelEntity.setCreatedBy( structureLevelDto.getCreatedBy() );
        structureLevelEntity.setCreatedDate( structureLevelDto.getCreatedDate() );

        return structureLevelEntity;
    }

    @Override
    public StructureLevelDto toStructureLevelDto(StructureLevelEntity structureLevelEntity) {
        if ( structureLevelEntity == null ) {
            return null;
        }

        StructureLevelDtoBuilder structureLevelDto = StructureLevelDto.builder();

        structureLevelDto.id( structureLevelEntity.getId() );
        structureLevelDto.name( structureLevelEntity.getName() );
        structureLevelDto.nameAr( structureLevelEntity.getNameAr() );
        structureLevelDto.properties( structureLevelEntity.getProperties() );
        structureLevelDto.parent( toStructureLevelDto( structureLevelEntity.getParent() ) );
        structureLevelDto.children( structureLevelEntitySetToStructureLevelDtoSet( structureLevelEntity.getChildren() ) );
        structureLevelDto.users( userEntitySetToUserDtoSet( structureLevelEntity.getUsers() ) );
        structureLevelDto.manager( userMapper.toUserDto( structureLevelEntity.getManager() ) );
        structureLevelDto.createdBy( structureLevelEntity.getCreatedBy() );
        structureLevelDto.createdDate( structureLevelEntity.getCreatedDate() );

        return structureLevelDto.build();
    }

    protected Set<StructureLevelDto> structureLevelEntitySetToStructureLevelDtoSet(Set<StructureLevelEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<StructureLevelDto> set1 = new HashSet<StructureLevelDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( StructureLevelEntity structureLevelEntity : set ) {
            set1.add( toStructureLevelDto( structureLevelEntity ) );
        }

        return set1;
    }

    protected Set<UserDto> userEntitySetToUserDtoSet(Set<UserEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<UserDto> set1 = new HashSet<UserDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserEntity userEntity : set ) {
            set1.add( userMapper.toUserDto( userEntity ) );
        }

        return set1;
    }
}
