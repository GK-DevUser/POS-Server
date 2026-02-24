package com.pos.service;

import com.pos.entity.Permission;
import com.pos.repository.PermissionRepository;
import com.pos.repository.RolePermissionRepository;
import com.pos.repository.UserPermissionRepository;
import com.pos.security.JwtContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepo;
    private final RolePermissionRepository rolePermRepo;
    private final UserPermissionRepository userPermRepo;

    public String getPermissionValue(String permissionCode) {

        String userId = JwtContext.userId();
        Integer roleId = JwtContext.roleId(); // add this in JwtContext

        Permission perm = permissionRepo.findByCode(permissionCode)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        // 1️⃣ User override
        var userPerm = userPermRepo.findByUserIdAndPermissionId(userId, perm.getId());
        if (userPerm.isPresent()) {
            return userPerm.get().getValue();
        }

        // 2️⃣ Role default
        var rolePerm = rolePermRepo.findByRoleIdAndPermissionId(roleId, perm.getId());
        if (rolePerm.isPresent()) {
            return rolePerm.get().getValue();
        }

        return "NO";
    }

    public boolean hasPermission(String permissionCode) {
        return !"NO".equals(getPermissionValue(permissionCode));
    }
}
