package com.example.business_management.repo;

import com.example.business_management.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByIdAndIsActiveTrue(Long id);
    Page<Customer> findByIsActiveTrue(Pageable pageable);
    Page<Customer> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    Optional<Customer> findByEmailAndIsActiveTrue(String email);

    Optional<Customer> findByCustomerCodeAndIsActiveTrue(String customerCode);
}
