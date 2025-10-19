package com.example.business_management.dto.customerDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustDto {
    @NotBlank
    @Size(min=2, max=50)
    String name;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String address;
}
