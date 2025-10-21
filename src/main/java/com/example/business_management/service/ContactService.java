package com.example.business_management.service;

import com.example.business_management.dto.contactsDto.ContactResponse;
import com.example.business_management.dto.contactsDto.ContactUpdateRequest;
import com.example.business_management.dto.DeletedDto;
import com.example.business_management.entity.Account;
import com.example.business_management.entity.Contact;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.exception.UnauthorizedException;
import com.example.business_management.repo.ContactRepo;
import com.example.business_management.repo.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepo contactRepo;
    private final AccountRepo accountRepo;


    public Page<ContactResponse> getContacts(String search, int page, int size) {
        Sort s=Sort.by("id").descending().and(Sort.by("email").descending());

        Pageable pageable = PageRequest.of(page, size, s);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Account account = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Page<Contact> contacts;
        if (search.isEmpty()) {
            contacts = contactRepo.findByAccountIdAndIsActiveTrue(account.getId(), pageable);
        } else {
            contacts = contactRepo.findByAccountIdAndNameContainingIgnoreCaseAndIsActiveTrue(
                    account.getId(), search, pageable);
        }

        List<ContactResponse> content = contacts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, contacts.getTotalElements());
    }


    public ContactResponse getContactByIdForAdmin(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account adminAccount = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        Contact contact = contactRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        if (!contact.getAccount().getId().equals(adminAccount.getId())) {
            throw new UnauthorizedException("Access denied: contact not in your account");
        }

        return toResponse(contact);
    }


    public ContactResponse updateContact(Long id, ContactUpdateRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = auth.getName();

        Contact contact = contactRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !contact.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedException("Access denied: you can only update your own profile");
        }

        if (req.getName() != null) contact.setName(req.getName());
        if (req.getEmail() != null) contact.setEmail(req.getEmail());
        if (req.getPhone() != null) contact.setPhone(req.getPhone());

        contactRepo.save(contact);
        return toResponse(contact);
    }

    public DeletedDto deleteContact(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Account adminAccount = accountRepo.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        Contact contact = contactRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        if (!contact.getAccount().getId().equals(adminAccount.getId())) {
            throw new UnauthorizedException("Access denied: cannot delete contact outside your account");
        }

        contact.setActive(false);
        contactRepo.save(contact);
        return DeletedDto.builder()
                .message("Contact deleted successfully")
                .build();
    }

    private ContactResponse toResponse(Contact c) {
        return new ContactResponse(
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getRole(),
                c.getAccount().getId(),
                c.isActive()
        );
    }
}
