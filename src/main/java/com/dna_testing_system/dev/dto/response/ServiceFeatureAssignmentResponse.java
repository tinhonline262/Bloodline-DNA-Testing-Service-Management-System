package com.dna_testing_system.dev.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO dùng để trả về thông tin gán đặc điểm của dịch vụ y tế.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceFeatureAssignmentResponse {
    Long featureId;
    String featureName;
    Boolean isAvailable;
}