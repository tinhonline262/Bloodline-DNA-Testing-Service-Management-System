package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.dto.request.StaffAvailableRequest;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.dto.response.StaffAvailableResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;

import java.util.List;

public interface OrderTaskManagementService {
    List<StaffAvailableResponse> getStaffAvailable();
    void sampleCollectionTaskAssignment(Long orderId, StaffAvailableRequest staff);
    List<ServiceOrderResponse> getServiceOrders();
}
