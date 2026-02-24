package com.pos.repository;

import com.pos.entity.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, String> {


    Optional<Item> findByIdAndOutletId(String id, String outletId);


    List<Item> findByOutletIdAndIsActiveTrue(String outletId);

    List<Item> findByOutletIdOrderBySeqNoAsc(String outletId);

    boolean existsByOutletIdAndShortCode(String outletId, String shortCode);

    List<Item> findByCategoryId(String categoryId, Sort sort);

    long deleteByIdInAndOutletId(List<String> ids, String outletId);
    long countByIdInAndOutletId(List<String> ids, String outletId);

    List<Item> findByCategoryIdIn(List<String> categoryIds);
}

