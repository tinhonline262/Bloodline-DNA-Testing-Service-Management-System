package com.dna_testing_system.dev.dto.request;

import com.dna_testing_system.dev.enums.ServiceCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO dùng để nhận yêu cầu tạo dịch vụ y tế từ client.
 * Bao gồm thông tin cơ bản như tên dịch vụ, loại, giá cả, và các đặc điểm liên quan.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalServiceRequest {

    /**
     * Tên của dịch vụ, không được để trống và tối đa 255 ký tự.
     */
    @NotBlank(message = "Service name cannot be blank")
    @Size(max = 255, message = "Service name must be less than 255 characters")
    String serviceName;

    /**
     * Loại dịch vụ, không được null.
     */
    @NotNull(message = "Service category must not be null")
    ServiceCategory serviceCategory;

    /**
     * ID của loại dịch vụ, không được null.
     */
    @NotNull(message = "Service type ID must not be null")
    Long serviceTypeId;

    /**
     * Số lượng người tham gia, không được null và ít nhất là 1.
     */
    @NotNull(message = "Number of participants must not be null")
    @Min(value = 1, message = "At least one participant is required")
    Integer participants;

    /**
     * Thời gian thực hiện dịch vụ (tính bằng ngày), không được null và ít nhất là 1 ngày.
     */
    @NotNull(message = "Execution time must not be null")
    @Min(value = 1, message = "Execution time must be at least 1 day")
    Integer executionTimeDays;

    /**
     * Giá cơ bản của dịch vụ, không được null và phải lớn hơn 0.
     */
    @NotNull(message = "Base price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than zero")
    BigDecimal basePrice;

    /**
     * Giá hiện tại của dịch vụ, không được null và phải lớn hơn 0, không được nhỏ hơn basePrice.
     */
    @NotNull(message = "Current price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Current price must be greater than zero")
    @DecimalMin(value = "#{T(java.math.BigDecimal).ZERO.compareTo(target.basePrice) < 0 ? target.basePrice.toString() : '0.01'}",
            message = "Current price must not be less than base price")
    BigDecimal currentPrice;

    /**
     * Trạng thái khả dụng của dịch vụ, không được null.
     */
    @NotNull(message = "Availability status must be specified")
    Boolean isAvailable;

    /**
     * Mô tả dịch vụ, không được để trống và tối đa 1000 ký tự.
     */
    @NotBlank(message = "Service description cannot be blank")
    @Size(max = 1000, message = "Service description must be less than 1000 characters")
    String serviceDescription;

    /**
     * Danh sách các đặc điểm dịch vụ, không được rỗng và mỗi phần tử phải hợp lệ.
     */
    @NotEmpty(message = "Feature assignments cannot be empty")
    @Valid
    List<FeatureAssignmentCreationRequest> featureAssignments;

    /**
     * Kiểm tra tính duy nhất của feature assignments (tránh trùng lặp).
     * @return true nếu không có trùng lặp, false nếu có.
     */
    public boolean isFeatureAssignmentsUnique() {
        return featureAssignments.stream()
                .map(FeatureAssignmentCreationRequest::getFeatureName)
                .distinct()
                .count() == featureAssignments.size();
    }
}