package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.DNATestType;
import com.dna_testing_system.dev.enums.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_medical_services")
public class MedicalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "service_name", nullable = false)
    String serviceName;

    @Enumerated(EnumType.STRING)
    @Size(max = 100)
    @NotNull
    @Column(name = "test_type", nullable = false, length = 100)
    DNATestType testType;

    @Enumerated(EnumType.STRING)
    @Size(max = 100)
    @NotNull
    @Column(name = "service_category", nullable = false, length = 100)
    ServiceType serviceCategory;

    @Builder.Default
    @Column(name = "participants", nullable = false)
    Integer participants = 1;

    @NotNull
    @Column(name = "execution_time_days", nullable = false)
    Integer executionTimeDays;

    @NotNull
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    BigDecimal basePrice;

    @NotNull
    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    BigDecimal currentPrice;

    @Builder.Default
    @Column(name = "is_available", nullable = false)
    Boolean isAvailable = true;

    @Size(max = 1000)
    @Column(name = "service_description", length = 1000)
    String serviceDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<ServiceOrder> serviceOrders = new HashSet<>();

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<CustomerFeedback> customerFeedbacks = new HashSet<>();
}