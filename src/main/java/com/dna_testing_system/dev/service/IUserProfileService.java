package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;

import java.util.List;

public interface IUserProfileService {
    public void createUserProfile(UserProfileRequest userProfileRequest);
    public UserProfileResponse getUserProfileById(String id);
    public UserProfileResponse updateUserProfile(String id, UserProfileRequest userProfileRequest);
    public void deleteUserProfile(String id);
    public List<UserProfileResponse> findAllUserProfile();
    public List<UserProfileResponse> findAllUserProfileByName(String name);
}
