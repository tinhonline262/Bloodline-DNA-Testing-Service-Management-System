package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.CollectionType;
import com.dna_testing_system.dev.enums.PaymentStatus;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;
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
@Table(name = "tbl_service_orders")
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    User customer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    MedicalService service;

    @Column(name = "appointment_date")
    LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Size(max = 50)
    @NotNull
    @Column(name = "collection_type", nullable = false, length = 50)
    CollectionType collectionType;

    @Size(max = 500)
    @Column(name = "collection_address", length = 500)
    String collectionAddress;

    @Builder.Default
    @Size(max = 50)
    @NotNull
    @Column(name = "order_status", nullable = false, length = 50)
    ServiceOrderStatus orderStatus = ServiceOrderStatus.PENDING;

    @Builder.Default
    @Size(max = 50)
    @NotNull
    @Column(name = "payment_status", nullable = false, length = 50)
    PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Builder.Default
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal finalAmount = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "updated_by")
    String updatedBy;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<OrderKit> orderKits = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<OrderParticipant> orderParticipants = new HashSet<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    Set<SampleCollection> sampleCollections = new HashSet<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    Set<TestResult> testResults = new HashSet<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    Set<Notification> notifications = new HashSet<>();
}