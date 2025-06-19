package com.dna_testing_system.dev.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {
    String id;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
    String phoneNumber;
    String profileImageUrl;
    String password;
    MultipartFile profileImage; // <--- Thêm dòng này
}
