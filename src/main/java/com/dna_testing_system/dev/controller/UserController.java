package com.dna_testing_system.dev.controller;


import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.OrderParticipantResponse;
import com.dna_testing_system.dev.dto.response.OrderTestKitResponse;
import com.dna_testing_system.dev.dto.response.ServiceOrderByCustomerResponse;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.service.*;
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
public class UserController {

    UserProfileService userProfileService;
    OrderService orderService;
    OrderKitService orderKitService;
    OrderParticipantService orderParticipantService;
    MedicalServiceManageService medicalService;
    TestKitService testKitService;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userProfile", userProfile);
        return "profile"; // Return the view name for the profile page
    }

    @GetMapping("/profile/update")
    public String showUpdateProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse userProfile = userProfileService.getUserProfile(currentPrincipalName);
        model.addAttribute("userEditProfile",userProfile);
        return "edit-profile"; // Return the view name for the update profile page
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("userEditProfile") UserProfileRequest userProfile, @RequestParam(value = "file",required = false) MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserProfileResponse existingProfile = userProfileService.getUserProfile(currentPrincipalName);
        if(file.getOriginalFilename().equals("")) {
            userProfile.setProfileImageUrl(existingProfile.getProfileImageUrl());
        }
        else{
            String uploadsDir = "uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadsDir + fileName);

            try{
                Files.createDirectories(Paths.get(uploadsDir));
                file.transferTo(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String imageUrl = "/uploads/" + fileName;
            userProfile.setProfileImageUrl(imageUrl);
        }

        if(userProfile.getDateOfBirth() == null) {
            userProfile.setDateOfBirth(existingProfile.getDateOfBirth());
        }
        userProfileService.updateUserProfile(currentPrincipalName, userProfile);
        return "redirect:/profile";
    }

    @GetMapping("/order-history")
    public String getOrderHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<ServiceOrderByCustomerResponse> orders = orderService.getAllOrdersByCustomerId(currentPrincipalName);
        model.addAttribute("orders", orders);
        return "order-history"; // Return the view name for the order history page
    }

    @GetMapping("/order-history/details")
    public String getOrderDetails(@RequestParam("orderId") Long orderId, Model model) {
        ServiceOrderByCustomerResponse orderDetails = orderService.getOrderById(orderId);
        List<OrderTestKitResponse> orderTestKits = orderKitService.getOrderById(orderId);
        List<OrderParticipantResponse> orderParticipants = orderParticipantService.getAllParticipantsByOrderId(orderId);
        model.addAttribute("orderParticipants", orderParticipants);
        model.addAttribute("orderTestKits", orderTestKits);
        model.addAttribute("orderDetails", orderDetails);
        return "order-details"; // Return the view name for the order details page
    }
}
