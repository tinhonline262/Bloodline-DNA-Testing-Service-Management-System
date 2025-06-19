package com.dna_testing_system.dev.mapper;


import com.dna_testing_system.dev.dto.request.UserProfileRequest;
import com.dna_testing_system.dev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toEntity(UserProfileRequest request);
    void toEntity(UserProfileRequest request, @MappingTarget User entity);

}
