package com.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
    private String displayName;
    private String outletDisplayName;
}
