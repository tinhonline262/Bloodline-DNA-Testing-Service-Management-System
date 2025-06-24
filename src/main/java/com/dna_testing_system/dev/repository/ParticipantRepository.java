package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}