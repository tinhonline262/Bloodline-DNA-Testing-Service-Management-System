package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.MedicalService;
import java.util.Optional;

public interface ServiceService {
    Optional<MedicalService> getServiceById(Long id);
}