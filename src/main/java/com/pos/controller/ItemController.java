package com.pos.controller;

import com.pos.dto.ApiResponse;
import com.pos.dto.ItemRequest;
import com.pos.dto.ItemResponse;
import com.pos.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @PostMapping
    public List<ItemResponse> create(@RequestBody List<ItemRequest> req) {
        return itemService.create(req);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @PutMapping("/bulk")
    public ApiResponse<List<ItemResponse>> bulkUpdate(@RequestBody List<ItemRequest> requests) {
        return itemService.bulkUpdate(requests);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable String id,
                               @RequestBody ItemRequest req) {
        return itemService.update(id, req);
    }

    @GetMapping("/{id}")
    public ItemResponse get(@PathVariable String id) {
        return itemService.get(id);
    }

    @GetMapping("/get_all")
    public List<ItemResponse> list() {
        return itemService.list();
    }


    @GetMapping
    public ApiResponse<List<ItemResponse>> list(@RequestParam(required = false) String categoryId) {
        return itemService.listByCategoryId(categoryId);
    }


    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable String id) {
        itemService.deactivate(id);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @DeleteMapping("/bulk")
    public ApiResponse<Void> deleteBulk(@RequestBody List<String> ids) {
        return itemService.deleteBulk(ids);
    }
}
