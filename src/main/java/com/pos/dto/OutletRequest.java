package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class OutletRequest {
    private String name;
    private String displayName;
    private String gstin;
    private Boolean isActive;
}

