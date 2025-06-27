package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.entity.OrderParticipant;
import com.dna_testing_system.dev.repository.ServiceOrderRepository;
import com.dna_testing_system.dev.repository.UserRepository;
import com.dna_testing_system.dev.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final MedicalServiceRepository medicalServiceRepository;

    @Transactional
    public ServiceOrder createServiceOrder(ServiceOrder serviceOrder, List<OrderParticipant> participants) {
        validateServiceOrder(serviceOrder);
        User customer = userRepository.findById(serviceOrder.getCustomer().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + serviceOrder.getCustomer().getId()));
        MedicalService medicalService = medicalServiceRepository.findById(serviceOrder.getService().getId())
                .orElseThrow(() -> new EntityNotFoundException("MedicalService not found with id: " + serviceOrder.getService().getId()));

        serviceOrder.setCustomer(customer);
        serviceOrder.setService(medicalService);
        serviceOrder.setFinalAmount(medicalService.getPrice());
        serviceOrder.setCreatedBy(customer.getUsername());

        for (OrderParticipant participant : participants) {
            participant.setOrder(serviceOrder);
        }
        serviceOrder.setOrderParticipants(new java.util.HashSet<>(participants));

        return serviceOrderRepository.save(serviceOrder);
    }

    @Transactional(readOnly = true)
    public Optional<ServiceOrder> getServiceOrderById(Long id) {
        return serviceOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ServiceOrder> getAllServiceOrders() {
        return serviceOrderRepository.findAll();
    }

    @Transactional
    public ServiceOrder updateServiceOrder(Long id, ServiceOrder updatedOrder, List<OrderParticipant> participants) {
        ServiceOrder existingOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ServiceOrder not found with id: " + id));

        existingOrder.setAppointmentDate(updatedOrder.getAppointmentDate());
        existingOrder.setCollectionType(updatedOrder.getCollectionType());
        existingOrder.setCollectionAddress(updatedOrder.getCollectionAddress());
        existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
        existingOrder.setPaymentStatus(updatedOrder.getPaymentStatus());
        existingOrder.setUpdatedBy(updatedOrder.getUpdatedBy());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        existingOrder.getOrderParticipants().clear();
        for (OrderParticipant participant : participants) {
            participant.setOrder(existingOrder);
        }
        existingOrder.setOrderParticipants(new java.util.HashSet<>(participants));

        return serviceOrderRepository.save(existingOrder);
    }

    @Transactional
    public void deleteServiceOrder(Long id) {
        if (!serviceOrderRepository.existsById(id)) {
            throw new EntityNotFoundException("ServiceOrder not found with id: " + id);
        }
        serviceOrderRepository.deleteById(id);
    }

    private void validateServiceOrder(ServiceOrder serviceOrder) {
        if (serviceOrder.getAppointmentDate() != null && serviceOrder.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }
        if (serviceOrder.getCollectionType() == null) {
            throw new IllegalArgumentException("Collection type must be specified");
        }
    }
}
