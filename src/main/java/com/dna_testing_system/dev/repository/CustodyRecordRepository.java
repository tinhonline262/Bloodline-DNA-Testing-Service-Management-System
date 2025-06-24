package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.CustodyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustodyRecordRepository extends JpaRepository<CustodyRecord, Long> {
    List<CustodyRecord> findBySampleId(Long sampleId);
}