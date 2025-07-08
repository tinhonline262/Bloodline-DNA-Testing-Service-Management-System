package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.OrderTestKitRequest;
import com.dna_testing_system.dev.dto.request.ParticipantRequest;
import com.dna_testing_system.dev.dto.request.ServiceOrderRequestByCustomer;
import com.dna_testing_system.dev.dto.response.*;
import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.enums.CollectionType;
import com.dna_testing_system.dev.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderingByCustomer {
    OrderService orderService;
    OrderKitService orderKitService;
    OrderParticipantService orderParticipantService;
    MedicalServiceManageService medicalService;
    TestKitService testKitService;
    UserProfileService userProfileService;
    // Controller đã được sửa
    @GetMapping("/user/list-service")
    public String listMedicalServices(Model model) {
        // ---- BẮT ĐẦU SỬA LỖI ----
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            UserProfileResponse userProfile = userProfileService.getUserProfile(authentication.getName());
            model.addAttribute("userProfile", userProfile);
        }
        // ---- KẾT THÚC SỬA LỖI ----

        List<MedicalServiceResponse> medicalServiceResponse = medicalService.getAllServices();
        model.addAttribute("medicalServices", medicalServiceResponse);

        return "CustomerOrderService/list-service";
    }

    @GetMapping("user/order-service")
    public String orderServiceGet(Model model, @RequestParam("medicalServiceId") Long medicalServiceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", userProfile);


        ServiceOrderRequestByCustomer serviceOrderRequestByCustomer = new ServiceOrderRequestByCustomer();
        serviceOrderRequestByCustomer.setUsername(currentPrincipalName);
        serviceOrderRequestByCustomer.setIdMedicalService(medicalServiceId);
        model.addAttribute("today", LocalDateTime.now());
        model.addAttribute("collectionTypes", CollectionType.values());
        model.addAttribute("serviceOrderRequestByCustomer", serviceOrderRequestByCustomer);

        return "CustomerOrderService/order-service";
    }

    @PostMapping("user/order-service")
    public String orderServicePost(Model model, @ModelAttribute("serviceOrderRequestByCustomer") ServiceOrderRequestByCustomer serviceOrderRequestByCustomer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", userProfile);

        ServiceOrderByCustomerResponse serviceOrderByCustomerResponse = orderService.createOrder(serviceOrderRequestByCustomer);
        model.addAttribute("serviceOrderByCustomerResponse", serviceOrderByCustomerResponse);

        return "CustomerOrderService/order-service";
    }

    @GetMapping("user/participant-information")
    public String participantInformation(Model model, @RequestParam("orderId") Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserProfileResponse userProfile = userProfileService.getUserProfile(authentication.getName());
            model.addAttribute("userProfile", userProfile);
        }


        model.addAttribute("today", LocalDate.now());
        model.addAttribute("orderId", orderId);
        model.addAttribute("participantRequest", new ParticipantRequest());
        return "ParticipantOrder/ParticipantInformation";
    }

    @PostMapping("user/participant-information")
    public String participantInformationPost(Model model, @ModelAttribute("participantRequest") ParticipantRequest participantRequest, @RequestParam("orderId") Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserProfileResponse userProfile = userProfileService.getUserProfile(authentication.getName());
            model.addAttribute("userProfile", userProfile);
        }

        // Code cũ giữ nguyên
        orderParticipantService.createOrderParticipant(orderId, participantRequest);
        model.addAttribute("message", "Participant information saved successfully!");
        model.addAttribute("orderId", orderId);

        return "ParticipantOrder/ParticipantInformation";
    }

    @GetMapping("/list-kit")
    public String listKits(Model model, @RequestParam("orderId") Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserProfileResponse userProfile = userProfileService.getUserProfile(authentication.getName());
            model.addAttribute("userProfile", userProfile);
        }

        List<TestKitResponse> testKitResponse = testKitService.GetTestKitResponseList();
        model.addAttribute("testKitResponse", testKitResponse);
        model.addAttribute("orderId", orderId);
        return "OrderKit/list-kit";
    }


    @GetMapping("/order-kit")
    public String orderKit(@RequestParam("kitTestId") Long kitTestId, Model model, @RequestParam("orderId") Long orderId) {
        // THÊM userProfile VÀO MODEL
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserProfileResponse userProfile = userProfileService.getUserProfile(authentication.getName());
            model.addAttribute("userProfile", userProfile);
        }

        // Code cũ giữ nguyên
        OrderTestKitRequest request = new OrderTestKitRequest();
        TestKitResponse testKit = testKitService.GetTestKitResponseById(kitTestId);
        request.setKitTestId(kitTestId);
        request.setOrderId(orderId);
        model.addAttribute("orderTestKitRequest", request);
        model.addAttribute("maxQuantity", testKit.getQuantityInStock());
        return "OrderKit/order-kit";
    }


    @PostMapping("user/order-kit")

    public String submitOrder(@ModelAttribute("orderTestKitRequest") OrderTestKitRequest orderTestKitRequest, Model model) {

        orderKitService.createOrder(orderTestKitRequest.getOrderId(),orderTestKitRequest);

        model.addAttribute("message", "Order placed successfully!");

        return "OrderKit/order-kit"; // Redirect to the list of kits after successful order

    }


    @GetMapping("/detail")
    public String detail(Model model, @RequestParam("orderId") Long orderId) {
        ServiceOrderByCustomerResponse detail = orderService.getOrderById(orderId);
        List<OrderTestKitResponse> testKitDetails = orderKitService.getOrderById(orderId);
        List<OrderParticipantResponse> participantDetails = orderParticipantService.getAllParticipantsByOrderId(orderId);
        model.addAttribute("detail", detail);
        model.addAttribute("testKitDetails",testKitDetails);
        model.addAttribute("participantDetails", participantDetails);
        return "CustomerOrderService/detail";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:user/order-history";
    }
}
