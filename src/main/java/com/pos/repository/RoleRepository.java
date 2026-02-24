package com.pos.repository;

import com.pos.entity.Outlet;
import com.pos.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
}