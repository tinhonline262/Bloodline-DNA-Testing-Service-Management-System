package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.CustodyRecord;
import com.dna_testing_system.dev.entity.Sample;

import java.util.List;

public interface SampleService {
    Sample createSample(Sample sample);
    Sample updateSampleCondition(Long id, String condition);
    Sample updateQualityAssessment(Long id, String qualityAssessment);
    Sample addCustodyRecord(Long sampleId, String transferredBy, String receivedBy);
    Sample getSampleById(Long id);
    List<Sample> getAllSamples();
    List<CustodyRecord> getCustodyRecords(Long sampleId);
}