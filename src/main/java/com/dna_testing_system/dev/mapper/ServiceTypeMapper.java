package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.request.ServiceTypeRequest;
import com.dna_testing_system.dev.dto.response.ServiceTypeResponse;
import com.dna_testing_system.dev.entity.ServiceType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {

    // Convert entity to response DTO
    ServiceTypeResponse toResponse(ServiceType serviceType);

    // Convert request DTO to entity
    ServiceType toEntity(ServiceTypeRequest request);

    // Convert list of entities to list of response DTOs
    List<ServiceTypeResponse> toResponse(List<ServiceType> serviceTypeList);

    // Update existing entity from request DTO
    void updateServiceType(ServiceTypeRequest request, @MappingTarget ServiceType serviceType);
}
