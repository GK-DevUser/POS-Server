package com.pos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@IdClass(RolePermissionId.class)
public class RolePermission {

    @Id
    private Integer roleId;

    @Id
    private Integer permissionId;

    private String value;
}
