package com.example.managementsystem.clientmanagement.bundle;

import com.example.managementsystem.clientmanagement.bundle.dto.BundleCreationDto;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleDto;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BundleMapper {

    BundleEntity toEntity(BundleUpdatingDto bundleDto);

    BundleEntity toEntity(BundleCreationDto bundleDto);

    BundleEntity toEntity(BundleDto bundleDto);

    BundleDto toBundleDto(BundleEntity bundle);
}
