package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Builder
public class ItemResponse {

    private String id;
    private Long seqNo;

    private String name;
    private String shortCode;
    private String onlineDisplayName;

    private String categoryId;
    private String taxId;

    private BigDecimal price;
    private String description;

    private String dietaryType;
    private String gstType;

    private Boolean allowDineIn;
    private Boolean allowTakeaway;
    private Boolean allowDelivery;
    private Boolean onlineExpose;

    private Boolean isActive;

    private Integer display_order;
}
