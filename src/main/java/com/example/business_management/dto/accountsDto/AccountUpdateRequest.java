package com.example.business_management.dto.accountsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
}
