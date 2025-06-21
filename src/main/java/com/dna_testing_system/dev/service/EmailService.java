package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.Booking;
import com.dna_testing_system.dev.entity.Customer;
import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.entity.Participant;
import com.dna_testing_system.dev.repository.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ParticipantRepository participantRepository;

    public void sendBookingConfirmationEmail(Booking booking) {
        Customer customer = null; // Khai báo customer ở ngoài try-catch block
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            customer = booking.getCustomer(); // Gán giá trị cho customer
            if (customer == null || customer.getEmail() == null) {
                logger.error("Customer or email is null for booking id: {}", booking.getId());
                throw new IllegalArgumentException("Customer or email cannot be null");
            }

            DnaService service = booking.getService();
            List<Participant> participants = participantRepository.findByBookingId(booking.getId());

            // Thiết lập thông tin email
            helper.setTo(customer.getEmail());
            helper.setSubject("Xác Nhận Đặt Lịch Xét Nghiệm DNA - DNACare");
            helper.setFrom("your-email@gmail.com"); // Thay bằng email của bạn

            // Tạo nội dung email dạng HTML
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html><body style='font-family: Arial, sans-serif;'>");
            emailContent.append("<h2>Xác Nhận Đặt Lịch Xét Nghiệm DNA</h2>");
            emailContent.append("<p>Kính gửi <strong>").append(customer.getName()).append("</strong>,</p>");
            emailContent.append("<p>Cảm ơn bạn đã sử dụng dịch vụ của DNACare. Dưới đây là thông tin đặt lịch của bạn:</p>");
            emailContent.append("<table style='border-collapse: collapse; width: 100%;'>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Họ và tên:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(customer.getName()).append("</td></tr>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Email:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(customer.getEmail()).append("</td></tr>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Số điện thoại:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(customer.getPhone()).append("</td></tr>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Dịch vụ:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(service.getServiceName()).append("</td></tr>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Giá:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(String.format("%,.2f VNĐ", service.getPrice())).append("</td></tr>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Phương thức thu mẫu:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(booking.getCollectionMethod().getDisplayName()).append("</td></tr>");
            emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Ngày giờ hẹn:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(booking.getAppointmentDate().format(DATE_TIME_FORMATTER)).append("</td></tr>");
            if (customer.getMessage() != null && !customer.getMessage().isEmpty()) {
                emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Ghi chú:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>").append(customer.getMessage()).append("</td></tr>");
            }
            if (!participants.isEmpty()) {
                emailContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'><strong>Người tham gia:</strong></td><td style='border: 1px solid #ddd; padding: 8px;'>");
                emailContent.append("<ul>");
                for (Participant participant : participants) {
                    emailContent.append("<li>").append(participant.getName()).append("</li>");
                }
                emailContent.append("</ul></td></tr>");
            }
            emailContent.append("</table>");
            emailContent.append("<p>Vui lòng kiểm tra thông tin và liên hệ chúng tôi nếu có bất kỳ câu hỏi nào.</p>");
            emailContent.append("<p>Trân trọng,<br>Đội ngũ DNACare</p>");
            emailContent.append("</body></html>");

            helper.setText(emailContent.toString(), true);
            mailSender.send(mimeMessage);
            logger.info("Booking confirmation email sent to: {}", customer.getEmail());

        } catch (MessagingException e) {
            // Bây giờ customer có thể truy cập được từ catch block
            if (customer != null && customer.getEmail() != null) {
                logger.error("Failed to send email to {}: {}", customer.getEmail(), e.getMessage(), e);
            } else {
                logger.error("Failed to send email: {}", e.getMessage(), e);
            }
            throw new RuntimeException("Không thể gửi email xác nhận: " + e.getMessage(), e);
        }
    }
}