package com.example.business_management.repo;

import com.example.business_management.entity.Account;
import com.example.business_management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndIsActiveTrue(Long id);

    Page<Product> findByAccountAndIsActiveTrue(Account account, Pageable pageable);

    Page<Product> findByAccountAndNameContainingIgnoreCaseAndIsActiveTrue(Account account, String name, Pageable pageable);

}
