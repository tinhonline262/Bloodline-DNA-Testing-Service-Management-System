package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByRoleName(String roleName);
}
