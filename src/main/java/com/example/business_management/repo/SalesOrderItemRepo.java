package com.example.business_management.repo;

import com.example.business_management.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderItemRepo extends JpaRepository<SalesOrderItem, Long> {
}
