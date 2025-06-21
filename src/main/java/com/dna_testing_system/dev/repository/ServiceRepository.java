package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.DnaService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<DnaService, Long> {
}
