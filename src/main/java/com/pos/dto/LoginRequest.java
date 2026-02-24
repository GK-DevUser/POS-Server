package com.pos.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class LoginRequest {
    private String loginId;   // email or mobile
    private String password;
}
