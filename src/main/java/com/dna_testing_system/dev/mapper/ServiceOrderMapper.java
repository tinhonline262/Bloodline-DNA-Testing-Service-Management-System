package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.entity.ServiceOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceOrderMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "createdDate", source = "createdAt")
    @Mapping(target = "updatedDate", source = "updatedAt")
    ServiceOrderResponse toResponse(ServiceOrder order);
}