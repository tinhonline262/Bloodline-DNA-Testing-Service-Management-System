package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    boolean existsBySampleId(String sampleId);
}