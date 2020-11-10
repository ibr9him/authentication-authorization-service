package com.estore.authenticationauthorizationservice.activity;

import com.estore.authenticationauthorizationservice.activity.dto.ActivityCreationDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto.ActivityDtoBuilder;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityTagNameDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityTagNameDto.ActivityTagNameDtoBuilder;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityUpdatingDto;
import com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class ActivityMapperImpl implements ActivityMapper {

    @Override
    public ActivityEntity toEntity(ActivityCreationDto activityDto) {
        if ( activityDto == null ) {
            return null;
        }

        ActivityEntity activityEntity = new ActivityEntity();

        activityEntity.setName( activityDto.getName() );
        activityEntity.setNameAr( activityDto.getNameAr() );
        activityEntity.setEnabled( activityDto.isEnabled() );
        activityEntity.setTagNames( toEntity( activityDto.getTagNames() ) );

        return activityEntity;
    }

    @Override
    public ActivityEntity toEntity(ActivityUpdatingDto activityDto) {
        if ( activityDto == null ) {
            return null;
        }

        ActivityEntity activityEntity = new ActivityEntity();

        activityEntity.setId( activityDto.getId() );
        activityEntity.setName( activityDto.getName() );
        activityEntity.setNameAr( activityDto.getNameAr() );
        activityEntity.setEnabled( activityDto.isEnabled() );
        activityEntity.setTagNames( toEntity( activityDto.getTagNames() ) );

        return activityEntity;
    }

    @Override
    public ActivityEntity toEntity(ActivityDto activityDto) {
        if ( activityDto == null ) {
            return null;
        }

        ActivityEntity activityEntity = new ActivityEntity();

        activityEntity.setId( activityDto.getId() );
        activityEntity.setName( activityDto.getName() );
        activityEntity.setNameAr( activityDto.getNameAr() );
        activityEntity.setEnabled( activityDto.isEnabled() );
        activityEntity.setTagNames( toEntity( activityDto.getTagNames() ) );
        activityEntity.setCreatedBy( activityDto.getCreatedBy() );
        activityEntity.setCreatedDate( activityDto.getCreatedDate() );

        return activityEntity;
    }

    @Override
    public TagNameEntity toEntity(ActivityTagNameDto activityDto) {
        if ( activityDto == null ) {
            return null;
        }

        TagNameEntity tagNameEntity = new TagNameEntity();

        tagNameEntity.setKey( activityDto.getKey() );
        tagNameEntity.setLocale( activityDto.getLocale() );
        tagNameEntity.setMessage( activityDto.getMessage() );

        return tagNameEntity;
    }

    @Override
    public ActivityDto toActivityDto(ActivityEntity activity) {
        if ( activity == null ) {
            return null;
        }

        ActivityDtoBuilder activityDto = ActivityDto.builder();

        activityDto.id( activity.getId() );
        activityDto.name( activity.getName() );
        activityDto.nameAr( activity.getNameAr() );
        activityDto.enabled( activity.isEnabled() );
        activityDto.tagNames( tagNameEntitySetToActivityTagNameDtoSet( activity.getTagNames() ) );
        activityDto.createdBy( activity.getCreatedBy() );
        activityDto.createdDate( activity.getCreatedDate() );

        return activityDto.build();
    }

    @Override
    public ActivityTagNameDto toActivityTagNameDto(TagNameEntity activity) {
        if ( activity == null ) {
            return null;
        }

        ActivityTagNameDtoBuilder activityTagNameDto = ActivityTagNameDto.builder();

        activityTagNameDto.key( activity.getKey() );
        activityTagNameDto.locale( activity.getLocale() );
        activityTagNameDto.message( activity.getMessage() );

        return activityTagNameDto.build();
    }

    protected Set<ActivityTagNameDto> tagNameEntitySetToActivityTagNameDtoSet(Set<TagNameEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ActivityTagNameDto> set1 = new HashSet<ActivityTagNameDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( TagNameEntity tagNameEntity : set ) {
            set1.add( toActivityTagNameDto( tagNameEntity ) );
        }

        return set1;
    }
}
