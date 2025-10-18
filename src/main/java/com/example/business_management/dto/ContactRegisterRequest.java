package com.example.business_management.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContactRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Long accountId;
}
