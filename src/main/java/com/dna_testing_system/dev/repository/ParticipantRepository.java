
        package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByBookingId(Long bookingId);
}
