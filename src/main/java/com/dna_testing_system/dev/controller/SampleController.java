package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.entity.CustodyRecord;
import com.dna_testing_system.dev.entity.Sample;
import com.dna_testing_system.dev.enums.SampleType;
import com.dna_testing_system.dev.service.SampleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @PostMapping
    public ResponseEntity<Sample> createSample(@Valid @RequestBody Sample sample) {
        return ResponseEntity.ok(sampleService.createSample(sample));
    }

    @PutMapping("/{id}/condition")
    public ResponseEntity<Sample> updateSampleCondition(
            @PathVariable Long id,
            @RequestParam String condition) {
        return ResponseEntity.ok(sampleService.updateSampleCondition(id, condition));
    }

    @PutMapping("/{id}/quality")
    public ResponseEntity<Sample> updateQualityAssessment(
            @PathVariable Long id,
            @RequestParam String qualityAssessment) {
        return ResponseEntity.ok(sampleService.updateQualityAssessment(id, qualityAssessment));
    }

    @PostMapping("/{id}/custody")
    public ResponseEntity<Sample> addCustodyRecord(
            @PathVariable Long id,
            @RequestParam String transferredBy,
            @RequestParam String receivedBy) {
        return ResponseEntity.ok(sampleService.addCustodyRecord(id, transferredBy, receivedBy));
    }

    @GetMapping("/sample-types")
    public ResponseEntity<SampleType[]> getSampleTypes() {
        return ResponseEntity.ok(SampleType.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sample> getSampleById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.getSampleById(id));
    }

    @GetMapping
    public ResponseEntity<List<Sample>> getAllSamples() {
        return ResponseEntity.ok(sampleService.getAllSamples());
    }

    @GetMapping("/{id}/custody")
    public ResponseEntity<List<CustodyRecord>> getCustodyRecords(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.getCustodyRecords(id));
    }
}