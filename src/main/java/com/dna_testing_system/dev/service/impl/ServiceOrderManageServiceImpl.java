package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.dto.request.OrderParticipantRequest;
import com.dna_testing_system.dev.dto.request.ServiceOrderRequest;
import com.dna_testing_system.dev.dto.response.MedicalServiceResponse;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.entity.OrderParticipant;
import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.enums.CollectionType;
import com.dna_testing_system.dev.enums.Gender; // Thêm import cho enum Gender
import com.dna_testing_system.dev.enums.PaymentStatus;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;
import com.dna_testing_system.dev.exception.ErrorCode;
import com.dna_testing_system.dev.exception.ResourceNotFoundException;
import com.dna_testing_system.dev.mapper.MedicalServiceMapper;
import com.dna_testing_system.dev.mapper.ServiceOrderMapper;
import com.dna_testing_system.dev.repository.MedicalServiceRepository;
import com.dna_testing_system.dev.repository.OrderParticipantRepository;
import com.dna_testing_system.dev.repository.ServiceOrderRepository;
import com.dna_testing_system.dev.repository.UserRepository;
import com.dna_testing_system.dev.service.ServiceOrderManageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceOrderManageServiceImpl implements ServiceOrderManageService {

    private final UserRepository userRepository;
    private final MedicalServiceRepository medicalServiceRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final OrderParticipantRepository orderParticipantRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final MedicalServiceMapper medicalServiceMapper;

    @Override
    @Transactional
    public ServiceOrderResponse createOrder(ServiceOrderRequest request) {
        if (request.getCustomerId() == null) {
            throw new ResourceNotFoundException(ErrorCode.INVALID_CUSTOMER_ID);
        }
        log.info("Creating order for customerId: {} at {}", request.getCustomerId(), LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTS));

        MedicalService service = medicalServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEDICAL_SERVICE_NOT_EXISTS));
        if (!service.getIsAvailable()) {
            throw new ResourceNotFoundException(ErrorCode.MEDICAL_SERVICE_NOT_AVAILABLE);
        }

        if (!isValidCollectionType(String.valueOf(request.getCollectionType()))) {
            throw new ResourceNotFoundException(ErrorCode.INVALID_COLLECTION_TYPE);
        }

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        if (request.getAppointmentDate() == null || request.getAppointmentDate().isBefore(now) || request.getAppointmentDate().isBefore(now.plusHours(24))) {
            log.error("Invalid appointment date: {} for customerId: {}", request.getAppointmentDate(), request.getCustomerId());
            throw new ResourceNotFoundException(ErrorCode.INVALID_APPOINTMENT_DATE);
        }

        ServiceOrder order = ServiceOrder.builder()
                .customer(customer)
                .service(service)
                .appointmentDate(request.getAppointmentDate())
                .collectionType(CollectionType.valueOf(String.valueOf(request.getCollectionType())))
                .collectionAddress(request.getCollectionAddress())
                .orderStatus(ServiceOrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .totalAmount(service.getCurrentPrice() != null ? service.getCurrentPrice() : service.getPrice())
                .finalAmount(calculateFinalAmount(service, request.getParticipants()))
                .orderParticipants(new HashSet<>())
                .build();

        if (request.getParticipants() != null && !request.getParticipants().isEmpty()) {
            validateParticipants(request.getParticipants(), service.getParticipants());
            for (OrderParticipantRequest participantRequest : request.getParticipants()) {
                String[] nameParts = participantRequest.getFullName().trim().split("\\s+", 2);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : "";
                OrderParticipant participant = OrderParticipant.builder()
                        .order(order)
                        .firstName(firstName)
                        .lastName(lastName)
                        .dateOfBirth(participantRequest.getDateOfBirth())
                        .gender(participantRequest.getGender())
                        .build();
                order.getOrderParticipants().add(participant);
            }
        }

        ServiceOrder savedOrder = serviceOrderRepository.save(order);
        log.info("Order created successfully with id: {} at {}", savedOrder.getId(), LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        return serviceOrderMapper.toResponse(savedOrder);
    }

    private BigDecimal calculateFinalAmount(MedicalService service, List<OrderParticipantRequest> participants) {
        BigDecimal basePrice = service.getCurrentPrice() != null ? service.getCurrentPrice() : service.getPrice();
        if (participants == null || participants.isEmpty()) {
            return basePrice;
        }
        int participantCount = participants.size();
        int requiredParticipants = service.getParticipants() != null ? service.getParticipants() : 1;
        if (participantCount > requiredParticipants) {
            BigDecimal extraFee = BigDecimal.valueOf((participantCount - requiredParticipants) * 0.1);
            return basePrice.multiply(BigDecimal.ONE.add(extraFee));
        }
        return basePrice;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceOrderResponse getOrderById(Long id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_EXISTS));
        return serviceOrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceOrderResponse> getOrdersByCustomer(String customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTS));
        List<ServiceOrder> orders = serviceOrderRepository.findByCustomer(customer);
        return orders.stream().map(serviceOrderMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServiceOrderResponse updateOrder(Long id, ServiceOrderRequest request) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_EXISTS));

        if (order.getOrderStatus() == ServiceOrderStatus.CONFIRMED) {
            throw new ResourceNotFoundException(ErrorCode.ORDER_ALREADY_CONFIRMED);
        }

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        if (request.getAppointmentDate() != null && request.getAppointmentDate().isAfter(now.plusHours(24))) {
            order.setAppointmentDate(request.getAppointmentDate());
        } else if (request.getAppointmentDate() != null) {
            log.error("Invalid appointment date: {} for orderId: {}", request.getAppointmentDate(), id);
            throw new ResourceNotFoundException(ErrorCode.INVALID_APPOINTMENT_DATE);
        }

        if (request.getCollectionType() != null && isValidCollectionType(String.valueOf(request.getCollectionType()))) {
            order.setCollectionType(CollectionType.valueOf(String.valueOf(request.getCollectionType())));
        }
        if (request.getCollectionAddress() != null) {
            order.setCollectionAddress(request.getCollectionAddress());
        }

        if (request.getParticipants() != null && !request.getParticipants().isEmpty()) {
            validateParticipants(request.getParticipants(), order.getService().getParticipants());
            order.getOrderParticipants().clear();
            for (OrderParticipantRequest participantRequest : request.getParticipants()) {
                String[] nameParts = participantRequest.getFullName().trim().split("\\s+", 2);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : "";
                OrderParticipant participant = OrderParticipant.builder()
                        .order(order)
                        .firstName(firstName)
                        .lastName(lastName)
                        .dateOfBirth(participantRequest.getDateOfBirth())
                        .gender(participantRequest.getGender())
                        .build();
                order.getOrderParticipants().add(participant);
            }
        }

        order.setFinalAmount(calculateFinalAmount(order.getService(), request.getParticipants()));
        ServiceOrder updatedOrder = serviceOrderRepository.save(order);
        return serviceOrderMapper.toResponse(updatedOrder);
    }

    /**
     * Xác thực danh sách người tham gia dựa trên số lượng tối đa cho phép.
     *
     * @param participants Danh sách người tham gia.
     * @param maxParticipants Số lượng người tham gia tối đa.
     * @throws ResourceNotFoundException Nếu dữ liệu không hợp lệ.
     */
    private void validateParticipants(List<OrderParticipantRequest> participants, Integer maxParticipants) {
        if (participants == null || participants.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCode.NO_PARTICIPANTS);
        }
        if (maxParticipants == null || participants.size() > maxParticipants) {
            throw new ResourceNotFoundException(ErrorCode.TOO_MANY_PARTICIPANTS);
        }
        for (OrderParticipantRequest participant : participants) {
            if (participant.getFullName() == null || participant.getFullName().trim().isEmpty()) {
                throw new ResourceNotFoundException(ErrorCode.INVALID_PARTICIPANT_NAME);
            }
            if (participant.getDateOfBirth() == null) {
                throw new ResourceNotFoundException(ErrorCode.INVALID_PARTICIPANT_DOB);
            }
            if (participant.getGender() == null || participant.getGender().trim().isEmpty() || !Gender.isValid(participant.getGender())) {
                throw new ResourceNotFoundException(ErrorCode.INVALID_PARTICIPANT_GENDER);
            }
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_EXISTS));
        if (order.getOrderStatus() == ServiceOrderStatus.CONFIRMED) {
            throw new ResourceNotFoundException(ErrorCode.ORDER_ALREADY_CONFIRMED);
        }
        order.setOrderStatus(ServiceOrderStatus.CANCELLED);
        serviceOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalServiceResponse> getServiceCatalog() {
        List<MedicalService> services = medicalServiceRepository.findByIsAvailableTrue();
        return medicalServiceMapper.toResponse(services);
    }

    @Override
    @Transactional
    public ServiceOrderResponse confirmOrder(Long id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_EXISTS));

        if (order.getOrderStatus() != ServiceOrderStatus.PENDING) {
            throw new ResourceNotFoundException(ErrorCode.ORDER_ALREADY_CONFIRMED);
        }
        if (order.getOrderParticipants().size() < order.getService().getParticipants()) {
            throw new ResourceNotFoundException(ErrorCode.INSUFFICIENT_PARTICIPANTS);
        }

        order.setOrderStatus(ServiceOrderStatus.CONFIRMED);
        ServiceOrder confirmedOrder = serviceOrderRepository.save(order);
        log.info("Order confirmed successfully with id: {} at {}", confirmedOrder.getId(), LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        return serviceOrderMapper.toResponse(confirmedOrder);
    }

    private boolean isValidCollectionType(String collectionType) {
        try {
            CollectionType.valueOf(collectionType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}