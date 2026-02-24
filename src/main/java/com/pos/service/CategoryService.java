package com.pos.service;

import com.pos.dto.ApiResponse;
import com.pos.dto.CategoryRequest;
import com.pos.dto.CategoryResponse;
import com.pos.dto.ItemResponse;
import com.pos.entity.Category;
import com.pos.entity.Item;
import com.pos.repository.CategoryRepository;
import com.pos.repository.ItemRepository;
import com.pos.security.JwtContext;
import com.pos.util.UuidV7;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    @Autowired
    private final ItemRepository itemRepository;

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    public CategoryResponse create(CategoryRequest req) {

        Category c = new Category();
        c.setId(UuidV7.generate());
        c.setOutletId(JwtContext.outletId());
        c.setName(req.getName());
        c.setOnlineDisplayName(req.getOnlineDisplayName());
        c.setDisplayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0);
        c.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        c.setCreatedBy(JwtContext.userId());

        categoryRepo.save(c);
        return toResponse(c);
    }

    @Transactional
    public ApiResponse<List<CategoryResponse>> bulkUpdate(List<CategoryRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return new ApiResponse<>("No categories provided", false, null);
        }

        String outletId = JwtContext.outletId();
        String userId = JwtContext.userId();

        List<CategoryResponse> updated = new ArrayList<>();
        int skipped = 0;

        for (CategoryRequest req : requests) {

            if (req.getId() == null) {
                skipped++;
                continue;
            }

            Optional<Category> opt = categoryRepo.findByIdAndOutletId(req.getId(), outletId);

            if (opt.isEmpty()) {
                skipped++;
                continue;
            }

            Category c = opt.get();

            if (req.getName() != null) {
                c.setName(req.getName());
            }
            if (req.getOnlineDisplayName() != null) {
                c.setOnlineDisplayName(req.getOnlineDisplayName());
            }
            if (req.getDisplayOrder() != null) {
                c.setDisplayOrder(req.getDisplayOrder());
            }
            if (req.getIsActive() != null) {
                c.setIsActive(req.getIsActive());
            }

            c.setModifiedBy(userId);

            updated.add(toResponse(c)); // safe
        }

        return new ApiResponse<>(
                "Updated: " + updated.size() + ", Skipped: " + skipped,
                !updated.isEmpty(),
                updated
        );
    }


    public CategoryResponse update(String id, CategoryRequest req) {

        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // outlet isolation
        if (!c.getOutletId().equals(JwtContext.outletId())) {
            throw new RuntimeException("Access denied");
        }

        if (req.getName() != null) {
            c.setName(req.getName());
        }
        if (req.getOnlineDisplayName() != null) {
            c.setOnlineDisplayName(req.getOnlineDisplayName());
        }
        if (req.getDisplayOrder() != null) {
            c.setDisplayOrder(req.getDisplayOrder());
        }
        if (req.getIsActive() != null) {
            c.setIsActive(req.getIsActive());
        }

        c.setModifiedBy(JwtContext.userId());
        categoryRepo.save(c);

        return toResponse(c);
    }

    public CategoryResponse get(String id) {

        Category c = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        if (!c.getOutletId().equals(JwtContext.outletId())) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(c);
    }

    public ApiResponse<List<CategoryResponse>> list() {
        try {
            // Fetch categories for the outlet
            List<Category> categories = categoryRepo.findByOutletIdOrderBySeqNoAsc(JwtContext.outletId());

            if (categories.isEmpty()) {
                return new ApiResponse<>("No categories found", false, List.of());
            }

            // Fetch items for these categories
            List<String> categoryIds = categories.stream()
                    .map(Category::getId)
                    .toList();
            List<Item> items = itemRepository.findByCategoryIdIn(categoryIds);

            // Group items by categoryId
            Map<String, List<ItemResponse>> itemsByCategory = items.stream()
                    .map(this::toItemResponse)
                    .collect(Collectors.groupingBy(ItemResponse::getCategoryId));

            // Map categories to responses
            List<CategoryResponse> categoryResponses = categories.stream()
                    .map(category -> {
                        List<ItemResponse> itemList = itemsByCategory.getOrDefault(category.getId(), List.of());
                        return toResponse(category, itemList);
                    })
                    .toList();

            return new ApiResponse<>("Categories fetched successfully", true, categoryResponses);

        } catch (Exception e) {
            logger.error("", e);
            return new ApiResponse<>("Failed to fetch categories: " + e.getMessage(), false, null);
        }
    }


    public ApiResponse<List<CategoryResponse>> listByIdName() {
        try {
            // Fetch categories for the outlet
            List<Category> categories =
                    categoryRepo.findByOutletIdOrderBySeqNoAsc(JwtContext.outletId());

            if (categories.isEmpty()) {
                return new ApiResponse<>("No categories found", false, List.of());
            }

            // Map categories ONLY (no items)
            List<CategoryResponse> categoryResponses = categories.stream()
                    .map(this::toResponse)   // simple category mapper
                    .toList();

            return new ApiResponse<>("Categories fetched successfully", true, categoryResponses);

        } catch (Exception e) {
            logger.error("", e);
            return new ApiResponse<>("Failed to fetch categories: " + e.getMessage(), false, null);
        }
    }


    public void deactivate(String id) {

        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!c.getOutletId().equals(JwtContext.outletId())) {
            throw new RuntimeException("Access denied");
        }

        c.setIsActive(false);
        c.setModifiedBy(JwtContext.userId());
        categoryRepo.save(c);
    }

    @Transactional
    public void delete(String id) {
        String outletId = JwtContext.outletId();
        long deleted = categoryRepo.deleteByIdAndOutletId(id, outletId);
        if (deleted == 0) {
            throw new RuntimeException("Category not found or access denied");
        }
    }

    @Transactional
    public ApiResponse<Void> deleteBulk(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ApiResponse<>("No IDs provided", false, null);
        }

        String outletId = JwtContext.outletId();

        long existingCount = categoryRepo.countByIdInAndOutletId(ids, outletId);

        if (existingCount == 0) {
            return new ApiResponse<>(
                    "No categories found for this outlet",
                    false,
                    null
            );
        }

        long deletedCount = categoryRepo.deleteByIdInAndOutletId(ids, outletId);

        return new ApiResponse<>(
                deletedCount + " categories deleted successfully",
                true,
                null
        );
    }

    private CategoryResponse toResponse(Category category, List<ItemResponse> items) {
        return CategoryResponse.builder()
                .id(category.getId())
                .seqNo(category.getSeqNo())
                .name(category.getName())
                .onlineDisplayName(category.getOnlineDisplayName())
                .displayOrder(category.getDisplayOrder())
                .isActive(category.getIsActive())
                .items(items)
                .build();
    }

    private ItemResponse toItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .seqNo(item.getSeqNo())
                .name(item.getName())
                .shortCode(item.getShortCode())
                .onlineDisplayName(item.getOnlineDisplayName())
                .categoryId(item.getCategoryId())
                .taxId(item.getTaxId())
                .price(item.getPrice())
                .description(item.getDescription())
                .dietaryType(item.getDietaryType().toString())
                .gstType(item.getGstType().toString())
                .allowDineIn(item.getAllowDineIn())
                .allowTakeaway(item.getAllowTakeaway())
                .allowDelivery(item.getAllowDelivery())
                .onlineExpose(item.getOnlineExpose())
                .isActive(item.getIsActive())
                .display_order(item.getDisplayOrder())
                .build();
    }



    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .seqNo(c.getSeqNo())
                .name(c.getName())
                .onlineDisplayName(c.getOnlineDisplayName())
                .displayOrder(c.getDisplayOrder())
                .isActive(c.getIsActive())
                .build();
    }
}
