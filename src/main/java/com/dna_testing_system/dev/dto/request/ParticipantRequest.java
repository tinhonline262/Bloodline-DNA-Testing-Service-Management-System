package com.dna_testing_system.dev.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantRequest {
    String firstName;
    String lastName;
    String gender;
    LocalDate birthDate;
}
