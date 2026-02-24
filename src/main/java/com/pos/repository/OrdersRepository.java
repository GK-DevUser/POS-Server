package com.pos.repository;

import com.pos.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {

    // Find all orders for an outlet
    List<Orders> findByOutletId(String outletId);

    // Find the maximum order_no for an outlet to generate incremental order_no
    @Query("SELECT MAX(o.orderNo) FROM Orders o WHERE o.outletId = :outletId")
    Long findMaxOrderNoByOutletId(@Param("outletId") String outletId);
}
