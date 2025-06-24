package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.Booking;
import com.dna_testing_system.dev.entity.Customer;
import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.entity.Participant;
import com.dna_testing_system.dev.enums.CollectionMethod;
import com.dna_testing_system.dev.repository.BookingRepository;
import com.dna_testing_system.dev.repository.CustomerRepository;
import com.dna_testing_system.dev.repository.DnaServiceRepository;
import com.dna_testing_system.dev.repository.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DnaServiceRepository dnaServiceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EmailService emailService;

    public DnaService getServiceById(Long serviceId) {
        logger.info("Fetching service with id: {}", serviceId);
        Optional<DnaService> serviceOpt = dnaServiceRepository.findById(serviceId);
        return serviceOpt.orElse(null);
    }

    @Transactional
    public void createBooking(Customer customer, List<String> participantNames) {
        if (customer == null || customer.getService() == null || customer.getCollectionMethod() == null || customer.getAppointmentDate() == null) {
            logger.error("Invalid input data for booking: customer={}, service={}, collectionMethod={}, appointmentDate={}",
                    customer, customer != null ? customer.getService() : null, customer != null ? customer.getCollectionMethod() : null, customer != null ? customer.getAppointmentDate() : null);
            throw new IllegalArgumentException("Customer, service, collection method, or appointment date cannot be null");
        }

        // Thêm kiểm tra validation cho email và phone
        if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        if (!customer.getPhone().matches("[0-9]{10}")) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số");
        }

        try {
            logger.info("Saving booking for customer: {} at {}", customer.getName(), LocalDateTime.now());
            DnaService service = getServiceById(customer.getService().getId());
            if (service == null) {
                logger.error("Service not found with id: {}", customer.getService().getId());
                throw new IllegalArgumentException("Dịch vụ với ID " + customer.getService().getId() + " không tồn tại");
            }

            Customer existingCustomer = customerRepository.findByPhone(customer.getPhone())
                    .orElseGet(() -> {
                        Customer newCustomer = new Customer();
                        newCustomer.setName(customer.getName());
                        newCustomer.setEmail(customer.getEmail());
                        newCustomer.setPhone(customer.getPhone());
                        newCustomer.setMessage(customer.getMessage());
                        newCustomer.setService(customer.getService());
                        newCustomer.setCollectionMethod(customer.getCollectionMethod());
                        newCustomer.setAppointmentDate(customer.getAppointmentDate());
                        return customerRepository.save(newCustomer);
                    });
            logger.info("Customer found or saved with id: {}", existingCustomer.getId());

            Booking booking = new Booking();
            booking.setCustomer(existingCustomer);
            booking.setService(service);
            booking.setCollectionMethod(customer.getCollectionMethod());
            booking.setAppointmentDate(customer.getAppointmentDate());

            Booking savedBooking = bookingRepository.save(booking);
            if (savedBooking == null || savedBooking.getId() == null) {
                logger.error("Failed to save booking for customer: {}", existingCustomer.getName());
                throw new RuntimeException("Không thể lưu booking vào database");
            }
            logger.info("Booking saved successfully with id: {}. Thông báo: Đặt lịch thành công cho khách hàng: {}", savedBooking.getId(), customer.getName());

            // Lưu thông tin người tham gia
            if (participantNames != null && !participantNames.isEmpty()) {
                for (String name : participantNames) {
                    if (name != null && !name.trim().isEmpty()) {
                        Participant participant = new Participant();
                        participant.setName(name);
                        participant.setBooking(savedBooking);
                        participantRepository.save(participant);
                    }
                }
            }

            // Gửi email xác nhận
            emailService.sendBookingConfirmationEmail(savedBooking);
        } catch (Exception e) {
            logger.error("Error creating booking: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo booking: " + e.getMessage(), e);
        }
    }

    public List<DnaService> getAllServices() {
        logger.info("Fetching all services at {}", LocalDateTime.now());
        return dnaServiceRepository.findAll();
    }
}