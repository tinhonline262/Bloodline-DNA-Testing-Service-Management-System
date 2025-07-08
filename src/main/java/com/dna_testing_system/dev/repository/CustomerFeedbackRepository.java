package com.dna_testing_system.dev.repository;

import com.dna_testing_system.dev.entity.CustomerFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedback,Long> {
    List<CustomerFeedback> findByCustomer_Id(String customerId);
    Page<CustomerFeedback> findAllByFeedbackTitleContainsIgnoreCase(String feedbackTitle, Pageable pageable);
}
