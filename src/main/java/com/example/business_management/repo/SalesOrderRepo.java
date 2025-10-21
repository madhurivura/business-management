package com.example.business_management.repo;

import com.example.business_management.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SalesOrderRepo extends JpaRepository<SalesOrder, Long> {
    List<SalesOrder> findByAccountIdAndIsActiveTrue(Long accountId);

    Page<SalesOrder> findByAccountIdAndIsActiveTrue(Long accountId, Pageable pageable);

    Optional<SalesOrder> findByIdAndIsActiveTrue(Long id);
}
