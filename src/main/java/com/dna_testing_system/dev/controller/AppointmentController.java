package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.entity.Customer;
import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.enums.CollectionMethod;
import com.dna_testing_system.dev.repository.ParticipantRepository;
import com.dna_testing_system.dev.service.BookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/v1")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ParticipantRepository participantRepository;

    @GetMapping({"/", "/index", "/appointment"})
    public String showAppointmentForm(@RequestParam(value = "serviceId", required = false) Long serviceId,
                                      Model model) {
        try {
            logger.info("Loading appointment form with serviceId: {}", serviceId);
            Customer customer = new Customer();
            DnaService service;
            if (serviceId != null) {
                service = bookingService.getServiceById(serviceId);
                if (service == null) {
                    logger.warn("Service not found with id: {}, using default", serviceId);
                    service = new DnaService();
                    service.setId(0L);
                    service.setServiceName("Dịch vụ mặc định");
                }
                customer.setService(service);
            } else {
                logger.info("No serviceId provided, using default service");
                service = new DnaService();
                service.setId(0L);
                service.setServiceName("Dịch vụ mặc định");
                customer.setService(service);
            }
            model.addAttribute("customer", customer);
            model.addAttribute("collectionMethods", CollectionMethod.values());
            model.addAttribute("services", bookingService.getAllServices());
            return "appointment";
        } catch (Exception e) {
            logger.error("Error loading appointment form: {}", e.getMessage(), e);
            model.addAttribute("error", "Có lỗi xảy ra khi tải trang: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/booking")
    public String processBooking(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "participantNames", required = false) String[] participantNames,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            logger.info("Processing booking for customer: {}", customer.getName());
            logger.debug("Received customer data: {}", customer);
            logger.debug("Received participant names: {}", Arrays.toString(participantNames));

            if (bindingResult.hasErrors()) {
                logger.warn("Validation errors found: {}", bindingResult.getAllErrors());
                model.addAttribute("service", customer.getService());
                model.addAttribute("collectionMethods", CollectionMethod.values());
                model.addAttribute("services", bookingService.getAllServices());
                return "appointment";
            }

            Long serviceId = customer.getService() != null ? customer.getService().getId() : null;
            if (serviceId == null || serviceId <= 0) {
                logger.error("Service ID is invalid: {}", serviceId);
                model.addAttribute("error", "Vui lòng chọn một dịch vụ hợp lệ!");
                model.addAttribute("service", customer.getService());
                model.addAttribute("collectionMethods", CollectionMethod.values());
                model.addAttribute("services", bookingService.getAllServices());
                return "appointment";
            }

            DnaService service = bookingService.getServiceById(serviceId);
            if (service == null) {
                throw new IllegalArgumentException("Dịch vụ với ID " + serviceId + " không tồn tại");
            }
            customer.setService(service);

            logger.debug("Collection method before booking: {}", customer.getCollectionMethod());

            bookingService.createBooking(customer, participantNames != null ? Arrays.asList(participantNames) : null);
            redirectAttributes.addFlashAttribute("success", "Đặt lịch thành công!");
            redirectAttributes.addFlashAttribute("customer", customer);
            redirectAttributes.addFlashAttribute("participants", participantNames != null ? Arrays.asList(participantNames) : null);
            return "redirect:/v1/booking-success";
        } catch (Exception e) {
            logger.error("Error during booking: {}", e.getMessage(), e);
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("service", customer.getService());
            model.addAttribute("collectionMethods", CollectionMethod.values());
            model.addAttribute("services", bookingService.getAllServices());
            return "appointment";
        }
    }

    @GetMapping("/booking-success")
    public String showBookingSuccess(@ModelAttribute("customer") Customer customer,
                                     @ModelAttribute("participants") List<String> participants,
                                     Model model) {
        if (customer == null || customer.getName() == null) {
            logger.warn("No customer data in booking success page");
            return "redirect:/v1/appointment";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("participants", participants);
        model.addAttribute("success", "Đặt lịch thành công cho " + customer.getName() + "!");
        return "booking-success";
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        try {
            logger.info("Loading services page");
            List<DnaService> services = bookingService.getAllServices();
            model.addAttribute("services", services);
            return "services";
        } catch (Exception e) {
            logger.error("Error loading services page: {}", e.getMessage(), e);
            model.addAttribute("error", "Có lỗi xảy ra khi tải trang: " + e.getMessage());
            return "error";
        }
    }
}