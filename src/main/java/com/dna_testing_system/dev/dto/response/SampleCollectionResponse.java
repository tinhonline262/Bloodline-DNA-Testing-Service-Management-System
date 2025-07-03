package com.dna_testing_system.dev.dto.response;

import com.dna_testing_system.dev.enums.SampleQuality;

import java.time.LocalDateTime;

public class SampleCollectionResponse {
    private Long id;
    private String sampleId;
    private LocalDateTime collectionDateTime;
    private String collectedBy;
    private String sampleType;
    private SampleQuality sampleQuality;
    private String conditionNotes;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }

    public LocalDateTime getCollectionDateTime() { return collectionDateTime; }
    public void setCollectionDateTime(LocalDateTime collectionDateTime) { this.collectionDateTime = collectionDateTime; }

    public String getCollectedBy() { return collectedBy; }
    public void setCollectedBy(String collectedBy) { this.collectedBy = collectedBy; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    public SampleQuality getSampleQuality() { return sampleQuality; }
    public void setSampleQuality(SampleQuality sampleQuality) { this.sampleQuality = sampleQuality; }

    public String getConditionNotes() { return conditionNotes; }
    public void setConditionNotes(String conditionNotes) { this.conditionNotes = conditionNotes; }
}