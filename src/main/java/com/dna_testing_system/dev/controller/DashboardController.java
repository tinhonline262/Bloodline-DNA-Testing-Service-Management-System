package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.StaffAvailableRequest;
import com.dna_testing_system.dev.service.MedicalServiceManageService;
import com.dna_testing_system.dev.service.NotificationService;
import com.dna_testing_system.dev.service.OrderTaskManagementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for all manager dashboard related pages
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    MedicalServiceManageService medicalServiceManageService;
    NotificationService notificationService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        // In a real application, you would add service statistics here
        return "manager/dashboard";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("pageTitle", "Services Management");
        model.addAttribute("services", medicalServiceManageService.getAllServices());
        model.addAttribute("serviceTypes", medicalServiceManageService.getAllServiceTypes());
        model.addAttribute("serviceFeatures", medicalServiceManageService.getAllServiceFeatures());
        model.addAttribute("service", medicalServiceManageService);
        return "manager/services";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("pageTitle", "Customer Management");
        // In a real application, you would add customer data here
        return "manager/customers";
    }


    @GetMapping("/staff")
    public String staff(Model model) {
        model.addAttribute("pageTitle", "Staff Management");
        // In a real application, you would add staff data here
        return "manager/staff";
    }


    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("pageTitle", "Reports & Analytics");
        // In a real application, you would add reporting data here
        return "manager/reports";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "System Settings");
        // In a real application, you would add settings data here
        return "manager/settings";
    }
}
