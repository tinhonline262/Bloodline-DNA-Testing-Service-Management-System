package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.MedicalServiceResponse;
import com.dna_testing_system.dev.dto.response.ServiceOrderResponse;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.repository.UserRepository;
import com.dna_testing_system.dev.service.MedicalServiceManageService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardController extends BaseController {

    MedicalServiceManageService medicalServiceManageService;
    OrderTaskManagementService orderTaskManagementService;
    UserProfileService userProfileService;
    UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        addUserProfileToModel(model);
        model.addAttribute("pageTitle", "Admin Dashboard");

        try {
            // Get admin dashboard statistics
            AdminDashboardStats dashboardStats = getAdminDashboardStatistics();
            model.addAttribute("dashboardStats", dashboardStats);

            // Get recent system activities
            List<SystemActivity> recentActivities = getRecentSystemActivities();
            model.addAttribute("recentActivities", recentActivities);

            // Get system alerts
            List<SystemAlert> systemAlerts = getSystemAlerts();
            model.addAttribute("systemAlerts", systemAlerts);

            // Get monthly statistics
            MonthlySystemStats monthlyStats = getMonthlySystemStatistics();
            model.addAttribute("monthlyStats", monthlyStats);

            // Get performance metrics
            SystemPerformanceMetrics performanceMetrics = getSystemPerformanceMetrics();
            model.addAttribute("performanceMetrics", performanceMetrics);

        } catch (Exception e) {
            log.error("Error loading admin dashboard data: ", e);
            model.addAttribute("error", "Unable to load dashboard data");
        }
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        addUserProfileToModel(model);
        model.addAttribute("pageTitle", "User Management");
        
        try {
            // Get all user profiles
            List<UserProfileResponse> users = userProfileService.getUserProfiles();
            model.addAttribute("users", users);

            // Get user statistics
            UserManagementStats userStats = getUserManagementStatistics();
            model.addAttribute("userStats", userStats);

            // Get user activity data
            List<UserActivity> userActivities = getRecentUserActivities();
            model.addAttribute("userActivities", userActivities);

        } catch (Exception e) {
            log.error("Error loading user management data: ", e);
            model.addAttribute("error", "Unable to load user data");
        }
        
        return "admin/users";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        addUserProfileToModel(model);
        model.addAttribute("pageTitle", "System Settings");
        
        try {
            // Get system configuration
            SystemConfiguration systemConfig = getSystemConfiguration();
            model.addAttribute("systemConfig", systemConfig);

        } catch (Exception e) {
            log.error("Error loading system settings data: ", e);
            model.addAttribute("error", "Unable to load settings data");
        }
        
        return "admin/settings";
    }

    @GetMapping("/logs")
    public String logs(Model model) {
        addUserProfileToModel(model);
        model.addAttribute("pageTitle", "System Logs");
        
        try {
            // Get system logs
            List<SystemLog> systemLogs = getSystemLogs();
            model.addAttribute("systemLogs", systemLogs);

            // Get log statistics
            LogStatistics logStats = getLogStatistics();
            model.addAttribute("logStats", logStats);

        } catch (Exception e) {
            log.error("Error loading system logs data: ", e);
            model.addAttribute("error", "Unable to load logs data");
        }
        
        return "admin/logs";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        addUserProfileToModel(model);
        model.addAttribute("pageTitle", "Analytics");
        
        try {
            // Get analytics data
            SystemAnalytics analyticsData = getSystemAnalytics();
            model.addAttribute("analyticsData", analyticsData);

            // Get performance trends
            List<PerformanceTrend> performanceTrends = getPerformanceTrends();
            model.addAttribute("performanceTrends", performanceTrends);

        } catch (Exception e) {
            log.error("Error loading analytics data: ", e);
            model.addAttribute("error", "Unable to load analytics data");
        }
        
        return "admin/analytics";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        addUserProfileToModel(model);
        model.addAttribute("pageTitle", "Admin Profile");
        return "admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("userEditProfile") UserProfileRequest userProfile,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse existingProfile = userProfileService.getUserProfile(currentPrincipalName);

        // Handle file upload
        if (file != null && !file.getOriginalFilename().equals("")) {
            String uploadsDir = "uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadsDir + fileName);

            try {
                Files.createDirectories(Paths.get(uploadsDir));
                file.transferTo(path);
                String imageUrl = "/uploads/" + fileName;
                userProfile.setProfileImageUrl(imageUrl);
            } catch (Exception e) {
                log.error("Error uploading profile image: ", e);
                userProfile.setProfileImageUrl(existingProfile.getProfileImageUrl());
            }
        } else {
            // Keep existing image if no new upload
            userProfile.setProfileImageUrl(existingProfile.getProfileImageUrl());
        }

        // Keep existing dateOfBirth if not changed
        if (userProfile.getDateOfBirth() == null) {
            userProfile.setDateOfBirth(existingProfile.getDateOfBirth());
        }

        userProfileService.updateUserProfile(currentPrincipalName, userProfile);

        // Refresh user information for header
        UserProfileResponse updatedProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", updatedProfile);

        return "redirect:/admin/profile";
    }

    // Admin user management operations (server-side only)

    @PostMapping("/users/{userId}/toggle-status")
    public String toggleUserStatus(@PathVariable String userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
            
            String status = user.getIsActive() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("successMessage", 
                "User " + user.getUsername() + " has been " + status + " successfully.");
            
        } catch (Exception e) {
            log.error("Error toggling user status: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating user status: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable String userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/admin/users";
            }
            
            String username = user.getUsername();
            userRepository.deleteById(userId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "User " + username + " has been deleted successfully.");
            
        } catch (Exception e) {
            log.error("Error deleting user: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }

    @PostMapping("/settings/update")
    public String updateSystemSettings(@ModelAttribute SystemConfiguration config, 
                                     RedirectAttributes redirectAttributes) {
        try {
            // In a real application, you would save this to a database or configuration file
            log.info("System settings updated: {}", config);
            redirectAttributes.addFlashAttribute("successMessage", 
                "System settings updated successfully");
        } catch (Exception e) {
            log.error("Error updating system settings: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating settings: " + e.getMessage());
        }
        
        return "redirect:/admin/settings";
    }

    // Helper methods for admin-specific statistics and data

    private AdminDashboardStats getAdminDashboardStatistics() {
        try {
            List<User> allUsers = userRepository.findAll();
            List<MedicalServiceResponse> services = medicalServiceManageService.getAllServices();
            List<ServiceOrderResponse> orders = orderTaskManagementService.getServiceOrders();

            long totalUsers = allUsers.size();
            long activeUsers = allUsers.stream()
                    .filter(User::getIsActive)
                    .count();
            long totalServices = services.size();
            long totalOrders = orders.size();
            long pendingOrders = orders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("PENDING"))
                    .count();

            // Calculate revenue (simplified)
            double totalRevenue = orders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("COMPLETED"))
                    .mapToDouble(order -> 299.0) // Default price, should be from service
                    .sum();

            return new AdminDashboardStats(totalUsers, activeUsers, totalServices, 
                    totalOrders, pendingOrders, totalRevenue);

        } catch (Exception e) {
            log.error("Error getting admin dashboard statistics: ", e);
            return new AdminDashboardStats(0, 0, 0, 0, 0, 0.0);
        }
    }

    private List<SystemActivity> getRecentSystemActivities() {
        List<SystemActivity> activities = new ArrayList<>();

        try {
            // Get recent orders and convert to activities
            List<ServiceOrderResponse> recentOrders = orderTaskManagementService.getServiceOrders()
                    .stream()
                    .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());

            for (ServiceOrderResponse order : recentOrders) {
                String timeAgo = getTimeAgo(order.getCreatedAt());
                activities.add(new SystemActivity(
                        "ORDER",
                        "New Order Created",
                        "Order #" + order.getId() + " created by " + order.getCustomerName(),
                        timeAgo,
                        "success"
                ));
            }

            // Add other system activities (user registrations, service updates, etc.)
            List<User> recentUsers = userRepository.findAll().stream()
                    .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
                    .limit(5)
                    .collect(Collectors.toList());

            for (User user : recentUsers) {
                String timeAgo = getTimeAgo(user.getCreatedAt());
                activities.add(new SystemActivity(
                        "USER",
                        "New User Registered",
                        "User " + user.getUsername() + " joined the system",
                        timeAgo,
                        "info"
                ));
            }

        } catch (Exception e) {
            log.error("Error getting recent system activities: ", e);
        }

        return activities.stream()
                .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
                .limit(20)
                .collect(Collectors.toList());
    }

    private List<SystemAlert> getSystemAlerts() {
        List<SystemAlert> alerts = new ArrayList<>();

        try {
            // Check for system issues
            List<ServiceOrderResponse> orders = orderTaskManagementService.getServiceOrders();
            
            // Alert for pending orders
            long pendingOrders = orders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("PENDING"))
                    .count();
            
            if (pendingOrders > 10) {
                alerts.add(new SystemAlert(
                        "warning",
                        "High Pending Orders",
                        pendingOrders + " orders are pending processing",
                        "high"
                ));
            }

            // Alert for inactive users
            List<User> allUsers = userRepository.findAll();
            long inactiveUsers = allUsers.stream()
                    .filter(user -> !user.getIsActive())
                    .count();
            
            if (inactiveUsers > 5) {
                alerts.add(new SystemAlert(
                        "info",
                        "Inactive Users",
                        inactiveUsers + " users are currently inactive",
                        "medium"
                ));
            }

            // System health alert (simulated)
            alerts.add(new SystemAlert(
                    "success",
                    "System Running Normally",
                    "All services are operational",
                    "low"
            ));

        } catch (Exception e) {
            log.error("Error getting system alerts: ", e);
            alerts.add(new SystemAlert(
                    "error",
                    "System Error",
                    "Unable to retrieve system status",
                    "critical"
            ));
        }

        return alerts;
    }

    private MonthlySystemStats getMonthlySystemStatistics() {
        try {
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            
            List<ServiceOrderResponse> orders = orderTaskManagementService.getServiceOrders();
            List<ServiceOrderResponse> monthlyOrders = orders.stream()
                    .filter(order -> order.getCreatedAt().isAfter(startOfMonth))
                    .collect(Collectors.toList());

            List<User> users = userRepository.findAll();
            List<User> monthlyUsers = users.stream()
                    .filter(user -> user.getCreatedAt().isAfter(startOfMonth))
                    .collect(Collectors.toList());

            long newOrders = monthlyOrders.size();
            long newUsers = monthlyUsers.size();
            double revenue = monthlyOrders.stream()
                    .filter(order -> order.getOrderStatus().name().equals("COMPLETED"))
                    .mapToDouble(order -> 299.0)
                    .sum();

            return new MonthlySystemStats(newOrders, newUsers, revenue);

        } catch (Exception e) {
            log.error("Error getting monthly system statistics: ", e);
            return new MonthlySystemStats(0, 0, 0.0);
        }
    }

    private SystemPerformanceMetrics getSystemPerformanceMetrics() {
        // Simulated performance metrics
        return new SystemPerformanceMetrics(
                95.5, // CPU usage percentage
                68.2, // Memory usage percentage
                42.1, // Disk usage percentage
                99.9, // System uptime percentage
                350   // Active sessions
        );
    }

    private UserManagementStats getUserManagementStatistics() {
        try {
            List<User> allUsers = userRepository.findAll();
            
            long totalUsers = allUsers.size();
            long activeUsers = allUsers.stream().filter(User::getIsActive).count();
            long inactiveUsers = totalUsers - activeUsers;
            
            // Get role distribution
            long adminUsers = userRepository.findUsersByRoleName("ADMIN").size();
            long managerUsers = userRepository.findUsersByRoleName("MANAGER").size();
            long staffUsers = userRepository.findUsersByRoleName("STAFF").size();
            long customerUsers = userRepository.findUsersByRoleName("CUSTOMER").size();

            return new UserManagementStats(totalUsers, activeUsers, inactiveUsers,
                    adminUsers, managerUsers, staffUsers, customerUsers);

        } catch (Exception e) {
            log.error("Error getting user management statistics: ", e);
            return new UserManagementStats(0, 0, 0, 0, 0, 0, 0);
        }
    }

    private List<UserActivity> getRecentUserActivities() {
        List<UserActivity> activities = new ArrayList<>();
        
        try {
            // Simulated user activities
            List<User> recentUsers = userRepository.findAll().stream()
                    .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());

            for (User user : recentUsers) {
                activities.add(new UserActivity(
                        user.getUsername(),
                        "Last Login",
                        getTimeAgo(user.getCreatedAt()),
                        user.getIsActive() ? "Online" : "Offline"
                ));
            }

        } catch (Exception e) {
            log.error("Error getting recent user activities: ", e);
        }
        
        return activities;
    }

    private SystemConfiguration getSystemConfiguration() {
        // Simulated system configuration
        return new SystemConfiguration(
                "Bloodline DNA Testing System",
                "1.0.0",
                "admin@bloodline.com",
                "Production",
                true,
                true,
                "smtp.bloodline.com"
        );
    }

    private List<SystemLog> getSystemLogs() {
        List<SystemLog> logs = new ArrayList<>();
        
        // Simulated system logs
        logs.add(new SystemLog("INFO", "System startup completed", "System", LocalDateTime.now().minusHours(2)));
        logs.add(new SystemLog("WARN", "High memory usage detected", "Performance", LocalDateTime.now().minusMinutes(30)));
        logs.add(new SystemLog("ERROR", "Database connection timeout", "Database", LocalDateTime.now().minusMinutes(15)));
        logs.add(new SystemLog("INFO", "User authentication successful", "Security", LocalDateTime.now().minusMinutes(5)));
        
        return logs;
    }

    private LogStatistics getLogStatistics() {
        return new LogStatistics(156, 23, 8, 2);
    }

    private SystemAnalytics getSystemAnalytics() {
        return new SystemAnalytics(
                1250,  // Total page views
                89.5,  // Conversion rate
                156,   // Active sessions
                34.2   // Bounce rate
        );
    }

    private List<PerformanceTrend> getPerformanceTrends() {
        List<PerformanceTrend> trends = new ArrayList<>();
        
        // Simulated performance trends for the last 7 days
        for (int i = 6; i >= 0; i--) {
            LocalDateTime date = LocalDateTime.now().minusDays(i);
            trends.add(new PerformanceTrend(
                    date.format(DateTimeFormatter.ofPattern("MM/dd")),
                    95.0 + (Math.random() * 5), // Response time
                    Math.random() * 100,        // CPU usage
                    Math.random() * 100         // Memory usage
            ));
        }
        
        return trends;
    }

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

    // Inner classes for admin-specific data structures

    public static class AdminDashboardStats {
        private final long totalUsers;
        private final long activeUsers;
        private final long totalServices;
        private final long totalOrders;
        private final long pendingOrders;
        private final double totalRevenue;

        public AdminDashboardStats(long totalUsers, long activeUsers, long totalServices, 
                                 long totalOrders, long pendingOrders, double totalRevenue) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.totalServices = totalServices;
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.totalRevenue = totalRevenue;
        }

        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public long getTotalServices() { return totalServices; }
        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public double getTotalRevenue() { return totalRevenue; }
    }

    public static class SystemActivity {
        private final String type;
        private final String title;
        private final String description;
        private final String timeAgo;
        private final String severity;
        private final LocalDateTime timestamp;

        public SystemActivity(String type, String title, String description, String timeAgo, String severity) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.timeAgo = timeAgo;
            this.severity = severity;
            this.timestamp = LocalDateTime.now();
        }

        // Getters
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getTimeAgo() { return timeAgo; }
        public String getSeverity() { return severity; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    public static class SystemAlert {
        private final String type;
        private final String title;
        private final String message;
        private final String priority;

        public SystemAlert(String type, String title, String message, String priority) {
            this.type = type;
            this.title = title;
            this.message = message;
            this.priority = priority;
        }

        // Getters
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getPriority() { return priority; }
    }

    public static class MonthlySystemStats {
        private final long newOrders;
        private final long newUsers;
        private final double revenue;

        public MonthlySystemStats(long newOrders, long newUsers, double revenue) {
            this.newOrders = newOrders;
            this.newUsers = newUsers;
            this.revenue = revenue;
        }

        // Getters
        public long getNewOrders() { return newOrders; }
        public long getNewUsers() { return newUsers; }
        public double getRevenue() { return revenue; }
    }

    public static class SystemPerformanceMetrics {
        private final double cpuUsage;
        private final double memoryUsage;
        private final double diskUsage;
        private final double uptime;
        private final int activeSessions;

        public SystemPerformanceMetrics(double cpuUsage, double memoryUsage, double diskUsage, 
                                      double uptime, int activeSessions) {
            this.cpuUsage = cpuUsage;
            this.memoryUsage = memoryUsage;
            this.diskUsage = diskUsage;
            this.uptime = uptime;
            this.activeSessions = activeSessions;
        }

        // Getters
        public double getCpuUsage() { return cpuUsage; }
        public double getMemoryUsage() { return memoryUsage; }
        public double getDiskUsage() { return diskUsage; }
        public double getUptime() { return uptime; }
        public int getActiveSessions() { return activeSessions; }
    }

    public static class UserManagementStats {
        private final long totalUsers;
        private final long activeUsers;
        private final long inactiveUsers;
        private final long adminUsers;
        private final long managerUsers;
        private final long staffUsers;
        private final long customerUsers;

        public UserManagementStats(long totalUsers, long activeUsers, long inactiveUsers,
                                 long adminUsers, long managerUsers, long staffUsers, long customerUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.inactiveUsers = inactiveUsers;
            this.adminUsers = adminUsers;
            this.managerUsers = managerUsers;
            this.staffUsers = staffUsers;
            this.customerUsers = customerUsers;
        }

        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public long getInactiveUsers() { return inactiveUsers; }
        public long getAdminUsers() { return adminUsers; }
        public long getManagerUsers() { return managerUsers; }
        public long getStaffUsers() { return staffUsers; }
        public long getCustomerUsers() { return customerUsers; }
    }

    public static class UserActivity {
        private final String username;
        private final String activity;
        private final String timeAgo;
        private final String status;

        public UserActivity(String username, String activity, String timeAgo, String status) {
            this.username = username;
            this.activity = activity;
            this.timeAgo = timeAgo;
            this.status = status;
        }

        // Getters
        public String getUsername() { return username; }
        public String getActivity() { return activity; }
        public String getTimeAgo() { return timeAgo; }
        public String getStatus() { return status; }
    }

    public static class SystemConfiguration {
        private final String systemName;
        private final String version;
        private final String adminEmail;
        private final String environment;
        private final boolean maintenanceMode;
        private final boolean emailNotifications;
        private final String smtpServer;

        public SystemConfiguration(String systemName, String version, String adminEmail,
                                 String environment, boolean maintenanceMode, 
                                 boolean emailNotifications, String smtpServer) {
            this.systemName = systemName;
            this.version = version;
            this.adminEmail = adminEmail;
            this.environment = environment;
            this.maintenanceMode = maintenanceMode;
            this.emailNotifications = emailNotifications;
            this.smtpServer = smtpServer;
        }

        // Getters
        public String getSystemName() { return systemName; }
        public String getVersion() { return version; }
        public String getAdminEmail() { return adminEmail; }
        public String getEnvironment() { return environment; }
        public boolean isMaintenanceMode() { return maintenanceMode; }
        public boolean isEmailNotifications() { return emailNotifications; }
        public String getSmtpServer() { return smtpServer; }
    }

    public static class SystemLog {
        private final String level;
        private final String message;
        private final String source;
        private final LocalDateTime timestamp;

        public SystemLog(String level, String message, String source, LocalDateTime timestamp) {
            this.level = level;
            this.message = message;
            this.source = source;
            this.timestamp = timestamp;
        }

        // Getters
        public String getLevel() { return level; }
        public String getMessage() { return message; }
        public String getSource() { return source; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    public static class LogStatistics {
        private final int infoCount;
        private final int warningCount;
        private final int errorCount;
        private final int criticalCount;

        public LogStatistics(int infoCount, int warningCount, int errorCount, int criticalCount) {
            this.infoCount = infoCount;
            this.warningCount = warningCount;
            this.errorCount = errorCount;
            this.criticalCount = criticalCount;
        }

        // Getters
        public int getInfoCount() { return infoCount; }
        public int getWarningCount() { return warningCount; }
        public int getErrorCount() { return errorCount; }
        public int getCriticalCount() { return criticalCount; }
    }

    public static class SystemAnalytics {
        private final long pageViews;
        private final double conversionRate;
        private final int activeSessions;
        private final double bounceRate;

        public SystemAnalytics(long pageViews, double conversionRate, int activeSessions, double bounceRate) {
            this.pageViews = pageViews;
            this.conversionRate = conversionRate;
            this.activeSessions = activeSessions;
            this.bounceRate = bounceRate;
        }

        // Getters
        public long getPageViews() { return pageViews; }
        public double getConversionRate() { return conversionRate; }
        public int getActiveSessions() { return activeSessions; }
        public double getBounceRate() { return bounceRate; }
    }

    public static class PerformanceTrend {
        private final String date;
        private final double responseTime;
        private final double cpuUsage;
        private final double memoryUsage;

        public PerformanceTrend(String date, double responseTime, double cpuUsage, double memoryUsage) {
            this.date = date;
            this.responseTime = responseTime;
            this.cpuUsage = cpuUsage;
            this.memoryUsage = memoryUsage;
        }

        // Getters
        public String getDate() { return date; }
        public double getResponseTime() { return responseTime; }
        public double getCpuUsage() { return cpuUsage; }
        public double getMemoryUsage() { return memoryUsage; }
    }
}
