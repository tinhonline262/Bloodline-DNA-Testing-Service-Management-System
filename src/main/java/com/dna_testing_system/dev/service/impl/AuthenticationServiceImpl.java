package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.dto.request.AuthenticationRequest;
import com.dna_testing_system.dev.dto.request.RegisterRequest;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.entity.UserProfile;
import com.dna_testing_system.dev.exception.AuthenticationException;
import com.dna_testing_system.dev.exception.ErrorCode;
import com.dna_testing_system.dev.exception.ResourceNotFoundException;
import com.dna_testing_system.dev.repository.UserProfileRepository;
import com.dna_testing_system.dev.repository.UserRepository;
import com.dna_testing_system.dev.service.AuthenticationService;
import com.dna_testing_system.dev.utils.PasswordUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    
    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
      @Override
    @Transactional
    public User register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException(ErrorCode.USER_EXISTS, 
                "Username '" + request.getUsername() + "' already exists. Please choose a different username.");
        }
        
        // Validate username
        if (request.getUsername() == null || request.getUsername().length() < 3) {
            throw new AuthenticationException(ErrorCode.USERNAME_INVALID);
        }
        
        // Validate password
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new AuthenticationException(ErrorCode.PASSWORD_INVALID);
        }

        // Create and save the User entity with minimal information
        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(PasswordUtil.encode(request.getPassword()))
                .isActive(false)
                .build();
        
        user = userRepository.save(user);
        
        // Create and save minimal UserProfile
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
        
        userProfileRepository.save(userProfile);
        
        // Set the profile in the user entity
        user.setUserProfile(userProfile);
        
        return user;
    }    @Override
    @Transactional
    public User activateUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.USER_NOT_EXISTS, 
                    "User not found with id: " + userId));
        
        // If user is already active, no need to update
        if (user.getIsActive()) {
            return user;
        }
        
        user.setIsActive(true);
        return userRepository.save(user);
    }@Override
    public User authenticate(AuthenticationRequest request) {
        if (request == null) {
            throw new AuthenticationException(ErrorCode.UNKNOWN_ERROR, "Authentication request cannot be null");
        }
        
        // Validate username
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new AuthenticationException(ErrorCode.USERNAME_INVALID, "Username is required");
        }
        
        // Validate password
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new AuthenticationException(ErrorCode.PASSWORD_INVALID, "Password is required");
        }
        
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_CREDENTIALS));

        // Verify password
        if (!PasswordUtil.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        }
        
        // Check if user account is active (uncomment if needed)
        // if (!user.getIsActive()) {
        //     throw new AuthenticationException(ErrorCode.ACCOUNT_INACTIVE, "Account is not activated. Please activate your account first.");
        // }

        return user;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
