package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder,Long> {
}
