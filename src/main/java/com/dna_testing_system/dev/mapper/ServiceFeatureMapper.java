package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.request.ServiceFeatureRequest;
import com.dna_testing_system.dev.dto.response.ServiceFeatureResponse;
import com.dna_testing_system.dev.entity.ServiceFeature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceFeatureMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "featureName", source = "featureName")
    @Mapping(target = "isAvailable", source = "isAvailable")
    ServiceFeatureResponse toResponse(ServiceFeature serviceFeature);

    List<ServiceFeatureResponse> toResponse(List<ServiceFeature> serviceFeatures);

    ServiceFeature toEntity(ServiceFeatureRequest request);
}
