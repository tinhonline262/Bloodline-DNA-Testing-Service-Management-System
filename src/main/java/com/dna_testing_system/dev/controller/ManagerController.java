package com.dna_testing_system.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for all manager dashboard related pages
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    /**
     * Display the main dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        // In a real application, you would add service statistics here
        return "manager/dashboard";
    }

    /**
     * Display the appointments management page
     */
    @GetMapping("/appointments")
    public String appointments(Model model) {
        model.addAttribute("pageTitle", "Appointments Management");
        // In a real application, you would add appointment data here
        return "manager/appointments";
    }

    /**
     * Display the test results management page
     */
    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("pageTitle", "Test Results Management");
        // In a real application, you would add test results data here
        return "manager/results";
    }

    /**
     * Display the services management page
     */
    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("pageTitle", "Services Management");
        // In a real application, you would add services data here
        return "manager/services";
    }

    /**
     * Display the customers management page
     */
    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("pageTitle", "Customer Management");
        // In a real application, you would add customer data here
        return "manager/customers";
    }

    /**
     * Display the staff management page
     */
    @GetMapping("/staff")
    public String staff(Model model) {
        model.addAttribute("pageTitle", "Staff Management");
        // In a real application, you would add staff data here
        return "manager/staff";
    }

    /**
     * Display the reports and analytics page
     */
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("pageTitle", "Reports & Analytics");
        // In a real application, you would add reporting data here
        return "manager/reports";
    }

    /**
     * Display the system settings page
     */
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "System Settings");
        // In a real application, you would add settings data here
        return "manager/settings";
    }
}
