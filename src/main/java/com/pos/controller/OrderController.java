package com.pos.controller;

import com.pos.dto.ApiResponse;
import com.pos.dto.OrderRequest;
import com.pos.dto.OrderResponse;
import com.pos.entity.Orders;
import com.pos.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;


    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest req) {
        return orderService.createOrder(req);
    }


    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }


    // Read all by outlet
    @GetMapping("/outlet")
    public ApiResponse<List<OrderResponse>> getOrdersByOutlet(@RequestParam(required = false) String outletId) {
        return orderService.getOrdersByOutlet(outletId);
    }


    // Update
    @PutMapping("/{id}")
    public ApiResponse<OrderResponse> updateOrder(
            @PathVariable String id,
            @RequestBody OrderRequest req) {
        return orderService.updateOrder(id, req);
    }

    @PostMapping("/search")
    public ApiResponse<List<OrderResponse>> searchOrders(@RequestBody OrderRequest req) {
        return orderService.searchOrders(req);
    }


    // Delete multiple orders
    @DeleteMapping
    public ApiResponse<Void> deleteOrders(@RequestBody List<String> ids) {
        return orderService.deleteOrders(ids);
    }

}
