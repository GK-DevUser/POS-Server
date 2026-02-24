package com.pos.controller;

import com.pos.dto.*;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest req) {
        return userService.create(req);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable String id,
                               @RequestBody UserCreateRequest req) {
        return userService.update(id, req);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable String id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.listByOutlet();
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable String id) {
        userService.deactivate(id);
    }
}
