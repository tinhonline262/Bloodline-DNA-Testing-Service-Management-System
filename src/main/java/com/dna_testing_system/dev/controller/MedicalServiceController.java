package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.service.MedicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/medical-services")
@RequiredArgsConstructor
public class MedicalServiceController {

    private final MedicalServiceService medicalServiceService;

    @PostMapping
    public ResponseEntity<MedicalService> createMedicalService(@Valid @RequestBody MedicalService medicalService) {
        MedicalService createdService = medicalServiceService.createMedicalService(medicalService);
        return ResponseEntity.ok(createdService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalService> getMedicalServiceById(@PathVariable Long id) {
        return medicalServiceService.getMedicalServiceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MedicalService>> getAllMedicalServices() {
        return ResponseEntity.ok(medicalServiceService.getAllMedicalServices());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalService> updateMedicalService(@PathVariable Long id, @Valid @RequestBody MedicalService medicalService) {
        MedicalService updatedService = medicalServiceService.updateMedicalService(id, medicalService);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalService(@PathVariable Long id) {
        medicalServiceService.deleteMedicalService(id);
        return ResponseEntity.noContent().build();
    }
}