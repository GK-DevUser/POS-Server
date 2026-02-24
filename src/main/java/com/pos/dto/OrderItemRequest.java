package com.pos.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemRequest {
    private String itemId;
    private String name;
    private Integer quantity;
    private BigDecimal discPer;
    private BigDecimal discAmount;
    private BigDecimal mrp;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal rateWithoutTax;
    private BigDecimal mrpWithoutTax;
    private String notes;
}
