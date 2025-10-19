package com.example.business_management.dto.contactsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String role;
}
