package com.pos.entity;


import java.io.Serializable;
import java.util.Objects;

public class UserPermissionId implements Serializable {

    private String userId;
    private Integer permissionId;

    public UserPermissionId() {
    }

    public UserPermissionId(String userId, Integer permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPermissionId)) return false;
        UserPermissionId that = (UserPermissionId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, permissionId);
    }
}
