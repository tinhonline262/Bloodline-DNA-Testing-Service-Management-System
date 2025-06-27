package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findByCustomer(User customer);
}
