package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.dto.response.UserProfileResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserProfileMapper {
    UserProfileResponse toResponse(UserProfile request, User user);
    UserProfile toEntity(UserProfileRequest request);
    void toEntity(UserProfileRequest request, @MappingTarget UserProfile entity);
}
