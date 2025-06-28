package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ServiceOrderMapper {

    @Mapping(target = "customerName", expression = "java(getFullName(order.getCustomer()))")
    @Mapping(source = "customer.username", target = "username")
    @Mapping(source = "customer.userProfile.profileImageUrl", target = "profileImageUrl")
    @Mapping(source = "customer.userProfile.email", target = "email")
    @Mapping(source = "customer.userProfile.phoneNumber", target = "phoneNumber")
    @Mapping(source = "service.serviceName", target = "serviceName")
    @Mapping(source = "service.serviceType.typeName", target = "serviceType")
    @Mapping(source = "service.serviceCategory.description", target = "serviceCategory")
    @Mapping(source = "service.participants", target = "participants")
    @Mapping(source = "service.executionTimeDays", target = "executionTimeDays")
    @Mapping(source = "orderStatus", target = "orderStatus")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "orderKits", target = "orderKits")
    @Mapping(source = "orderParticipants", target = "orderParticipants")
    @Mapping(source = "sampleCollections", target = "sampleCollections")
    @Mapping(source = "service.")
    ServiceOrderResponse toDto(ServiceOrder order);

    default String getFullName(User user) {
        if (user != null && user.getUserProfile() != null) {
            return user.getUserProfile().getFirstName() + " " + user.getUserProfile().getLastName();
        }
        return null;
    }
}
