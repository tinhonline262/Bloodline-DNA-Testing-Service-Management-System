package com.dna_testing_system.dev.controller;


import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user") // Thêm base path
public class UserController {

    UserProfileService userProfileService;

    @GetMapping("/profile")  // URL sẽ là /user/profile
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", userProfile);
        return "user/profile";
    }

    @GetMapping("/profile/update")  // URL sẽ là /user/profile/update
    public String showUpdateProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userEditProfile", userProfile);
        return "user/edit-profile";
    }

    @PostMapping("/profile/update")  // URL sẽ là /user/profile/update
    public String updateProfile(@ModelAttribute("userEditProfile") UserProfileRequest userProfile,
                                @RequestParam(value = "file", required = false) MultipartFile file) {
        // ... existing code ...
        return "redirect:/user/profile"; // Sửa redirect URL
    }
}