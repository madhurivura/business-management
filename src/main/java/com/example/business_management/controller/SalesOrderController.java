package com.example.business_management.controller;

import com.example.business_management.dto.DeletedDto;
import com.example.business_management.dto.salesDto.SalesOrderRequest;
import com.example.business_management.dto.salesDto.SalesOrderResponse;
import com.example.business_management.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<SalesOrderResponse> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return orderService.getAllOrders(page, size);
    }

    @DeleteMapping("/{id}")
    public DeletedDto cancelOrder(@PathVariable Long id){
        return orderService.cancelOrder(id);
    }


}
