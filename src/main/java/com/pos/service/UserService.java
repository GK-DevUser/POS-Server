package com.pos.service;

import com.pos.dto.*;
import com.pos.entity.Role;
import com.pos.entity.User;
import com.pos.repository.RoleRepository;
import com.pos.repository.UserRepository;
import com.pos.security.JwtContext;
import com.pos.util.UuidV7;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    public UserResponse create(UserCreateRequest req) {

        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        Role role = roleRepo.findById(req.getRoleId())
                .orElseThrow(() -> new RuntimeException("Invalid role"));

        String outletId;

        if (JwtContext.isSuperAdmin()) {
            if (req.getOutletId() == null) {
                throw new RuntimeException("OutletId required for SUPER_ADMIN");
            }
            outletId = req.getOutletId();
        } else {
            outletId = JwtContext.outletId();
        }

        User user = new User();
        user.setId(UuidV7.generate());
        user.setOutletId(outletId);
        user.setDisplayName(req.getDisplayName());
        user.setEmail(req.getEmail());
        user.setMobile(req.getMobile());
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        user.setCreatedBy(JwtContext.userId());

        userRepo.save(user);
        return toResponse(user);
    }
    public UserResponse update(String id, UserCreateRequest req) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // outlet reassignment ONLY for SUPER_ADMIN
        if (JwtContext.isSuperAdmin() && req.getOutletId() != null) {
            user.setOutletId(req.getOutletId());
        }

        if (req.getDisplayName() != null) {
            user.setDisplayName(req.getDisplayName());
        }

        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }

        if (req.getMobile() != null) {
            user.setMobile(req.getMobile());
        }

        if (req.getIsActive() != null) {
            user.setIsActive(req.getIsActive());
        }

        if (req.getRoleId() != null) {
            Role role = roleRepo.findById(req.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Invalid role"));
            user.setRole(role);
        }

        // 🔐 PASSWORD UPDATE (SAFE)
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        }

        user.setModifiedBy(JwtContext.userId());
        userRepo.save(user);

        return toResponse(user);
    }

    public UserResponse getById(String id) {
        return userRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponse> listByOutlet() {
        return userRepo.findByOutletId(JwtContext.outletId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deactivate(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(false);
        user.setModifiedBy(JwtContext.userId());
        userRepo.save(user);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .seqNo(u.getSeqNo())
                .outletId(u.getOutletId())
                .displayName(u.getDisplayName())
                .email(u.getEmail())
                .mobile(u.getMobile())
                .role(u.getRole().getName())
                .isActive(u.getIsActive())
                .build();
    }
}
