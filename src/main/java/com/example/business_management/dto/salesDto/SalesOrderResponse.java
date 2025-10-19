package com.example.business_management.dto.salesDto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderResponse {
    private String orderNumber;
    private LocalDate orderDate;
    private Double subtotal;
    private Double tax;
    private Double total;
    private Long accountId;
    private List<SalesOrderItemResponse> items;
}
