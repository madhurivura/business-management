package com.example.business_management.dto.customerDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustDto {
    Long id;

    @NotBlank
    String customerCode;

    @NotBlank @Size(min=2, max=50)
    String name;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String address;
}
