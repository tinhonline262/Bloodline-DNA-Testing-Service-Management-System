package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.dto.request.UpdateProfileRequest;
import com.dna_testing_system.dev.dto.response.UserResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.entity.UserProfile;
import com.dna_testing_system.dev.exception.ErrorCode;
import com.dna_testing_system.dev.exception.ResourceNotFoundException;
import com.dna_testing_system.dev.mapper.UserMapper;
import com.dna_testing_system.dev.repository.UserProfileRepository;
import com.dna_testing_system.dev.repository.UserRepository;
import com.dna_testing_system.dev.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public abstract class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
    UserMapper userMapper;

    @Override
    public Optional<User> getUserById(String userId) {
        return Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTS)));
    }

    @Transactional
    @Override
    public Optional<User> updateProfile(String userId, UpdateProfileRequest request) {
        User user = getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTS)); // Lấy User từ Optional

        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            profile = userMapper.toUserProfile(request, user);
        } else {
            userMapper.updateUserProfileFromDto(request, profile);
        }

        userProfileRepository.save(profile);
        user.setUserProfile(profile); // Cập nhật User
        log.info("User Profile Updated Successfully");
        return Optional.of(user); // Trả về Optional<User> với user đã cập nhật
    }

    @Override
    public UserResponse toUserResponse(User user) {
        return userMapper.toResponse(user);
    }
}