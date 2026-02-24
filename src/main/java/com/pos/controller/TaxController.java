package com.pos.controller;

import com.pos.entity.Tax;
import com.pos.security.JwtContext;
import com.pos.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService service;

    @PostMapping
    public Tax create(@RequestBody Tax tax) {
        return service.create(JwtContext.outletId(), JwtContext.userId(), tax);
    }

    @GetMapping
    public List<Tax> list() {
        return service.list(JwtContext.outletId());
    }
}
