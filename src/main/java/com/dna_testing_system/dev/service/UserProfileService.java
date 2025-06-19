package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.entity.UserProfile;
import com.dna_testing_system.dev.mapper.IUserMapper;
import com.dna_testing_system.dev.mapper.IUserProfileMapper;
import com.dna_testing_system.dev.repository.IUserProfileRepository;
import com.dna_testing_system.dev.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService implements IUserProfileService {

    IUserRepository userRepository;
    IUserProfileRepository userProfileRepository;
    IUserProfileMapper userProfileMapper;
    IUserMapper userMapper;


    @Override
    public void createUserProfile(UserProfileRequest userProfileRequest) {
        User user = userMapper.toEntity(userProfileRequest);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileRequest);
        if (user != null) {
            User finalUser = user;
            user = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + finalUser.getId()));
        } else {
            throw new RuntimeException("User cannot be null");
        }
        userProfile.setUser(user);
        user.setUserProfile(userProfile);
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse getUserProfileById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        UserProfile _userProfile = userProfileRepository.findById(user.getUserProfile().getProfileId())
                .orElseThrow(() -> new RuntimeException("User profile not found with id: " + id));

        return userProfileMapper.toResponse(_userProfile, user);
    }

    @Override
    public UserProfileResponse updateUserProfile(String id, UserProfileRequest userProfileRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        User userEntity = userMapper.toEntity(userProfileRequest);
        UserProfile _userProfile = userProfileMapper.toEntity(userProfileRequest);
        UserProfile existingProfile = userProfileRepository.findById(user.getUserProfile().getProfileId())
                .orElseThrow(() -> new RuntimeException("User profile not found with id: " + id));

        existingProfile.setFirstName(_userProfile.getFirstName());
        existingProfile.setLastName(_userProfile.getLastName());
        existingProfile.setEmail(_userProfile.getEmail());
        existingProfile.setDateOfBirth(_userProfile.getDateOfBirth());
        existingProfile.setPhoneNumber(_userProfile.getPhoneNumber());
        existingProfile.setProfileImageUrl(_userProfile.getProfileImageUrl());
        user.setPasswordHash(userEntity.getPasswordHash());

        existingProfile.setUser(user);
        user.setUserProfile(existingProfile);

        userRepository.save(user);


        UserProfileResponse userProfileResponse = userProfileMapper.toResponse(user.getUserProfile(), user);

        return userProfileResponse;
    }

    @Override
    public void deleteUserProfile(String id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserProfileResponse> findAllUserProfile() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        List<UserProfileResponse> userProfiles = new ArrayList<>();
        for (User user : users) {
            if (user.getUserProfile() == null) {
                throw new RuntimeException("User profile not found for user with id: " + user.getId());
            }
            UserProfileResponse userProfileResponse = userProfileMapper.toResponse(user.getUserProfile(), user);
            userProfiles.add(userProfileResponse);
        }
        return userProfiles;
    }

    @Override
    public List<UserProfileResponse> findAllUserProfileByName(String name) {

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        List<UserProfileResponse> userProfiles = new ArrayList<>();
        for (User user : users) {
            if (user.getUserProfile() == null) {
                throw new RuntimeException("User profile not found for user with id: " + user.getId());
            }
            if (user.getUserProfile().getFirstName().equalsIgnoreCase(name) ||
                user.getUserProfile().getLastName().equalsIgnoreCase(name)) {
                UserProfileResponse userProfileResponse = userProfileMapper.toResponse(user.getUserProfile(), user);
                userProfiles.add(userProfileResponse);
            }
        }
        return userProfiles;
    }
}
