package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.ServiceOrderRequest;
import com.dna_testing_system.dev.dto.response.MedicalServiceResponse;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.service.MedicalServiceManageService;
import com.dna_testing_system.dev.service.ServiceOrderManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderManageService serviceOrderManageService;
    private final MedicalServiceManageService medicalServiceManageService;

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> createOrder(@RequestBody ServiceOrderRequest request) {
        return ResponseEntity.ok(serviceOrderManageService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponse> getOrderById(@PathVariable Long id) {
        ServiceOrderResponse response = serviceOrderManageService.getOrderById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ServiceOrderResponse>> getOrdersByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(serviceOrderManageService.getOrdersByCustomer(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponse> updateOrder(@PathVariable Long id, @RequestBody ServiceOrderRequest request) {
        return ResponseEntity.ok(serviceOrderManageService.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        serviceOrderManageService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<MedicalServiceResponse>> getServiceCatalog() {
        return ResponseEntity.ok(serviceOrderManageService.getServiceCatalog());
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<MedicalServiceResponse> getServiceDetails(@PathVariable Long serviceId) {
        MedicalServiceResponse response = medicalServiceManageService.getServiceById(serviceId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ServiceOrderResponse> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOrderManageService.confirmOrder(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<ServiceOrderResponse> getOrderSummary(@PathVariable Long id) {
        ServiceOrderResponse response = serviceOrderManageService.getOrderById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
