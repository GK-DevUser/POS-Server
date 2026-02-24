package com.pos.repository;

import com.pos.entity.Permission;
import com.pos.entity.RolePermission;
import com.pos.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByCode(String code);
}


