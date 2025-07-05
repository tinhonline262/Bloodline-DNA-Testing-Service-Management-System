package com.dna_testing_system.dev.dto.request;

import com.dna_testing_system.dev.enums.CollectionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceOrderRequestByCustomer {
    String username;
    Long idMedicalService;
    LocalDate appointmentDate;
    CollectionType collectionType;
    String collectionAddress;
}
