package com.example.business_management.service;

import com.example.business_management.dto.DeletedDto;
import com.example.business_management.dto.customerDto.CustDto;
import com.example.business_management.dto.customerDto.UpdateCustDto;
import com.example.business_management.entity.Account;
import com.example.business_management.entity.Customer;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.exception.UnauthorizedException;
import com.example.business_management.repo.AccountRepo;
import com.example.business_management.repo.CustomerRepo;
import com.example.business_management.repo.SalesOrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final SalesOrderRepo orderRepo;

    public Page<CustDto> getCustomers(String search, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Get logged-in account
        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Page<Customer> customers;
        if (search == null || search.isBlank()) {
            customers = customerRepo.findByIsActiveTrue(pageable);
        } else {
            customers = customerRepo.findByNameContainingIgnoreCaseAndIsActiveTrue(search, pageable);
        }

        return customers.map(this::toDTO);
    }


    public CustDto getCustomerById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Customer customer = customerRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.getEmail().equals(email) && !account.getEmail().equals(email)) {
            throw new SecurityException("Access denied");
        }

        return toDTO(customer);
    }


    public UpdateCustDto updateCustomer(Long id, UpdateCustDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = customerRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.getEmail().equals(email)) {
            throw new UnauthorizedException("Access denied");
        }


        if(dto.getName()!=null) customer.setName(dto.getName());
        if(dto.getEmail()!=null) customer.setEmail(dto.getEmail());
        if(dto.getAddress()!=null) customer.setAddress(dto.getAddress());

        customerRepo.save(customer);

        return UpdateCustDto.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }


    public DeletedDto deleteCustomer(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();


        Customer customer = customerRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.getEmail().equals(email)) {
            throw new SecurityException("Access denied");
        }

        customer.setActive(false);
        customerRepo.save(customer);

        return DeletedDto.builder()
                .message("customer deleted successfully.")
                .build();
    }

    private CustDto toDTO(Customer c) {
        return new CustDto(
                c.getId(),
                c.getCustomerCode(),
                c.getName(),
                c.getEmail(),
                c.getAddress()
        );
    }
}
