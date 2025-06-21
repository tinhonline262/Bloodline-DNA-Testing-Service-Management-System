package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.CollectionMethod;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private DnaService service;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_method", nullable = false)
    private CollectionMethod collectionMethod;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DnaService getService() {
        return service;
    }

    public void setService(DnaService service) {
        this.service = service;
    }

    public CollectionMethod getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(CollectionMethod collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}