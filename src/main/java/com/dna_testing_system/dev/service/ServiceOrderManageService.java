package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.dto.request.ServiceOrderRequest;
import com.dna_testing_system.dev.dto.response.MedicalServiceResponse;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;

import java.util.List;

public interface ServiceOrderManageService {
    ServiceOrderResponse createOrder(ServiceOrderRequest request);
    ServiceOrderResponse getOrderById(Long id);
    List<ServiceOrderResponse> getOrdersByCustomer(String customerId);
    ServiceOrderResponse updateOrder(Long id, ServiceOrderRequest request);
    void cancelOrder(Long id);
    ServiceOrderResponse confirmOrder(Long id);

    // ✅ Method bị thiếu gây lỗi
    List<MedicalServiceResponse> getServiceCatalog();
}
