package com.dna_testing_system.dev.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.NotNull;

/**
 * DTO dùng để nhận yêu cầu tạo gán đặc điểm cho dịch vụ y tế.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeatureAssignmentCreationRequest {
    /**
     * Tên đặc điểm, không được null.
     */
    @NotNull(message = "Feature name must not be null")
    private String featureName;

    /**
     * Trạng thái khả dụng của đặc điểm, không được null.
     */
    @NotNull(message = "Availability status must be specified")
    private Boolean isAvailable;
}