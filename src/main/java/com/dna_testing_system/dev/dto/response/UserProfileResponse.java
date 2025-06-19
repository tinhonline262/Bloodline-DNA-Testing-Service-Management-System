package com.dna_testing_system.dev.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String userName;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
    String phoneNumber;
    String profileImageUrl;
    String password;
}
