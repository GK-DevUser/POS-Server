package com.pos.dto;

import com.pos.entity.DietaryType;
import com.pos.entity.GstType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
public class ItemRequest {

    private String id;
    private String name;
    private String shortCode;
    private String onlineDisplayName;

    private String categoryId;
    private String taxId;

    private BigDecimal price;
    private String description;

    private DietaryType dietaryType;
    private GstType gstType;

    private Boolean allowDineIn;
    private Boolean allowTakeaway;
    private Boolean allowDelivery;
    private Boolean onlineExpose;

    private Boolean isActive;

    private Integer display_order;
}
