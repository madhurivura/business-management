package com.example.business_management.dto.salesDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemRequest {
    private Long productId;
    private Integer quantity;
}
