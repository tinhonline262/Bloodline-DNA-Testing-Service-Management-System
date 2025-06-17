package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.RegisterRequest;
import com.dna_testing_system.dev.dto.request.UpdateProfileRequest;
import com.dna_testing_system.dev.dto.response.UserResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.service.AuthenticationService;
import com.dna_testing_system.dev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    

}
