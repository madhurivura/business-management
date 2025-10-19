package com.example.business_management.dto.contactsDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactRegisterResponse {
    private Long id;
    private String role;
    private String status;
}
