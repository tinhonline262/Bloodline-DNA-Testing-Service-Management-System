package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.repository.MedicalServiceRepository;
import com.dna_testing_system.dev.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final MedicalServiceRepository medicalServiceRepository;

    @Override
    public Optional<MedicalService> getServiceById(Long id) {
        return medicalServiceRepository.findById(id);
    }
}
