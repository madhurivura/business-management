package com.example.business_management.controller;

import com.example.business_management.dto.salesDto.SalesOrderRequest;
import com.example.business_management.dto.salesDto.SalesOrderResponse;
import com.example.business_management.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService orderService;

    @PostMapping
    public SalesOrderResponse createOrder(@RequestBody SalesOrderRequest request, Authentication authentication) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<SalesOrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }


}
