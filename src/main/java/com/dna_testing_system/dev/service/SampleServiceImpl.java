package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.CustodyRecord;
import com.dna_testing_system.dev.entity.Sample;
import com.dna_testing_system.dev.repository.CustodyRecordRepository;
import com.dna_testing_system.dev.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private CustodyRecordRepository custodyRecordRepository;

    @Override
    public Sample createSample(Sample sample) {
        if (sampleRepository.existsBySampleId(sample.getSampleId())) {
            throw new IllegalArgumentException("Sample ID already exists");
        }
        sample.setCollectionDate(LocalDateTime.now()); // Ghi thời gian thu thập
        return sampleRepository.save(sample);
    }

    @Override
    public Sample updateSampleCondition(Long id, String condition) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sample not found"));
        sample.setCondition(condition);
        return sampleRepository.save(sample);
    }

    @Override
    public Sample updateQualityAssessment(Long id, String qualityAssessment) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sample not found"));
        sample.setQualityAssessment(qualityAssessment);
        return sampleRepository.save(sample);
    }

    @Override
    public Sample addCustodyRecord(Long sampleId, String transferredBy, String receivedBy) {
        Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(() -> new IllegalArgumentException("Sample not found"));
        CustodyRecord record = new CustodyRecord();
        record.setSample(sample);
        record.setTransferredBy(transferredBy);
        record.setReceivedBy(receivedBy);
        record.setTimestamp(LocalDateTime.now());
        custodyRecordRepository.save(record);
        return sample;
    }

    @Override
    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sample not found"));
    }

    @Override
    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

    @Override
    public List<CustodyRecord> getCustodyRecords(Long sampleId) {
        return custodyRecordRepository.findBySampleId(sampleId);
    }
}