package com.example.business_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerRegisterResponse {
    private Long id;
    private String role;
    private String message;
    private String customerCode;
}