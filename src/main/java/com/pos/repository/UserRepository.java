package com.pos.repository;

import com.pos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMobile(String mobile);

    List<User> findByOutletId(String outletId);
}
