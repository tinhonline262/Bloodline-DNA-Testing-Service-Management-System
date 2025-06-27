package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.ServiceCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id")
    ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_category", length = 100)
    ServiceCategory serviceCategory;

    @Column(name = "participants", nullable = false)
    Integer participants = 1;

    @Column(name = "execution_time_days")
    Integer executionTimeDays;

    @Column(name = "base_price", precision = 10, scale = 2)
    BigDecimal basePrice;

    @Column(name = "current_price", precision = 10, scale = 2)
    BigDecimal currentPrice;

    @Column(name = "is_available", nullable = false)
    Boolean isAvailable = true;

    @Column(name = "is_best_value", nullable = false)
    Boolean isBestValue = false;

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
    Set<ServiceOrder> serviceOrders = new HashSet<>();

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    Set<CustomerFeedback> customerFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    Set<ServiceFeature> serviceFeatures = new HashSet<>();
}