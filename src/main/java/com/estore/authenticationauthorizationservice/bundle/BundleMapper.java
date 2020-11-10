package com.estore.authenticationauthorizationservice.bundle;

import com.estore.authenticationauthorizationservice.bundle.dto.BundleCreationDto;
import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto;
import com.estore.authenticationauthorizationservice.bundle.dto.BundleUpdatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BundleMapper {

    BundleEntity toEntity(BundleUpdatingDto bundleDto);

    BundleEntity toEntity(BundleCreationDto bundleDto);

    BundleEntity toEntity(BundleDto bundleDto);

    BundleDto toBundleDto(BundleEntity bundle);
}
