package com.dna_testing_system.dev.dto.request;

import com.dna_testing_system.dev.enums.CollectionType;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;
import com.dna_testing_system.dev.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderRequest {
    private String customerId;
    private Long serviceId;
    private LocalDateTime appointmentDate;
    private CollectionType collectionType;
    private String collectionAddress;
    private ServiceOrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private List<OrderParticipantRequest> participants;
}