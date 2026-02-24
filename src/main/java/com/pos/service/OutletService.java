package com.pos.service;

import com.pos.dto.OutletRequest;
import com.pos.dto.OutletResponse;
import com.pos.entity.Outlet;
import com.pos.repository.OutletRepository;
import com.pos.security.JwtContext;
import com.pos.util.UuidV7;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutletService {

    private final OutletRepository outletRepository;

    public OutletResponse create(OutletRequest req) {
        Outlet outlet = new Outlet();
        outlet.setId(UuidV7.generate());
        outlet.setName(req.getName());
        outlet.setDisplayName(req.getDisplayName());
        outlet.setGstin(req.getGstin());
        outlet.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        outlet.setCreatedBy(JwtContext.userId());

        outletRepository.save(outlet);
        return toResponse(outlet);
    }

    public OutletResponse update(String id, OutletRequest req) {
        Outlet outlet = outletRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Outlet not found"));

        outlet.setName(req.getName());
        outlet.setDisplayName(req.getDisplayName());
        outlet.setGstin(req.getGstin());
        outlet.setIsActive(req.getIsActive());
        outlet.setModifiedBy(JwtContext.userId());

        outletRepository.save(outlet);
        return toResponse(outlet);
    }

    public OutletResponse getById(String id) {
        return outletRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Outlet not found"));
    }

    public List<OutletResponse> listAll() {
        return outletRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deactivate(String id) {
        Outlet outlet = outletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outlet not found"));

        outlet.setIsActive(false);
        outlet.setModifiedBy(JwtContext.userId());
        outletRepository.save(outlet);
    }

    private OutletResponse toResponse(Outlet o) {
        return OutletResponse.builder()
                .id(o.getId())
                .seqNo(o.getSeqNo())
                .name(o.getName())
                .displayName(o.getDisplayName())
                .gstin(o.getGstin())
                .isActive(o.getIsActive())
                .build();
    }
}
