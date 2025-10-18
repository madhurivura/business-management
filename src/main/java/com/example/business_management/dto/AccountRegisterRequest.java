package com.example.business_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}
