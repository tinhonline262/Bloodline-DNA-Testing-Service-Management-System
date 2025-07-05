package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
}
