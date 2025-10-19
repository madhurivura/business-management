package com.example.business_management.dto.productsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {
    private Long id;
    private String productCode;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private boolean isActive;
}
