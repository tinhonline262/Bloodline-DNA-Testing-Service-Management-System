package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.MedicalServiceResponse;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.service.MedicalServiceManageService;
import com.dna_testing_system.dev.service.NotificationService;
import com.dna_testing_system.dev.service.OrderTaskManagementService;
import com.dna_testing_system.dev.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManagerDashboardController extends BaseController {

    MedicalServiceManageService medicalServiceManageService;
    OrderTaskManagementService orderTaskManagementService;
    NotificationService notificationService;
    UserProfileService userProfileService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        // In a real application, you would add service statistics here
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {

            String currentPrincipalName = authentication.getName();
            UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
            model.addAttribute("userProfile", userProfile);
        }

        try {
            // Get dashboard statistics
            DashboardStats dashboardStats = getDashboardStatistics();
            model.addAttribute("dashboardStats", dashboardStats);

            // Get recent orders (limit to 5 for dashboard)
            List<ServiceOrderResponse> allOrders = orderTaskManagementService.getServiceOrders();
            List<ServiceOrderResponse> recentOrders = allOrders.stream()
                    .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                    .limit(5)
                    .collect(Collectors.toList());
            model.addAttribute("recentOrders", recentOrders);

            // Get recent activities
            List<RecentActivity> recentActivities = getRecentActivities();
            model.addAttribute("recentActivities", recentActivities);

            // Get monthly statistics
            MonthlyStats monthlyStats = getMonthlyStatistics();
            model.addAttribute("monthlyStats", monthlyStats);

            model.addAttribute("pageTitle", "Dashboard");

        } catch (Exception e) {
            log.error("Error loading dashboard data: ", e);
            model.addAttribute("error", "Unable to load dashboard data");
        }
        return "manager/dashboard";
    }

    @GetMapping("/services")
    public String services(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {

            String currentPrincipalName = authentication.getName();
            UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
            model.addAttribute("userProfile", userProfile);
        }
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


    @GetMapping("/profile")  // URL sẽ là /user/profile
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", userProfile);
        return "user/profile";
    }

    @GetMapping("/profile/update")
    public String showUpdateProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse userProfileData = userProfileService.getUserProfile(currentPrincipalName);

        // Dữ liệu cũ chỉ có 1 dòng này
        model.addAttribute("userEditProfile", userProfileData);

        model.addAttribute("userProfile", userProfileData);

        return "user/edit-profile"; //
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("userEditProfile") UserProfileRequest userProfile,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                Model model) { // Thêm Model
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse existingProfile = userProfileService.getUserProfile(currentPrincipalName);


        // Xử lý file upload
        if (file != null && !file.getOriginalFilename().equals("")) {
            String uploadsDir = "uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadsDir + fileName);

            try {

                Files.createDirectories(Paths.get(uploadsDir));
                file.transferTo(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String imageUrl = "/uploads/" + fileName;
            userProfile.setProfileImageUrl(imageUrl);

        } else {
            // Giữ nguyên ảnh cũ nếu không upload ảnh mới
            userProfile.setProfileImageUrl(existingProfile.getProfileImageUrl());
        }

        // Giữ nguyên dateOfBirth nếu không có thay đổi
        if (userProfile.getDateOfBirth() == null) {
            userProfile.setDateOfBirth(existingProfile.getDateOfBirth());
        }


        userProfileService.updateUserProfile(currentPrincipalName, userProfile);

        // Refresh lại thông tin user cho header
        UserProfileResponse updatedProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", updatedProfile);

        return "redirect:/manager/profile";
    }


    private DashboardStats getDashboardStatistics() {
        try {
            List<MedicalServiceResponse> services = medicalServiceManageService.getAllServices();
            List<ServiceOrderResponse> orders = orderTaskManagementService.getServiceOrders();

            long totalServices = services.size();
            long activeOrders = orders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("IN_PROGRESS") ||
                            order.getOrderStatus().name().equals("PENDING"))
                    .count();
            long pendingResults = orders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("PENDING"))
                    .count();

            // You'll need to implement customer counting based on your user management
            long totalCustomers = orders.stream()
                    .map(ServiceOrderResponse::getCustomerName)
                    .distinct()
                    .count();

            return new DashboardStats(totalServices, activeOrders, pendingResults, totalCustomers);

        } catch (Exception e) {
            log.error("Error getting dashboard statistics: ", e);
            return new DashboardStats(0, 0, 0, 0);
        }
    }

    // Helper method to get recent activities
    private List<RecentActivity> getRecentActivities() {
        List<RecentActivity> activities = new ArrayList<>();

        try {
            // Get recent orders and convert to activities
            List<ServiceOrderResponse> recentOrders = orderTaskManagementService.getServiceOrders()
                    .stream()
                    .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());

            for (ServiceOrderResponse order : recentOrders) {
                String timeAgo = getTimeAgo(order.getCreatedAt());
                activities.add(new RecentActivity(
                        "ORDER",
                        "New order received",
                        "Order #" + order.getId() + " from " + order.getCustomerName(),
                        timeAgo
                ));
            }

        } catch (Exception e) {
            log.error("Error getting recent activities: ", e);
        }

        return activities;
    }

    // Helper method to get monthly statistics
    private MonthlyStats getMonthlyStatistics() {
        try {
            List<ServiceOrderResponse> orders = orderTaskManagementService.getServiceOrders();
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

            List<ServiceOrderResponse> monthlyOrders = orders.stream()
                    .filter(order -> order.getCreatedAt().isAfter(startOfMonth))
                    .collect(Collectors.toList());

            long newOrders = monthlyOrders.size();
            long completedOrders = monthlyOrders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("COMPLETED"))
                    .count();

            // Calculate revenue based on your pricing logic
            double revenue = monthlyOrders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("COMPLETED"))
                    .mapToDouble(order -> 299.0) // Default price, you should get this from service
                    .sum();

            return new MonthlyStats(newOrders, completedOrders, revenue);

        } catch (Exception e) {
            log.error("Error getting monthly statistics: ", e);
            return new MonthlyStats(0, 0, 0.0);
        }
    }

    // Helper method to calculate time ago
    private String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "Unknown";

        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);

        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minutes ago";

        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) return hours + " hours ago";

        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days < 7) return days + " days ago";

        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Inner classes for data structure
    public static class DashboardStats {
        private final long totalServices;
        private final long activeOrders;
        private final long pendingResults;
        private final long totalCustomers;

        public DashboardStats(long totalServices, long activeOrders, long pendingResults, long totalCustomers) {
            this.totalServices = totalServices;
            this.activeOrders = activeOrders;
            this.pendingResults = pendingResults;
            this.totalCustomers = totalCustomers;
        }

        // Getters
        public long getTotalServices() { return totalServices; }
        public long getActiveOrders() { return activeOrders; }
        public long getPendingResults() { return pendingResults; }
        public long getTotalCustomers() { return totalCustomers; }
    }

    public static class RecentActivity {
        private final String type;
        private final String title;
        private final String description;
        private final String timeAgo;

        public RecentActivity(String type, String title, String description, String timeAgo) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.timeAgo = timeAgo;
        }

        // Getters
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getTimeAgo() { return timeAgo; }
    }

    public static class MonthlyStats {
        private final long newOrders;
        private final long completedOrders;
        private final double revenue;

        public MonthlyStats(long newOrders, long completedOrders, double revenue) {
            this.newOrders = newOrders;
            this.completedOrders = completedOrders;
            this.revenue = revenue;
        }

        // Getters
        public long getNewOrders() { return newOrders; }
        public long getCompletedOrders() { return completedOrders; }
        public double getRevenue() { return revenue; }
    }
}
