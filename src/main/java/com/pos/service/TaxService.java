package com.pos.service;

import com.pos.entity.Tax;
import com.pos.repository.TaxRepository;
import com.pos.util.UuidV7;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxRepository repo;

    public Tax create(String outletId, String userId, Tax tax) {
        tax.setId(UuidV7.generate());
        tax.setOutletId(outletId);
        tax.setCreatedBy(userId);
        tax.setCreatedAt(LocalDateTime.now());
        return repo.save(tax);
    }

    public List<Tax> list(String outletId) {
        return repo.findByOutletIdAndIsActiveTrue(outletId);
    }
}
