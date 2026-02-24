package com.pos.repository;

import com.pos.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaxRepository extends JpaRepository<Tax, String> {

    List<Tax> findByOutletIdAndIsActiveTrue(String outletId);
}
