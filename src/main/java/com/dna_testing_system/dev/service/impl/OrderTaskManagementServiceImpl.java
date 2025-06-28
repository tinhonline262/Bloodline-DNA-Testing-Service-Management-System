package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.dto.request.StaffAvailableRequest;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.dto.response.StaffAvailableResponse;
import com.dna_testing_system.dev.entity.Role;
import com.dna_testing_system.dev.entity.SampleCollection;
import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.enums.RoleType;
import com.dna_testing_system.dev.enums.SampleQuality;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;
import com.dna_testing_system.dev.exception.ErrorCode;
import com.dna_testing_system.dev.exception.ResourceNotFoundException;
import com.dna_testing_system.dev.mapper.ServiceOrderMapper;
import com.dna_testing_system.dev.repository.*;
import com.dna_testing_system.dev.service.EmailSender;
import com.dna_testing_system.dev.service.OrderTaskManagementService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderTaskManagementServiceImpl implements OrderTaskManagementService {
    UserRepository userRepository;
    SampleCollectionRepository sampleCollectionRepository;
    TestResultRepository testResultRepository;
    ServiceOrderRepository serviceOrderRepository;
    EmailSender emailSender;
    ServiceOrderMapper serviceOrderMapper;

    @Override
    public List<StaffAvailableResponse> getStaffAvailable() {
        List<User> staffUser = userRepository.findAll().stream()
                .filter(user -> user.getUserRoles()
                        .stream().anyMatch(userRole -> userRole.getRole().getRoleName().equals(RoleType.STAFF.name())))
                .toList();

        var staffsExistInCollectionSample = sampleCollectionRepository.findAll()
                .stream().map(samp -> samp.getStaff().getId())
                .toList(); // staff exits in sample collection is inAvailable
        var staffsExistInTestResult = testResultRepository.findAll()
                .stream().map(testResult -> testResult.getAnalyzedByStaff().getId())
                .toList(); // same

        List<User> staffAvailable = staffUser.stream()
                .filter(staff -> !staffsExistInCollectionSample.contains(staff.getId())
                && !staffsExistInTestResult.contains(staff.getId()))
                .toList();
        if  (staffAvailable.isEmpty()) {
            return new ArrayList<>();
        }
        List<StaffAvailableResponse> staffAvailableResponses = new ArrayList<>();
        for (User user : staffAvailable) {
            StaffAvailableResponse staffAvailableResponse = StaffAvailableResponse.builder()
                    .id(user.getId())
                    .email(user.getUserProfile().getEmail())
                    .username(user.getUsername())
                    .profileImageUrl(user.getUserProfile().getProfileImageUrl())
                    .roleName(user.getUserRoles().stream().map(x -> x.getRole().getRoleName()).collect(Collectors.toSet()))
                    .isActive(user.getIsActive())
                    .createdAt(user.getUserProfile().getCreatedAt())
                    .fullName(user.getUserProfile().getFirstName() + " " + user.getUserProfile().getLastName())
                    .phoneNumber(user.getUserProfile().getPhoneNumber())
                    .build();
            staffAvailableResponses.add(staffAvailableResponse);
        }
        return staffAvailableResponses;
    }

    @Override
    @Transactional
    public void sampleCollectionTaskAssignment(Long orderId, StaffAvailableRequest staffRequest) {
        log.info("Assigning sample collection task for order ID: {} to staff ID: {}", orderId, staffRequest.getStaffId());

        // Find the order
        var order = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND));

        // Find the staff member
        var staff = userRepository.findById(staffRequest.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND));

        // Create sample collection task
        SampleCollection sampleCollection = new SampleCollection();
        sampleCollection.setOrder(order);
        sampleCollection.setStaff(staff);
        sampleCollection.setAssignAt(LocalDateTime.now());
        sampleCollection.setCollectionDate(order.getAppointmentDate());
        sampleCollection.setSampleQuality(SampleQuality.EXCELLENT); // mock, sửa sau

        sampleCollectionRepository.save(sampleCollection);

        order.setOrderStatus(ServiceOrderStatus.CONFIRMED);
        serviceOrderRepository.save(order);

        try {
            User staffUser = new User();
            staffUser.setId(staff.getId());
            staffUser.setUsername(staff.getUsername());
            staffUser.setUserProfile(staff.getUserProfile());

            emailSender.sendTestAssignmentNotification(order, sampleCollection, staffUser);
            log.info("Email notification sent to staff member: {}", staff.getUserProfile().getEmail());
        } catch (Exception e) {
            log.error("Failed to send email notification to staff: {}", e.getMessage());
            // Continue execution - email failure shouldn't prevent assignment
        }

        log.info("Successfully assigned sample collection task for order ID: {} to staff: {}",
                orderId, staff.getUsername());
    }

    @Override
    public List<ServiceOrderResponse> getServiceOrders() {
        var orders = serviceOrderRepository.findAll();
        List<ServiceOrderResponse> serviceOrderResponses = new ArrayList<>();
        for (ServiceOrder serviceOrder : orders) {
            ServiceOrderResponse serviceOrderResponse = serviceOrderMapper.toDto(serviceOrder);
            serviceOrderResponses.add(serviceOrderResponse);
        }
        return serviceOrderResponses;
    }
}
