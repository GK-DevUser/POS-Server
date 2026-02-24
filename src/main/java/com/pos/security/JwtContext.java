package com.pos.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class JwtContext {

    private JwtContext() {
    }

    private static JwtUser principal() {
        return (JwtUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public static String userId() {
        return principal().getUid();
    }

    public static String outletId() {
        return principal().getOutletId();
    }

    public static String outletIdRequired() {
        if (principal().getOutletId() == null) {
            throw new RuntimeException("Outlet context required");
        }
        return principal().getOutletId();
    }

    public static String roleName() {
        return principal().getRoleName();
    }


    public static Integer roleId() {
        return principal().getRoleId();
    }


    public static boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(roleName());
    }
}
