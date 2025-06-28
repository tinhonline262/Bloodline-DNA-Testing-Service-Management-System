package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDashboardController {

    // Thêm UserProfileService để có thể lấy thông tin user
    UserProfileService userProfileService;

    // Enum để định nghĩa các trạng thái của yêu cầu
    public enum RequestStatus {
        COMPLETED, IN_PROGRESS, PENDING, CANCELLED
    }

    // Record để chứa dữ liệu thống kê cho các thẻ
    public record Statistics(int requestsSent, int requestsInProgress, int upcomingAppointments, int resultsAvailable) {}

    // Record để chứa dữ liệu cho mỗi yêu cầu trong bảng
    public record TestRequest(String id, String code, LocalDate submissionDate, String testType, RequestStatus status) {
        // Phương thức để lấy văn bản trạng thái tiếng Việt
        public String getStatusText() {
            return switch (status) {
                case COMPLETED -> "Hoàn thành";
                case IN_PROGRESS -> "Đang xử lý";
                case PENDING -> "Chờ xử lý";
                case CANCELLED -> "Đã hủy";
            };
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // --- BẮT ĐẦU PHẦN QUAN TRỌNG NHẤT ---
        // Lấy thông tin user hiện tại cho layout
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {

            String currentPrincipalName = authentication.getName();
            UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
            model.addAttribute("userProfile", userProfile);
        }
        // --- KẾT THÚC PHẦN QUAN TRỌNG NHẤT ---

        // 1. Tạo dữ liệu mẫu cho các thẻ thống kê
        var stats = new Statistics(24, 3, 2, 8);

        // 2. Tạo danh sách các yêu cầu xét nghiệm gần đây
        var recentRequests = List.of(
                new TestRequest("req1", "XN001", LocalDate.now().minusDays(1), "Xét nghiệm máu tổng quát", RequestStatus.COMPLETED),
                new TestRequest("req2", "XN002", LocalDate.now(), "Xét nghiệm nước tiểu", RequestStatus.IN_PROGRESS),
                new TestRequest("req3", "XN003", LocalDate.now().minusDays(2), "Xét nghiệm sinh hóa", RequestStatus.PENDING),
                new TestRequest("req4", "XN004", LocalDate.now().minusDays(3), "Xét nghiệm vi sinh", RequestStatus.COMPLETED),
                new TestRequest("req5", "XN005", LocalDate.now().minusDays(4), "Xét nghiệm hormone", RequestStatus.CANCELLED)
        );

        // 3. Đưa dữ liệu vào Model để Thymeleaf có thể truy cập
        model.addAttribute("statistics", stats);
        model.addAttribute("recentRequests", recentRequests);

        // 4. Trả về view "user/dashboard"
        return "user/dashboard";
    }
}