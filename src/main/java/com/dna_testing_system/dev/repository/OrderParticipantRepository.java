package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.OrderParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderParticipantRepository extends JpaRepository<OrderParticipant, Long> {
}