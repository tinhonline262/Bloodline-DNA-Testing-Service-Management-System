package com.dna_testing_system.dev.controller;


import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Test{

    UserProfileService userProfileService;
    @GetMapping("/profile")
    public String getProfile(Model model, String id) {
        UserProfileResponse userProfile = userProfileService.getUserProfile(id);
        model.addAttribute("userProfile", userProfile);
        return "profile"; // Return the view name for the profile page
    }
    @PutMapping("/profile/{username}")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable String username, @RequestBody @Valid UserProfileRequest userProfile, Model model) {
        boolean flag = userProfileService.updateUserProfile(username, userProfile);
        if (flag) {
            UserProfileResponse updatedProfile = userProfileService.getUserProfile(username);
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
