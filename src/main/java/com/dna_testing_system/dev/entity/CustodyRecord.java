package com.dna_testing_system.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "custody_records")
@Data
@NoArgsConstructor
public class CustodyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sample_id", nullable = false)
    private Sample sample;

    @NotBlank(message = "Người chuyển giao là bắt buộc")
    @Column(nullable = false)
    private String transferredBy;

    @NotBlank(message = "Người nhận là bắt buộc")
    @Column(nullable = false)
    private String receivedBy;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}