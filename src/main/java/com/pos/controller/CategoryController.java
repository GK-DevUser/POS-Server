package com.pos.controller;

import com.pos.dto.ApiResponse;
import com.pos.dto.CategoryRequest;
import com.pos.dto.CategoryResponse;
import com.pos.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @PostMapping
    public CategoryResponse create(@RequestBody CategoryRequest req) {
        return categoryService.create(req);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @PutMapping("/bulk")
    public ApiResponse<List<CategoryResponse>> bulkUpdate(
            @RequestBody List<CategoryRequest> requests) {
        return categoryService.bulkUpdate(requests);
    }


    @GetMapping("/{id}")
    public CategoryResponse get(@PathVariable String id) {
        return categoryService.get(id);
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>>  list() {
        return categoryService.list();
    }

    @GetMapping("/listByIdName")
    public ApiResponse<List<CategoryResponse>>  listByIdName() {
        return categoryService.listByIdName();
    }


    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OUTLET_ADMIN')")
    @DeleteMapping("/bulk")
    public ApiResponse<Void> deleteBulk(@RequestBody List<String> ids) {
        return categoryService.deleteBulk(ids);
    }

}
