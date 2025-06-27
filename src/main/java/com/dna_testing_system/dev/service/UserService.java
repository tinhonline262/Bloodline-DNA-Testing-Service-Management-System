package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.dto.request.UpdateProfileRequest;
import com.dna_testing_system.dev.dto.response.UserResponse;
import com.dna_testing_system.dev.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(String id);
    List<User> getAllUsers();
    User updateUser(String id, User updatedUser);
    void deleteUser(String id);

    @Transactional
    Optional<User> updateProfile(String userId, UpdateProfileRequest request);

    UserResponse toUserResponse(User user);
}