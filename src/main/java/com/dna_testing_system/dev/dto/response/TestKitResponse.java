package com.dna_testing_system.dev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestKitResponse {
    Long idKit;
    String kitName;
    String kitType;
    String sampleType;
    BigDecimal basePrice;
    BigDecimal currentPrice;
    Integer quantity;
    String kitDescription;
    LocalDate expiryDate;
    String producedBy;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
