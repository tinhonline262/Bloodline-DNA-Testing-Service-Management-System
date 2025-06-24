package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.entity.Booking;
import com.dna_testing_system.dev.entity.Customer;
import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Void> createBooking(
            @Valid @RequestBody Customer customer,
            @RequestParam(required = false) List<String> participantNames) {
        bookingService.createBooking(customer, participantNames != null ? participantNames : List.of());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/services")
    public ResponseEntity<List<DnaService>> getAllServices() {
        return ResponseEntity.ok(bookingService.getAllServices());
    }
}