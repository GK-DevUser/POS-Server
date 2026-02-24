package com.pos.repository;

import com.pos.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    // Fetch all items for a specific order
    List<OrderItem> findByOrderId(String orderId);

    // Optional: Delete all items for an order (useful when deleting order)
    void deleteByOrderId(String orderId);
}
