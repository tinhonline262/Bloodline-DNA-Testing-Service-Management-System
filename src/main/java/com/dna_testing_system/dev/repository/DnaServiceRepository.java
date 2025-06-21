package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.DnaService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DnaServiceRepository extends JpaRepository<DnaService, Long> {
}