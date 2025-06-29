package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.dto.request.TestKitRequest;
import com.dna_testing_system.dev.dto.response.TestKitResponse;
import com.dna_testing_system.dev.entity.TestKit;
import com.dna_testing_system.dev.mapper.TestKitMapper;
import com.dna_testing_system.dev.repository.TestKitRepository;
import com.dna_testing_system.dev.service.TestKitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestKitServiceImpl implements TestKitService {

    TestKitRepository testKitRepository;
    TestKitMapper testKitMapper;


    @Override
    @Transactional
    public void CreateTestKit(TestKitRequest testKitRequest) {
        TestKit testKit = testKitMapper.toEntity(testKitRequest);
        testKitRepository.save(testKit);
    }

    @Override
    public List<TestKitResponse> GetTestKitResponseByName(String kitName) {
        List<TestKit> testKits = testKitRepository.findAll();
        List<TestKitResponse> testKitResponses = new ArrayList<>();
        for (TestKit testKit : testKits) {
            if (testKit.getKitName().equalsIgnoreCase(kitName)) {
                TestKitResponse response = testKitMapper.toResponse(testKit);
                testKitResponses.add(response);
            }
        }
        return testKitResponses;
    }

    @Override
    public List<TestKitResponse> GetTestKitResponseList() {
        List<TestKit> testKits = testKitRepository.findAll();
        List<TestKitResponse> testKitResponses = new ArrayList<>();
        for (TestKit testKit : testKits) {
            TestKitResponse response = testKitMapper.toResponse(testKit);
            testKitResponses.add(response);
        }
        return testKitResponses;
    }

    @Override
    @Transactional
    public void UpdateTestKit(Long kitId, TestKitRequest testKitRequest) {
        TestKit testKit = testKitRepository.findById(kitId)
                .orElseThrow(() -> new RuntimeException("Test Kit not found with id: " + kitId));
        LocalDateTime currentTime = LocalDateTime.now();
        testKit.setUpdatedAt(currentTime);
        testKitMapper.updateEntityFromDto(testKitRequest, testKit);
        testKitRepository.save(testKit);
    }

    @Override
    @Transactional
    public void DeleteTestKit(Long kitId) {
        if (!testKitRepository.existsById(kitId)) {
            throw new RuntimeException("Test Kit not found with id: " + kitId);
        }
        testKitRepository.deleteById(kitId);
    }

    @Override
    public TestKitResponse GetTestKitResponseById(Long kitId) {
        TestKit testKit = testKitRepository.findById(kitId)
                .orElseThrow(() -> new RuntimeException("Test Kit not found with id: " + kitId));
        return testKitMapper.toResponse(testKit);
    }
}
