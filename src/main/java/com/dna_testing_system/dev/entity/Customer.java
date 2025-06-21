package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.CollectionMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Họ và tên không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "[0-9]{10}", message = "Số điện thoại phải có 10 chữ số")
    @Column(unique = true)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private DnaService service;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_method", nullable = false)
    private CollectionMethod collectionMethod;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    public Customer() {
        this.appointmentDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public DnaService getService() { return service; }
    public void setService(DnaService service) { this.service = service; }
    public CollectionMethod getCollectionMethod() { return collectionMethod; }
    public void setCollectionMethod(CollectionMethod collectionMethod) { this.collectionMethod = collectionMethod; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
}