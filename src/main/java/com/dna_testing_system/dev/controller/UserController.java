package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    IUserProfileService userProfileService;

    //view user profile
    @GetMapping("/user/profile/{id}")
    public String userProfile(@PathVariable String id, Model model) {
        model.addAttribute("userProfile", userProfileService.getUserProfileById(id)); // Replace "1" with the actual user ID
        return "profile";
    }

    //update user profile
    @RequestMapping("/user/profile/update/{id}")
    public String updateUserProfileForm(@PathVariable String id, Model model) {
        model.addAttribute("userProfile", userProfileService.getUserProfileById(id));
        return "update-profile";
    }

    @PostMapping("/user/profile/update/{id}")
    public String updateUserProfile(@ModelAttribute("userProfile") UserProfileRequest userProfileDto, Model model) {
        MultipartFile file = userProfileDto.getProfileImage();
        if (file != null && !file.isEmpty()) {
            // Convert MultipartFile to Base64 String
            try {
                byte[] fileBytes = file.getBytes();
                String base64 = Base64.getEncoder().encodeToString(fileBytes);
                userProfileDto.setProfileImageUrl(base64);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userProfileService.createUserProfile(userProfileDto);
        return "redirect:/profile";
    }
    //end update user profile
}
