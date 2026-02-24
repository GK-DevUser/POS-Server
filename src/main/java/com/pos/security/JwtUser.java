package com.pos.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {
    private final String uid;
    private final String outletId;
    private final Integer roleId;
    private final String roleName;

    public JwtUser(String uid, String outletId, Integer roleId, String roleName) {
        this.uid = uid;
        this.outletId = outletId;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Raw role names: SUPER_ADMIN, OUTLET_ADMIN, USER
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleName)); // NO ROLE_ prefix
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return uid;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // Getters
    public String getUid() { return uid; }
    public String getOutletId() { return outletId; }
    public Integer getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }
}
