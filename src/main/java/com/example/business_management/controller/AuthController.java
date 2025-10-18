package com.example.business_management.controller;

import com.example.business_management.dto.*;
import com.example.business_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-account")
    public AccountRegisterResponse registerAccount(@RequestBody AccountRegisterRequest request) {
        return authService.registerAccount(request);
    }

    @PostMapping("/register-contact")
    public ContactRegisterResponse registerContact(@RequestBody ContactRegisterRequest request) {
        return authService.registerContact(request);
    }

    @PostMapping("/register-customer")
    public CustomerRegisterResponse registerCustomer(@RequestBody CustomerRegisterRequest request) {
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

