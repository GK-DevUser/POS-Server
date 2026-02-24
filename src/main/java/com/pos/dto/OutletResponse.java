package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class OutletResponse {
    private String id;
    private Long seqNo;
    private String name;
    private String displayName;
    private String gstin;
    private Boolean isActive;
}
