package com.pos.repository;

import com.pos.entity.RolePermission;
import com.pos.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    Optional<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}