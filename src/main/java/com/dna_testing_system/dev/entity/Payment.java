package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.PaymentMethod;
import com.dna_testing_system.dev.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    ServiceOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    Promotion promotion;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Size(max = 50)
    @NotNull
    @Column(name = "payment_method", nullable = false, length = 50)
    PaymentMethod paymentMethod = PaymentMethod.CASH;

    @NotNull
    @Column(name = "gross_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal grossAmount;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal discountAmount;

    @NotNull
    @Column(name = "net_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Size(max = 50)
    @NotNull
    @Column(name = "payment_status", nullable = false, length = 50)
    PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_date")
    LocalDateTime paymentDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}