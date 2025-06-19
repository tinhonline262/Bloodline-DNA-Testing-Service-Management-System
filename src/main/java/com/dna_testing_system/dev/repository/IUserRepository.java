package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {

}
