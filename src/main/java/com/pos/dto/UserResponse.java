package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UserResponse {
    private String id;
    private Long seqNo;
    private String outletId;
    private String displayName;
    private String email;
    private String mobile;
    private String role;
    private Boolean isActive;
}
