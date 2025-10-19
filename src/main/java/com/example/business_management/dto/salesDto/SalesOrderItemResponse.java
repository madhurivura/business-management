package com.example.business_management.dto.salesDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemResponse {
    private String productName;
    private Integer quantity;
    private Double price;
}
