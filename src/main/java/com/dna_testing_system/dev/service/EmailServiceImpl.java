package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendBookingConfirmationEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getCustomer().getEmail());
            message.setSubject("Booking Confirmation - DNA Testing System");
            message.setText(String.format(
                    "Dear %s,\n\nYour booking has been confirmed at %s.\nBooking ID: %d\nService: %s\nAppointment Date: %s\nCollection Method: %s\n\nThank you!",
                    booking.getCustomer().getName(),
                    java.time.LocalDateTime.now(),
                    booking.getId(),
                    booking.getService().getServiceName(),
                    booking.getAppointmentDate(),
                    booking.getCollectionMethod().getDisplayName()
            ));
            mailSender.send(message);
            logger.info("Confirmation email sent to: {} at {}", booking.getCustomer().getEmail(), java.time.LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", booking.getCustomer().getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send confirmation email: " + e.getMessage(), e);
        }
    }
}