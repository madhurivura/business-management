package com.example.business_management.service;

import com.example.business_management.dto.AccountResponse;
import com.example.business_management.dto.AccountUpdateRequest;
import com.example.business_management.entity.Account;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.exception.UnauthorizedException;
import com.example.business_management.repo.AccountRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;

    public List<AccountResponse> getAccounts(String search, int page, int size) {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

        if(authentication1 == null){
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication1.getName();

        if(!accountRepo.findByEmailAndIsActiveTrue(email).isPresent()){
            throw new UnauthorizedException("Access denied");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<Account> accounts;
        if (search.isEmpty()) {
            accounts = accountRepo.findByIsActiveTrue(pageable);
        } else {
            accounts = accountRepo.findByNameContainingIgnoreCaseAndIsActiveTrue(search, pageable);
        }

        List<AccountResponse> content = accounts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return content;
    }

    public AccountResponse getAccountById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();

        if(!accountRepo.findByEmailAndIsActiveTrue(email).isPresent()){
            throw new UnauthorizedException("Access denied");
        }

        Account account = accountRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return toResponse(account);
    }

    public AccountResponse updateAccount(Long id, AccountUpdateRequest req) {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        if(authentication1 == null){
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication1.getName();

        if(!accountRepo.findByEmailAndIsActiveTrue(email).isPresent()){
            throw new UnauthorizedException("Access denied");
        }



        Account loggedAccount = accountRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new UnauthorizedException("Access denied"));

        if(!loggedAccount.getId().equals(id)){
            throw new UnauthorizedException("You can only update your own account");
        }

        Account account = accountRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (req.getName() != null) account.setName(req.getName());
        if (req.getEmail() != null) account.setEmail(req.getEmail());
        if (req.getPhone() != null) account.setPhone(req.getPhone());
        if (req.getAddress() != null) account.setAddress(req.getAddress());

        accountRepo.save(loggedAccount);
        return toResponse(loggedAccount);
    }

    @Transactional
    public String deleteAccount(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();

        Account loggedInAcc = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UnauthorizedException("Access denied"));


        if (!loggedInAcc.getId().equals(id)) {
            throw new UnauthorizedException("You can only delete your own account");
        }

        Account account = accountRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setActive(false);
        if (account.getContacts() != null) {
            account.getContacts().forEach(c -> c.setActive(false));
        }

        accountRepo.save(account);

        return "Account deleted successfully";
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getName(),
                account.getEmail(),
                account.getPhone(),
                account.getAddress(),
                account.isActive()
        );
    }
}
