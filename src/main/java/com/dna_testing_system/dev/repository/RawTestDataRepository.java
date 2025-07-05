package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.RawTestData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawTestDataRepository extends JpaRepository<RawTestData,Long> {
}
