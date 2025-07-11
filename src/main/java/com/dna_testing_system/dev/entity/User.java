package com.dna_testing_system.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "tbl_users")
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, unique = true, length = 50)
    String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    Boolean isActive = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    Set<ServiceOrder> serviceOrders = new HashSet<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    Set<CustomerFeedback> customerFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    Set<ContentPost> contentPosts = new HashSet<>();
}