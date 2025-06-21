package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}