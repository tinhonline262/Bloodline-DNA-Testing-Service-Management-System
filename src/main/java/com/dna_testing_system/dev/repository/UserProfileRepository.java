package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByEmail(String email);
}