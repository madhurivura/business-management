package com.example.business_management.controller;

import com.example.business_management.dto.DeletedDto;
import com.example.business_management.dto.customerDto.CustDto;
import com.example.business_management.dto.customerDto.UpdateCustDto;
import com.example.business_management.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Page<CustDto> getCustomers(@RequestParam(required = false) String search, Pageable pageable) {
        return customerService.getCustomers(search, pageable);
    }

    @GetMapping("/{id}")
    public CustDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }


    @PutMapping("/{id}")
    public UpdateCustDto updateCustomer(@PathVariable Long id, @RequestBody UpdateCustDto dto) {
        return customerService.updateCustomer(id, dto);
    }

    @DeleteMapping("/{id}")
    public DeletedDto deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}
