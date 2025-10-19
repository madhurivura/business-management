package com.example.business_management.dto.customerDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRegisterRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Password must be 10 digits")
    private String password;

    private String address;
}
