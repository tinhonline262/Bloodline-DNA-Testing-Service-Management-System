package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
}
