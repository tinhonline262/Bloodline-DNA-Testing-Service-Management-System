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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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

    @GetMapping("user/list-service")
    public String orderService(Model model) {
        List<MedicalServiceResponse> medicalServiceResponse = medicalService.getAllServices();
        model.addAttribute("medicalServices", medicalServiceResponse);
        return "CustomerOrderService/list-service";
    }

    @GetMapping("user/order-service")
    public String orderServiceGet(Model model, @RequestParam("medicalServiceId") Long medicalServiceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        ServiceOrderRequestByCustomer serviceOrderRequestByCustomer = new ServiceOrderRequestByCustomer();
        serviceOrderRequestByCustomer.setUsername(currentPrincipalName);
        serviceOrderRequestByCustomer.setIdMedicalService(medicalServiceId);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("collectionTypes", CollectionType.values());
        model.addAttribute("serviceOrderRequestByCustomer", serviceOrderRequestByCustomer);
        return "CustomerOrderService/order-service"; // Assuming you have a Thymeleaf template named "order-service.html"
    }

    @PostMapping("user/order-service")
    public String orderServicePost(Model model,@ModelAttribute("serviceOrderRequestByCustomer") ServiceOrderRequestByCustomer serviceOrderRequestByCustomer) {
        ServiceOrderByCustomerResponse serviceOrderByCustomerResponse = orderService.createOrder(serviceOrderRequestByCustomer);
        model.addAttribute("serviceOrderByCustomerResponse", serviceOrderByCustomerResponse);
        return "CustomerOrderService/order-service"; // Redirect to participant information page after successful order
    }

    @GetMapping("user/participant-information")
    public String participantInformation(Model model, @RequestParam("orderId") Long orderId) {
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("orderId", orderId);
        model.addAttribute("participantRequest", new ParticipantRequest());
        return "ParticipantOrder/ParticipantInformation"; // Assuming you have a Thymeleaf template named "participant-information.html"
    }

    @PostMapping("user/participant-information")
    public String participantInformationPost(Model model, @ModelAttribute("participantRequest") ParticipantRequest participantRequest, @ModelAttribute("orderId") Long orderId) {
        orderParticipantService.createOrderParticipant(orderId, participantRequest);
        model.addAttribute("message", "Participant information saved successfully!");
        return "ParticipantOrder/ParticipantInformation"; // Redirect to the list of services after successful order
    }

    @GetMapping("/list-kit")
    public String listKits(Model model,@RequestParam("orderId") Long orderId) {
        List<TestKitResponse> testKitResponse = testKitService.GetTestKitResponseList();
        model.addAttribute("testKitResponse", testKitResponse);
        model.addAttribute("orderId", orderId); // Pass the orderId to the view
        return "OrderKit/list-kit"; // Assuming you have a Thymeleaf template named "list-kits.html"
    }

    @GetMapping("/order-kit")
    public String orderKit(@RequestParam("kitTestId") Long kitTestId, Model model, @RequestParam("orderId") Long orderId) {
        OrderTestKitRequest request = new OrderTestKitRequest();
        TestKitResponse testKit = testKitService.GetTestKitResponseById(kitTestId);
        request.setKitTestId(kitTestId);
        request.setOrderId(orderId); // Assuming you want to associate the kit order with a specific service order
        model.addAttribute("orderTestKitRequest", request);
        model.addAttribute("maxQuantity", testKit.getQuantityInStock());
        return "OrderKit/order-kit"; // Assuming you have a Thymeleaf template named "order-kit.html"
    }

    @PostMapping("/order-kit")
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
        return "redirect:/order-history";
    }
}
