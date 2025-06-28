package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.service.MedicalServiceManageService;
import com.dna_testing_system.dev.service.NotificationService;
import com.dna_testing_system.dev.service.OrderTaskManagementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for all manager dashboard related pages
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    MedicalServiceManageService medicalServiceManageService;
    NotificationService notificationService;
    OrderTaskManagementService orderTaskManagementService;


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
    @GetMapping("/order-management")
    public String orderManagement(Model model) {
        model.addAttribute("pageTitle", "Appointments Management");
        // In a real application, you would add appointment data here
        return "manager/order-management";
    }


    @GetMapping("/new-orders")
    public String newOrdersManagement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String testType,
            @RequestParam(required = false) String priority,
            Model model) {

        try {
            // Get all service orders using existing method
            List<ServiceOrderResponse> allOrders = orderTaskManagementService.getServiceOrders();

            // Filter orders for new/pending status only
            List<ServiceOrderResponse> newOrders = allOrders.stream()
                    .filter(order -> order.getOrderStatus().equals("PENDING") ||
                            order.getOrderStatus().equals("NEW") ||
                            order.getOrderStatus().equals("WAITING_FOR_PAYMENT"))
                    .toList();

            // Apply search filter
            if (search != null && !search.trim().isEmpty()) {
                newOrders = newOrders.stream()
                        .filter(order ->
                                order.getId().toString().contains(search) ||
                                        (order.getCustomerName() != null &&
                                                order.getCustomerName().toLowerCase().contains(search.toLowerCase())) ||
                                        (order.getEmail() != null &&
                                                order.getEmail().toLowerCase().contains(search.toLowerCase())))
                        .collect(Collectors.toList());
            }

            // Apply test type filter
            if (testType != null && !testType.trim().isEmpty()) {
                newOrders = newOrders.stream()
                        .filter(order -> testType.equals(order.getTestType()))
                        .collect(Collectors.toList());
            }

            // Apply priority filter
            if (priority != null && !priority.trim().isEmpty()) {
                newOrders = newOrders.stream()
                        .filter(order -> priority.equals(order.getPriority()))
                        .collect(Collectors.toList());
            }

            // Calculate pagination
            int totalElements = newOrders.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);

            List<ServiceOrderResponse> paginatedOrders = startIndex < totalElements ?
                    newOrders.subList(startIndex, endIndex) : new ArrayList<>();

            // Get available staff using existing method
            List<StaffAvailableResponse> availableStaff = orderTaskManagementService.getStaffAvailable();

            // Calculate statistics
            Map<String, Integer> statistics = calculateStatistics(allOrders, availableStaff);

            // Create search parameters object
            Map<String, String> searchParams = new HashMap<>();
            searchParams.put("search", search != null ? search : "");
            searchParams.put("testType", testType != null ? testType : "");
            searchParams.put("priority", priority != null ? priority : "");

            // Add all data to model
            model.addAttribute("newOrders", paginatedOrders);
            model.addAttribute("availableStaff", availableStaff);
            model.addAttribute("statistics", statistics);
            model.addAttribute("searchParams", searchParams);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);

            log.info("Loaded {} new orders for page {}", paginatedOrders.size(), page);

        } catch (Exception e) {
            log.error("Error loading new orders: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load orders. Please try again.");
            model.addAttribute("newOrders", new ArrayList<>());
            model.addAttribute("availableStaff", new ArrayList<>());
            model.addAttribute("statistics", getDefaultStatistics());
        }

        return "manager/new-order-management";
    }

    @PostMapping("/assign-staff")
    public String assignStaff(
            @RequestParam Long orderId,
            @RequestParam Long staffId,
            @RequestParam String assignmentType,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes) {

        try {
            StaffAvailableRequest staffRequest = new StaffAvailableRequest();
            staffRequest.setStaffId(staffId);
            staffRequest.setNotes(notes);

            // Use existing sampleCollectionTaskAssignment method
            orderTaskManagementService.sampleCollectionTaskAssignment(orderId, staffRequest);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Staff successfully assigned to order #" + orderId);

            log.info("Staff {} assigned to order {}", staffId, orderId);

        } catch (Exception e) {
            log.error("Error assigning staff: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to assign staff. Please try again.");
        }

        return "redirect:/manager/new-orders";
    }

    private Map<String, Integer> calculateStatistics(List<ServiceOrderResponse> allOrders,
                                                     List<StaffAvailableResponse> availableStaff) {
        Map<String, Integer> stats = new HashMap<>();

        // Count new orders
        int newOrdersCount = (int) allOrders.stream()
                .filter(order -> order.getOrderStatus().equals("PENDING") ||
                        order.getOrderStatus().equals("NEW") ||
                        order.getOrderStatus().equals("WAITING_FOR_PAYMENT"))
                .count();

        // Count assigned today
        LocalDate today = LocalDate.now();
        int assignedTodayCount = (int) allOrders.stream()
                .filter(order -> order.getOrderStatus().equals("CONFIRMED") &&
                        order.getOrderDate() != null &&
                        order.getOrderDate().toLocalDate().equals(today))
                .count();

        stats.put("newOrdersCount", newOrdersCount);
        stats.put("availableStaffCount", availableStaff.size());
        stats.put("assignedTodayCount", assignedTodayCount);
        stats.put("pendingAssignmentCount", newOrdersCount);

        return stats;
    }

    private Map<String, Integer> getDefaultStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("newOrdersCount", 0);
        stats.put("availableStaffCount", 0);
        stats.put("assignedTodayCount", 0);
        stats.put("pendingAssignmentCount", 0);
        return stats;
    }


    /**
     * Display the test results management page
     */
    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("pageTitle", "Test Results Management");
        // In a real application, you would add test results data here
        return "manager/results";
    }    /**
     * Display the services management page
     */
    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("pageTitle", "Services Management");
        model.addAttribute("services", medicalServiceManageService.getAllServices());
        model.addAttribute("serviceTypes", medicalServiceManageService.getAllServiceTypes());
        model.addAttribute("serviceFeatures", medicalServiceManageService.getAllServiceFeatures());
        model.addAttribute("service", medicalServiceManageService);
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
