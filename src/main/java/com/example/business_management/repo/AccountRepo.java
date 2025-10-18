package com.example.business_management.repo;

import com.example.business_management.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByIdAndIsActiveTrue(Long id);

    Optional<Account> findByEmailAndIsActiveTrue(String email);


    Page<Account> findByIsActiveTrue(Pageable pageable);

    Page<Account> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
}
