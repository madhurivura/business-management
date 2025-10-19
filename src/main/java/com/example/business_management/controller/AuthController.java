package com.example.business_management.controller;

import com.example.business_management.dto.accountsDto.AccountRegisterRequest;
import com.example.business_management.dto.accountsDto.AccountRegisterResponse;
import com.example.business_management.dto.contactsDto.ContactRegisterRequest;
import com.example.business_management.dto.contactsDto.ContactRegisterResponse;
import com.example.business_management.dto.customerDto.CustomerRegisterRequest;
import com.example.business_management.dto.customerDto.CustomerRegisterResponse;
import com.example.business_management.dto.loginDto.LoginRequest;
import com.example.business_management.dto.loginDto.LoginResponse;
import com.example.business_management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-account")
    public AccountRegisterResponse registerAccount(@RequestBody @Valid AccountRegisterRequest request) {
        return authService.registerAccount(request);
    }

    @PostMapping("/register-contact")
    public ContactRegisterResponse registerContact(@RequestBody @Valid ContactRegisterRequest request) {
        return authService.registerContact(request);
    }

    @PostMapping("/register-customer")
    public CustomerRegisterResponse registerCustomer(@RequestBody @Valid CustomerRegisterRequest request) {
        return authService.registerCustomer(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public Object getMe(Authentication authentication) {
        String email = authentication.getName();
        return authService.getMe(email);
    }
}

