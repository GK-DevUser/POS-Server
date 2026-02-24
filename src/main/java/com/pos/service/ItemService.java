package com.pos.service;

import com.pos.controller.AuthController;
import com.pos.dto.ApiResponse;
import com.pos.dto.ItemRequest;
import com.pos.dto.ItemResponse;
import com.pos.entity.Item;
import com.pos.repository.ItemRepository;
import com.pos.security.JwtContext;
import com.pos.util.UuidV7;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepo;
    private Logger logger = LogManager.getLogger(AuthController.class);

    @Transactional
    public List<ItemResponse> create(List<ItemRequest> reqs) {
        // ✅ Convert List<ItemRequest> → List<Item> (entities)
        List<Item> items = reqs.stream().map(this::createItemFromRequest).toList();

        // ✅ saveAll() gets List<Item> → perfect match!
        List<Item> savedItems = itemRepo.saveAll(items);

        // ✅ Convert List<Item> → List<ItemResponse>
        return savedItems.stream().map(this::toResponse).toList();
    }


    public Item createItemFromRequest(ItemRequest req) {

        Item i = new Item();
        i.setId(UuidV7.generate());
        i.setOutletId(JwtContext.outletId());
        i.setName(req.getName());
        i.setShortCode(req.getShortCode());
        i.setOnlineDisplayName(req.getOnlineDisplayName());
        i.setCategoryId(req.getCategoryId());
        i.setTaxId(req.getTaxId());
        i.setPrice(req.getPrice());
        i.setDescription(req.getDescription());
        i.setDietaryType(req.getDietaryType());
        i.setGstType(req.getGstType());
        i.setAllowDineIn(req.getAllowDineIn());
        i.setAllowTakeaway(req.getAllowTakeaway());
        i.setAllowDelivery(req.getAllowDelivery());
        i.setOnlineExpose(req.getOnlineExpose());
        i.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        i.setCreatedBy(JwtContext.userId());

//        itemRepo.save(i);
        return i;
    }

    @Transactional
    public ApiResponse<List<ItemResponse>> bulkUpdate(List<ItemRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return new ApiResponse<>("No items provided", false, null);
        }

        String outletId = JwtContext.outletId();
        String userId = JwtContext.userId();

        List<ItemResponse> updatedItems = new ArrayList<>();
        int skipped = 0;

        for (ItemRequest req : requests) {

            String id = req.getId();

            if (id == null || req == null) {
                skipped++;
                continue;
            }

            Optional<Item> opt = itemRepo.findByIdAndOutletId(id, outletId);

            if (opt.isEmpty()) {
                skipped++;
                continue;
            }

            Item item = opt.get();

            // --- update only non-null fields ---
            if (req.getName() != null) item.setName(req.getName());
            if (req.getShortCode() != null) item.setShortCode(req.getShortCode());
            if (req.getOnlineDisplayName() != null) item.setOnlineDisplayName(req.getOnlineDisplayName());
            if (req.getCategoryId() != null) item.setCategoryId(req.getCategoryId());
            if (req.getTaxId() != null) item.setTaxId(req.getTaxId());
            if (req.getPrice() != null) item.setPrice(req.getPrice());
            if (req.getDescription() != null) item.setDescription(req.getDescription());
            if (req.getDietaryType() != null) item.setDietaryType(req.getDietaryType());
            if (req.getGstType() != null) item.setGstType(req.getGstType());
            if (req.getAllowDineIn() != null) item.setAllowDineIn(req.getAllowDineIn());
            if (req.getAllowTakeaway() != null) item.setAllowTakeaway(req.getAllowTakeaway());
            if (req.getAllowDelivery() != null) item.setAllowDelivery(req.getAllowDelivery());
            if (req.getOnlineExpose() != null) item.setOnlineExpose(req.getOnlineExpose());
            if (req.getIsActive() != null) item.setIsActive(req.getIsActive());
            if (req.getDisplay_order() != null) item.setDisplayOrder(req.getDisplay_order());

            item.setModifiedBy(userId);

            updatedItems.add(toResponse(item));
        }

        String message = "Updated: " + updatedItems.size() + ", Skipped: " + skipped;
        return new ApiResponse<>(message, !updatedItems.isEmpty(), updatedItems);
    }


    public ItemResponse update(String id, ItemRequest req) {

        Item i = itemRepo.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        if (!i.getOutletId().equals(JwtContext.outletId())) {
            throw new RuntimeException("Access denied");
        }

        if (req.getName() != null) i.setName(req.getName());
        if (req.getShortCode() != null) i.setShortCode(req.getShortCode());
        if (req.getOnlineDisplayName() != null) i.setOnlineDisplayName(req.getOnlineDisplayName());
        if (req.getCategoryId() != null) i.setCategoryId(req.getCategoryId());
        if (req.getTaxId() != null) i.setTaxId(req.getTaxId());
        if (req.getPrice() != null) i.setPrice(req.getPrice());
        if (req.getDescription() != null) i.setDescription(req.getDescription());
        if (req.getDietaryType() != null) i.setDietaryType(req.getDietaryType());
        if (req.getGstType() != null) i.setGstType(req.getGstType());
        if (req.getAllowDineIn() != null) i.setAllowDineIn(req.getAllowDineIn());
        if (req.getAllowTakeaway() != null) i.setAllowTakeaway(req.getAllowTakeaway());
        if (req.getAllowDelivery() != null) i.setAllowDelivery(req.getAllowDelivery());
        if (req.getOnlineExpose() != null) i.setOnlineExpose(req.getOnlineExpose());
        if (req.getIsActive() != null) i.setIsActive(req.getIsActive());

        i.setModifiedBy(JwtContext.userId());
        itemRepo.save(i);

        return toResponse(i);
    }

    public ItemResponse get(String id) {

        Item i = itemRepo.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        if (!i.getOutletId().equals(JwtContext.outletId())) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(i);
    }

    public ApiResponse<List<ItemResponse>> listByCategoryId(String categoryId) {
        try {
            // 1. Input validation
            if (categoryId == null || categoryId.trim().isEmpty()) {
                logger.warn("Invalid categoryId: {}", categoryId);
                return new ApiResponse<>("Category ID cannot be empty", false, null);
            }

            // 2. Fetch from repository
            List<Item> items = itemRepo.findByCategoryId(categoryId, Sort.by(Sort.Direction.DESC, "createdAt"));

            // 3. Business logic
            List<ItemResponse> responses = items.stream().filter(Item::getIsActive).map(this::toResponse).toList();

            logger.info("Found {} active items for category: {}", responses.size(), categoryId);

            // ✅ POSITIVE RESPONSE - Same format
            return new ApiResponse<>("Items fetched successfully", true, responses);

        } catch (DataAccessException e) {
            logger.error("Database error fetching items for category {}: {}", categoryId, e.getMessage());
            return new ApiResponse<>("Failed to fetch items. Please try again later", false, null);

        } catch (Exception e) {
            logger.error("Unexpected error fetching items for category {}: ", categoryId, e);
            return new ApiResponse<>("Service temporarily unavailable", false, null);
        }
    }

    private void validateCategoryId(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            throw new IllegalArgumentException("CategoryId cannot be empty");
        }
    }

    public List<ItemResponse> list() {

        return itemRepo.findByOutletIdOrderBySeqNoAsc(JwtContext.outletId()).stream().map(this::toResponse).toList();
    }


    @Transactional
    public ApiResponse<Void> deleteBulk(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ApiResponse<>("No IDs provided", false, null);
        }

        String outletId = JwtContext.outletId();

        long existingCount = itemRepo.countByIdInAndOutletId(ids, outletId);

        if (existingCount == 0) {
            return new ApiResponse<>("No items found for this outlet", false, null);
        }

        long deletedCount = itemRepo.deleteByIdInAndOutletId(ids, outletId);

        return new ApiResponse<>(deletedCount + " items deleted successfully", true, null);
    }


    public void deactivate(String id) {

        Item i = itemRepo.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        if (!i.getOutletId().equals(JwtContext.outletId())) {
            throw new RuntimeException("Access denied");
        }

        i.setIsActive(false);
        i.setModifiedBy(JwtContext.userId());
        itemRepo.save(i);
    }

    private ItemResponse toResponse(Item i) {
        return ItemResponse.builder().id(i.getId()).seqNo(i.getSeqNo()).name(i.getName()).shortCode(i.getShortCode()).onlineDisplayName(i.getOnlineDisplayName()).categoryId(i.getCategoryId()).taxId(i.getTaxId()).price(i.getPrice()).description(i.getDescription()).dietaryType(i.getDietaryType() != null ? i.getDietaryType().name() : null).gstType(i.getGstType() != null ? i.getGstType().name() : null).allowDineIn(i.getAllowDineIn()).allowTakeaway(i.getAllowTakeaway()).allowDelivery(i.getAllowDelivery()).onlineExpose(i.getOnlineExpose()).isActive(i.getIsActive()).build();
    }
}
