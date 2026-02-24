package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class CategoryRequest {

    private String id;
    private String name;
    private String onlineDisplayName;
    private Integer displayOrder;
    private Boolean isActive;
}
