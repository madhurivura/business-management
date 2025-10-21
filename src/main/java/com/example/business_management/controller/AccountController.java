package com.example.business_management.controller;

import com.example.business_management.dto.DeletedDto;
import com.example.business_management.dto.accountsDto.AccountResponse;
import com.example.business_management.dto.accountsDto.AccountUpdateRequest;
import com.example.business_management.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping
    public List<AccountResponse> getAllAccounts(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        return accountService.getAccounts(search, page, size);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id, Authentication authentication) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccount(@PathVariable Long id, @RequestBody AccountUpdateRequest request, Authentication authentication) {
        return accountService.updateAccount(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public DeletedDto deleteAccount(@PathVariable Long id, Authentication authentication) {
        return accountService.deleteAccount(id);
    }
}

