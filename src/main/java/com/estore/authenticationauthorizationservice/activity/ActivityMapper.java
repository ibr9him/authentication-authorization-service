package com.estore.authenticationauthorizationservice.activity;

import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityTagNameDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityUpdatingDto;
import com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityCreationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ActivityMapper {

    ActivityEntity toEntity(ActivityCreationDto activityDto);

    ActivityEntity toEntity(ActivityUpdatingDto activityDto);

    ActivityEntity toEntity(ActivityDto activityDto);

    TagNameEntity toEntity(ActivityTagNameDto activityDto);

    ActivityDto toActivityDto(ActivityEntity activity);

    ActivityTagNameDto toActivityTagNameDto(TagNameEntity activity);

    default Set<TagNameEntity> toEntity(Set<ActivityTagNameDto> activityTagNameDtos) {
        return activityTagNameDtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
