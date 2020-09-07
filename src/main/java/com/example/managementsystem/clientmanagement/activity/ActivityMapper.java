package com.example.managementsystem.clientmanagement.activity;

import com.example.managementsystem.clientmanagement.activity.dto.ActivityCreationDto;
import com.example.managementsystem.clientmanagement.activity.dto.ActivityDto;
import com.example.managementsystem.clientmanagement.activity.dto.ActivityTagNameDto;
import com.example.managementsystem.clientmanagement.activity.dto.ActivityUpdatingDto;
import com.example.managementsystem.clientmanagement.activity.tagname.TagNameEntity;
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
