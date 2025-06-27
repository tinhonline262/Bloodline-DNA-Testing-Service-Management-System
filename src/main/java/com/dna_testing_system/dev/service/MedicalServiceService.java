package com.dna_testing_system.dev.service;

import java.math.BigDecimal;
import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalServiceService {

    private final MedicalServiceRepository medicalServiceRepository;

    @Transactional
    public MedicalService createMedicalService(MedicalService medicalService) {
        validateMedicalService(medicalService);
        return medicalServiceRepository.save(medicalService);
    }

    @Transactional(readOnly = true)
    public Optional<MedicalService> getMedicalServiceById(Long id) {
        return medicalServiceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MedicalService> getAllMedicalServices() {
        return medicalServiceRepository.findAll();
    }

    @Transactional
    public MedicalService updateMedicalService(Long id, MedicalService updatedService) {
        MedicalService existingService = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalService not found with id: " + id));

        existingService.setServiceName(updatedService.getServiceName());
        existingService.setDescription(updatedService.getDescription());
        existingService.setPrice(updatedService.getPrice());
        existingService.setServiceCategory(updatedService.getServiceCategory());
        existingService.setParticipants(updatedService.getParticipants());
        existingService.setExecutionTimeDays(updatedService.getExecutionTimeDays());
        existingService.setIsAvailable(updatedService.getIsAvailable());
        existingService.setIsBestValue(updatedService.getIsBestValue());

        return medicalServiceRepository.save(existingService);
    }

    @Transactional
    public void deleteMedicalService(Long id) {
        if (!medicalServiceRepository.existsById(id)) {
            throw new EntityNotFoundException("MedicalService not found with id: " + id);
        }
        medicalServiceRepository.deleteById(id);
    }

    private void validateMedicalService(MedicalService medicalService) {
        if (medicalService.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}