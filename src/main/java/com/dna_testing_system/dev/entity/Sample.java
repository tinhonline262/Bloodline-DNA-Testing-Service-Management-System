package com.dna_testing_system.dev.entity;

import com.dna_testing_system.dev.enums.SampleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "samples")
@Data
@NoArgsConstructor
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Loại mẫu là bắt buộc")
    @Column(nullable = false)
    private SampleType sampleType;

    @NotBlank(message = "ID mẫu là bắt buộc")
    @Column(nullable = false, unique = true)
    private String sampleId;

    @NotNull(message = "Ngày thu thập là bắt buộc")
    @Column(nullable = false)
    private LocalDateTime collectionDate;

    @Column(name = "`condition`")
    private String condition;

    private String qualityAssessment;

    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustodyRecord> custodyRecords = new ArrayList<>();
}