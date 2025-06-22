package com.dna_testing_system.dev.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestKitRequest {
    String kitName;
    String kitType;
    String sampleType;
    BigDecimal basePrice;
    BigDecimal currentPrice;
    Integer quantity;
    String kitDescription;
    LocalDate expiryDate;
    String producedBy;
}
