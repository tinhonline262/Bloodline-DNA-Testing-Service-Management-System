package com.dna_testing_system.dev.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "tbl_service_types")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_type_id", nullable = false)
    Long id;

    @Column(name = "type_name", nullable = false, unique = true, length = 100)
    String typeName;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<MedicalService> medicalServices = new HashSet<>();
}
