package com.pos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "user_permissions")
@Getter
@Setter
@IdClass(UserPermissionId.class)
public class UserPermission {

    @Id
    private String userId;

    @Id
    private Integer permissionId;

    private String value;
}
