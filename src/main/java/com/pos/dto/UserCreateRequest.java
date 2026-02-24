package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class UserCreateRequest {

    private String outletId;   // ONLY for SUPER_ADMIN
    private String displayName;
    private String email;
    private String mobile;
    private String password;
    private Integer roleId;
    private Boolean isActive;
}
