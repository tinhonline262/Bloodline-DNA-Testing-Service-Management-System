package com.dna_testing_system.dev.dto.request;

import com.dna_testing_system.dev.enums.DNATestType;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceTypeRequest {
    DNATestType typeName;
}
