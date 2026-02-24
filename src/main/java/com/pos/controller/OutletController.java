package com.pos.controller;

import com.pos.dto.OutletRequest;
import com.pos.dto.OutletResponse;
import com.pos.service.OutletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outlets")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class OutletController {

    private final OutletService outletService;

    @PostMapping
    public OutletResponse create(@RequestBody OutletRequest req) {
        return outletService.create(req);
    }

    @PutMapping("/{id}")
    public OutletResponse update(@PathVariable String id, @RequestBody OutletRequest req) {
        return outletService.update(id, req);
    }

    @GetMapping("/{id}")
    public OutletResponse get(@PathVariable String id) {
        return outletService.getById(id);
    }

    @GetMapping
    public List<OutletResponse> list() {
        return outletService.listAll();
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable String id) {
        outletService.deactivate(id);
    }
}
