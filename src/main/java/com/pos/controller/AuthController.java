package com.pos.controller;

import com.pos.dto.ApiResponse;
import com.pos.dto.LoginRequest;
import com.pos.dto.LoginResponse;
import com.pos.entity.Outlet;
import com.pos.entity.User;
import com.pos.repository.OutletRepository;
import com.pos.repository.UserRepository;
import com.pos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final OutletRepository outletRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest req) {
        try {
            Optional<User> userOpt =
                    req.getLoginId().contains("@")
                            ? userRepo.findByEmail(req.getLoginId())
                            : userRepo.findByMobile(req.getLoginId());

            if (userOpt.isEmpty()) {
                return new ApiResponse<>("Invalid credentials", false, null);
            }

            User user = userOpt.get();

            if (!Boolean.TRUE.equals(user.getIsActive())) {
                return new ApiResponse<>("User is inactive", false, null);
            }

            if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
                logger.warn("Invalid login attempt for {}", req.getLoginId());
                return new ApiResponse<>("Invalid credentials", false, null);
            }

            String outletDisplayName = null;
            if (user.getOutletId() != null) {
                outletDisplayName = outletRepo.findById(user.getOutletId())
                        .map(Outlet::getDisplayName)
                        .orElse(null);
            }

            LoginResponse response = LoginResponse.builder()
                    .token(jwtUtil.generateToken(user))
                    .role(user.getRole().getName())
                    .displayName(user.getDisplayName())
                    .outletDisplayName(outletDisplayName)
                    .build();

            return new ApiResponse<>("Login successful", true, response);
        } catch (Exception e) {
            logger.error(e);
            return new ApiResponse<>("Invalid Request", false, null);
        }

    }
}
