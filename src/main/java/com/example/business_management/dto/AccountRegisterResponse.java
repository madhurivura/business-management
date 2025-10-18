package com.example.business_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountRegisterResponse {
    private Long id;
    private String role;
    private String status;
    private String accountNumber;
}
