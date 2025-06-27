package com.dna_testing_system.dev.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderParticipantRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender; // Will be validated against Gender enum
}