package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.ServiceFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceFeatureRepository extends JpaRepository<ServiceFeature, Long> {
}