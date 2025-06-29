package com.dna_testing_system.dev.dto.request;

import com.dna_testing_system.dev.enums.CollectionType;
import com.dna_testing_system.dev.enums.PaymentMethod;
import com.dna_testing_system.dev.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceOrderRequest {
    String username;
    String idMedicalService;
    LocalDate appointmentDate;
    CollectionType collectionType;
    String collectionAddress;
    String createdBy;
    String idKit;
    Integer quantityKit;
    List<ParticipantRequest> participants;
    Long promotionId;
    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
}
