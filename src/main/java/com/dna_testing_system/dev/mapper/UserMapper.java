package com.dna_testing_system.dev.mapper;

import com.dna_testing_system.dev.dto.request.RegisterRequest;
import com.dna_testing_system.dev.dto.request.UpdateProfileRequest;
import com.dna_testing_system.dev.dto.response.UserResponse;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.entity.UserProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(RegisterRequest request);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "firstName", source = "userProfile.firstName")
    @Mapping(target = "lastName", source = "userProfile.lastName")
    @Mapping(target = "email", source = "userProfile.email")
    @Mapping(target = "phoneNumber", source = "userProfile.phoneNumber")
    @Mapping(target = "profileImageUrl", source = "userProfile.profileImageUrl")
    @Mapping(target = "dateOfBirth", source = "userProfile.dateOfBirth")
    @Mapping(target = "message", ignore = true)
    UserResponse toResponse(User user);

    void updateUserProfileFromDto(UpdateProfileRequest request, @MappingTarget UserProfile userProfile);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "firstName", source = "request.firstName"),
            @Mapping(target = "lastName", source = "request.lastName"),
            @Mapping(target = "email", source = "request.email"),
            @Mapping(target = "phoneNumber", source = "request.phoneNumber"),
            @Mapping(target = "profileImageUrl", source = "request.profileImageUrl"),
            @Mapping(target = "dateOfBirth", source = "request.dateOfBirth")
    })
    UserProfile toUserProfile(UpdateProfileRequest request, User user);
}
