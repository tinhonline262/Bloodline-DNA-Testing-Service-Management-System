package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.request.ServiceOrderRequestByCustomer;
import com.dna_testing_system.dev.dto.response.ServiceOrderByCustomerResponse;
import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.service.OrderService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderServiceMapper {
    ServiceOrder toOrderService(ServiceOrderRequestByCustomer serviceOrderRequestByCustomer);
    ServiceOrderRequestByCustomer toServiceOrderRequestByCustomer(ServiceOrder orderService);
    void updateOrderServiceFromRequest(ServiceOrderRequestByCustomer serviceOrderRequestByCustomer
            ,@MappingTarget ServiceOrder orderService);
    @Mapping(target = "idServiceOrder", source = "orderService.id")
    @Mapping(target="finalAmount", source = "finalAmount")
    @Mapping(target = "orderStatus", source = "orderService.orderStatus")
    ServiceOrderByCustomerResponse toServiceOrderByCustomerResponse(ServiceOrder orderService);
    List<ServiceOrderByCustomerResponse> toServiceOrderByCustomerResponseList(List<ServiceOrder> orderServices);
}
