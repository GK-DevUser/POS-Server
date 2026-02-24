package com.pos.repository;


import com.pos.entity.UserPermission;
import com.pos.entity.UserPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPermissionRepository extends JpaRepository<UserPermission, UserPermissionId> {
    Optional<UserPermission> findByUserIdAndPermissionId(String userId, Integer permissionId);
}
