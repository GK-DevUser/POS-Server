package com.pos.repository;

import com.pos.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByOutletIdOrderBySeqNoAsc(String outletId);

    Optional<Category> findByIdAndOutletId(String id, String outletId);

    long deleteByIdAndOutletId(String id, String outletId);


    long deleteByIdInAndOutletId(List<String> ids, String outletId);
    long countByIdInAndOutletId(List<String> ids, String outletId);

}
