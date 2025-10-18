package com.example.business_management.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderRequest {
    private Long accountId;
    private List<SalesOrderItemRequest> items;
}
