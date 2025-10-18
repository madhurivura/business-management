package com.example.business_management.security;

import com.example.business_management.entity.Account;
import com.example.business_management.entity.Contact;
import com.example.business_management.entity.Customer;
import com.example.business_management.repo.AccountRepo;
import com.example.business_management.repo.ContactRepo;
import com.example.business_management.repo.CustomerRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;
    private final ContactRepo contactRepo;
    private final CustomerRepo customerRepo;

    public CustomUserDetailsService(AccountRepo accountRepo, ContactRepo contactRepo, CustomerRepo customerRepo) {
        this.accountRepo = accountRepo;
        this.contactRepo = contactRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepo.findByEmail(email)
                .map(acc -> User.builder()
                        .username(acc.getEmail())
                        .password(acc.getPassword())
                        .roles("ADMIN")
                        .build())
                .orElseGet(() -> contactRepo.findByEmail(email)
                        .map(c -> User.builder()
                                .username(c.getEmail())
                                .password(c.getPassword())
                                .roles(c.getRole().toUpperCase())
                                .build())
                        .orElseGet(() -> customerRepo.findByEmail(email)
                                .map(cust -> User.builder()
                                        .username(cust.getEmail())
                                        .password(cust.getPassword())
                                        .roles("CUSTOMER")
                                        .build())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
                        ));
    }
}
