package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.dto.request.ServiceOrderRequestByCustomer;
import com.dna_testing_system.dev.dto.response.ServiceOrderByCustomerResponse;

import java.util.List;

public interface OrderService {
    ServiceOrderByCustomerResponse createOrder(ServiceOrderRequestByCustomer serviceOrderRequestByCustomer);
    void cancelOrder(Long orderId);
    void updateOrder(Long orderId, ServiceOrderRequestByCustomer serviceOrderRequestByCustomer);
    List<ServiceOrderByCustomerResponse> getAllOrdersByCustomerId(String customerName);
    ServiceOrderByCustomerResponse getOrderById(Long orderId);
    void acceptOrder(Long orderId);
}
