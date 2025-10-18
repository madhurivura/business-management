package com.example.business_management.repo;

import com.example.business_management.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepo extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);

    Optional<Contact> findByIdAndIsActiveTrue(Long id);

    Optional<Contact> findByEmailAndIsActive(String email, boolean isActive);

    Page<Contact> findByIsActiveTrue(Pageable pageable);

    Page<Contact> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
    Page<Contact> findByAccountIdAndNameContainingIgnoreCaseAndIsActiveTrue(Long id,String search, Pageable pageable);

    Page<Contact> findByAccountIdAndIsActiveTrue(Long accountId, Pageable pageable);
}
