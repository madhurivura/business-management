package com.example.business_management.controller;

import com.example.business_management.dto.contactsDto.ContactResponse;
import com.example.business_management.dto.contactsDto.ContactUpdateRequest;
import com.example.business_management.dto.DeletedDto;
import com.example.business_management.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public Page<ContactResponse> getAllContactsByAccount(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            , Authentication authentication
    ) {
        return contactService.getContacts(search, page, size);
    }

    @GetMapping("/{id}")
    public ContactResponse getContact(@PathVariable Long id, Authentication authentication) {
        return contactService.getContactByIdForAdmin(id);
    }

    @PatchMapping("/{id}")
    public ContactResponse updateContact(@PathVariable Long id, @RequestBody ContactUpdateRequest contactUpdateRequest, Authentication authentication) {
        return contactService.updateContact(id, contactUpdateRequest);
    }

    @DeleteMapping("/delete/{id}")
    public DeletedDto softDeleteContact(@PathVariable Long id, Authentication authentication) {
        return contactService.deleteContact(id);
    }
}
