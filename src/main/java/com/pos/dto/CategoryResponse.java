package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
public class CategoryResponse {

    private String id;
    private Long seqNo;
    private String name;
    private String onlineDisplayName;
    private Integer displayOrder;
    private Boolean isActive;
    private List<ItemResponse> items;
}
