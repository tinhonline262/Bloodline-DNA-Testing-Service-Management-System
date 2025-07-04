package com.dna_testing_system.dev.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestKitResponse {
    Long id;
    String kitName;
    String kitType;
    String sampleType;
    BigDecimal basePrice;
    BigDecimal currentPrice;
    Integer quantityInStock;
    String kitDescription;
    LocalDate expiryDate;
    String producedBy;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
