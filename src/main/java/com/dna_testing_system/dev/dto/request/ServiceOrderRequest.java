package com.dna_testing_system.dev.dto.request;

import com.dna_testing_system.dev.enums.CollectionType;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;
import com.dna_testing_system.dev.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceOrderRequest {
    String customerId;
    String username;
    Long serviceId;
    String idMedicalService;
    LocalDateTime appointmentDate;
    CollectionType collectionType;
    String collectionAddress;
    String createdBy;
    String idKit;
    Integer quantityKit;
    ServiceOrderStatus orderStatus;
    PaymentStatus paymentStatus;
    List<OrderParticipantRequest> participants;
}
