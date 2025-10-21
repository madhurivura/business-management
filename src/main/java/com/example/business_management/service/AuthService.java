package com.example.business_management.service;

import com.example.business_management.dto.accountsDto.AccountRegisterRequest;
import com.example.business_management.dto.accountsDto.AccountRegisterResponse;
import com.example.business_management.dto.contactsDto.ContactRegisterRequest;
import com.example.business_management.dto.contactsDto.ContactRegisterResponse;
import com.example.business_management.dto.customerDto.CustomerRegisterRequest;
import com.example.business_management.dto.customerDto.CustomerRegisterResponse;
import com.example.business_management.dto.loginDto.LoginRequest;
import com.example.business_management.dto.loginDto.LoginResponse;
import com.example.business_management.entity.Account;
import com.example.business_management.entity.Contact;
import com.example.business_management.entity.Customer;
import com.example.business_management.exception.DuplicateResourceException;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.repo.AccountRepo;
import com.example.business_management.repo.ContactRepo;
import com.example.business_management.repo.CustomerRepo;
import com.example.business_management.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepo accountRepo;
    private final ContactRepo contactRepo;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AccountRegisterResponse registerAccount(AccountRegisterRequest request) {
        if (accountRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already registered");
        }

        Account account = new Account();
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setPhone(request.getPhone());
        account.setAccountNumber("ACC-" + (1000+accountRepo.count()+1));

        accountRepo.save(account);

        return new AccountRegisterResponse(account.getId(), "ROLE_ADMIN", "registered",account.getAccountNumber());
    }

    public ContactRegisterResponse registerContact(ContactRegisterRequest req) {
        if (contactRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Contact email already exists");
        }

        Account account = accountRepo.findByIdAndIsActiveTrue(req.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or inactive"));



        Contact contact = new Contact();
        contact.setName(req.getName());
        contact.setEmail(req.getEmail());
        contact.setPassword(passwordEncoder.encode(req.getPassword()));
        contact.setPhone(req.getPhone());
        contact.setRole(req.getRole());
        contact.setAccount(account);

        contactRepo.save(contact);

        return new ContactRegisterResponse(contact.getId(), contact.getRole(), "registered");
    }


    public LoginResponse login(LoginRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (Exception ex){
            throw new ResourceNotFoundException("Invalid email or password");
        }

        Optional<Account> accountOpt = accountRepo.findByEmailAndIsActiveTrue(request.getEmail());
        if (accountOpt.isPresent()) {
            Account acc = accountOpt.get();
            String token = jwtUtil.generateToken(acc.getEmail(), "ROLE_ADMIN");
            return new LoginResponse(token, acc.getId().toString(), "ROLE_ADMIN");
        }

        Optional<Contact> contactOpt = contactRepo.findByEmailAndIsActive(request.getEmail(),true);
        if (contactOpt.isPresent()) {
            Contact c = contactOpt.get();
            String token = jwtUtil.generateToken(c.getEmail(), c.getRole());
            return new LoginResponse(token, c.getId().toString(), c.getRole());
        }

        Optional<Customer> cust = customerRepo.findByEmailAndIsActiveTrue(request.getEmail());
        if(cust.isPresent()){
            Customer customer = cust.get();
            String tkn = jwtUtil.generateToken(customer.getEmail(), "ROLE_CUSTOMER");
            return new LoginResponse(tkn, customer.getId().toString(),"ROLE_CUSTOMER");
        }

        throw new ResourceNotFoundException("Invalid credentials or inactive");
    }

    public CustomerRegisterResponse registerCustomer(CustomerRegisterRequest req) {
        if (customerRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Customer email already exists");
        }

        Customer customer = new Customer();
        customer.setCustomerCode("CUST-" + (1000 + customerRepo.count() + 1));
        customer.setName(req.getName());
        customer.setEmail(req.getEmail());
        customer.setPassword(passwordEncoder.encode(req.getPassword()));
        customer.setAddress(req.getAddress());

        customerRepo.save(customer);

        return new CustomerRegisterResponse(customer.getId(), "ROLE_CUSTOMER", "registered", customer.getCustomerCode());
    }

    public Object getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Account> accountOpt = accountRepo.findByEmail(email);
        if (accountOpt.isPresent()) return accountOpt.get();

        Optional<Contact> contactOpt = contactRepo.findByEmail(email);
        if (contactOpt.isPresent()) return contactOpt.get();

        throw new ResourceNotFoundException("User not found");
    }

}

