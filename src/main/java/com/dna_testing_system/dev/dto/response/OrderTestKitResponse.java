package com.dna_testing_system.dev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTestKitResponse {
    Long id;
    String kitName;
    String kitType;
    String sampleType;
    Integer quantityOrdered;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
}
