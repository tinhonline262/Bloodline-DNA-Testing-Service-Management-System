package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find a user by email:
    // Optional<UserProfile> findByEmail(String email);
    // Other CRUD operations are inherited from JpaRepository
}
