package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.Booking;

public interface EmailService {
    void sendBookingConfirmationEmail(Booking booking);
}