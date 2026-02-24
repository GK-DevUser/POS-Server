package com.pos.service;

import com.pos.dto.LoginRequest;
import com.pos.dto.LoginResponse;
import com.pos.entity.User;
import com.pos.security.JwtUtil;
import com.pos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {

        if (request.getLoginId() == null || request.getPassword() == null) {
            throw new RuntimeException("LoginId and password required");
        }

        User user = findUser(request.getLoginId());

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new RuntimeException("User is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().getName())
                .displayName(user.getDisplayName())
                .build();
    }

    private User findUser(String loginId) {

        return loginId.contains("@")
                ? userRepository.findByEmail(loginId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"))
                : userRepository.findByMobile(loginId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
