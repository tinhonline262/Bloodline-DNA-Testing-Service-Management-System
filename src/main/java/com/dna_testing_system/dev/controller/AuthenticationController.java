package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.AuthenticationRequest;
import com.dna_testing_system.dev.dto.request.RegisterRequest;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling authentication operations
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    
    AuthenticationService authenticationService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Check if username is available
        if (!authenticationService.isUsernameAvailable(request.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists");
            return "register";
        }

        try {
            User user = authenticationService.register(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! Please check your email to activate your account.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authRequest", new AuthenticationRequest());
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("authRequest") AuthenticationRequest request,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "login";
        }
        
        try {
            User user = authenticationService.authenticate(request);
            
            // In a real application, you would set up the security context here
            // and handle "remember me" functionality if requested
            if (request.isRememberMe()) {
                // Set remember-me cookie or token
            }
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Login successful! Welcome, " + user.getUserProfile().getFirstName());
            
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            // Don't clear the username to make it easier for the user to retry
            model.addAttribute("username", request.getUsername());
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        // In a real application, you would invalidate the session and clear the security context
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out successfully");
        return "redirect:/login";
    }
}
