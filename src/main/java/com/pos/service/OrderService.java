package com.pos.service;

import com.pos.controller.AuthController;
import com.pos.dto.*;
import com.pos.entity.Orders;
import com.pos.entity.OrderItem;
import com.pos.repository.OrderItemRepository;
import com.pos.repository.OrdersRepository;
import com.pos.security.JwtContext;
import com.pos.util.UuidV7;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    @Autowired
    private final OrdersRepository orderRepo;
    @Autowired
    private final OrderItemRepository orderItemRepo;

    @Autowired
    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd


    public ApiResponse<OrderResponse> createOrder(OrderRequest req) {
        try {
            Orders order = new Orders();
            order.setId(UuidV7.generate());
            order.setOutletId(JwtContext.outletId());

            // Incremental order_no per outlet
            Long maxOrderNo = orderRepo.findMaxOrderNoByOutletId(JwtContext.outletId());
            order.setOrderNo(maxOrderNo != null ? maxOrderNo + 1 : 1L);

            // Map request fields
            order.setTableNo(req.getTableNo());
            order.setC_id(req.getCId());
            order.setDate(req.getDate());
            order.setType(req.getType());
            order.setPaymentType(req.getPaymentType());
            order.setStatus(req.getStatus());
            order.setNoOfPrint(req.getNoOfPrint());
            order.setNoOfPerson(req.getNoOfPerson());
            order.setAmount(req.getAmount());
            order.setNotes(req.getNotes());
            order.setCancelReason(req.getCancelReason());
            order.setPaymentReason(req.getPaymentReason());
            order.setCreatedBy(JwtContext.userId());
            order.setModifiedBy(JwtContext.userId());

            // Save order first
            orderRepo.save(order);

            // Save items
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            if (req.getItems() != null && !req.getItems().isEmpty()) {
                for (OrderItemRequest itemReq : req.getItems()) {
                    OrderItem item = new OrderItem();
                    item.setId(UuidV7.generate());
                    item.setOrderId(order.getId());
                    item.setItemId(itemReq.getItemId());
                    item.setName(itemReq.getName());
                    item.setQuantity(itemReq.getQuantity());
                    item.setDiscPer(itemReq.getDiscPer());
                    item.setDiscAmount(itemReq.getDiscAmount());
                    item.setMrp(itemReq.getMrp());
                    item.setRate(itemReq.getRate());
                    item.setAmount(itemReq.getAmount());
                    item.setTax(itemReq.getTax());
                    item.setRateWithoutTax(itemReq.getRateWithoutTax());
                    item.setMrpWithoutTax(itemReq.getMrpWithoutTax());
                    item.setNotes(itemReq.getNotes());
                    item.setCreatedBy(JwtContext.userId());
                    item.setModifiedBy(JwtContext.userId());

                    orderItemRepo.save(item);

                    itemResponses.add(toItemResponse(item));
                }
            }

            OrderResponse response = toOrderResponse(order);
            response.setItems(itemResponses);

            logger.info("Create OrderResponse: {}", response);

            return new ApiResponse<>("Order created successfully", true, response);

        } catch (Exception e) {
            logger.error("createOrder", e);
            return new ApiResponse<>("Failed to create order: " + e.getMessage(), false, null);
        }
    }


    // Get order by ID
    public ApiResponse<OrderResponse> getOrder(String id) {
        try {
            // Fetch order by ID
            Orders order = orderRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Fetch items for this order
            List<OrderItem> items = orderItemRepo.findByOrderId(id);

            // Map items to response DTO
            List<OrderItemResponse> itemResponses = items.stream()
                    .map(this::toItemResponse)
                    .toList();

            // Map order to response DTO
            OrderResponse response = toOrderResponse(order);
            response.setItems(itemResponses);

            return new ApiResponse<>("Order fetched successfully", true, response);

        } catch (Exception e) {
            logger.error("getOrder", e);
            return new ApiResponse<>("Failed to fetch order: " + e.getMessage(), false, null);
        }
    }


    // Get all orders for an outlet
    public ApiResponse<List<OrderResponse>> getOrdersByOutlet(String outletId) {
        try {
            // fallback to JWT outletId
            if (outletId == null || outletId.isBlank()) {
                outletId = JwtContext.outletId();
            }
            List<Orders> orders = orderRepo.findByOutletId(outletId);
            List<OrderResponse> orderResponses = orders.stream()
                    .map(this::toOrderResponse)
                    .toList();
            return new ApiResponse<>("Orders fetched successfully", true, orderResponses);
        } catch (Exception e) {
            logger.error("getOrdersByOutlet", e);
            return new ApiResponse<>("Failed to fetch orders: " + e.getMessage(), false, null);
        }
    }

    // Update an order
    @Transactional
    public ApiResponse<OrderResponse> updateOrder(String id, OrderRequest req) {
        try {
            // Fetch existing order
            Orders order = orderRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Update order fields
            order.setTableNo(req.getTableNo());
            order.setC_id(req.getCId());
            order.setDate(req.getDate() != null ? req.getDate() : order.getDate());
            order.setType(req.getType());
            order.setPaymentType(req.getPaymentType());
            order.setStatus(req.getStatus());
            order.setNoOfPrint(req.getNoOfPrint());
            order.setNoOfPerson(req.getNoOfPerson());
            order.setAmount(req.getAmount());
            order.setNotes(req.getNotes());
            order.setModifiedBy(JwtContext.userId());
            order.setPaymentReason(req.getPaymentReason());
            order.setCancelReason(req.getCancelReason());

            orderRepo.save(order);

            // Update items
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            if (req.getItems() != null) {
                // Delete existing items for this order
                orderItemRepo.deleteByOrderId(id);

                // Add updated items
                for (OrderItemRequest itemReq : req.getItems()) {
                    OrderItem item = new OrderItem();
                    item.setId(UuidV7.generate());
                    item.setOrderId(id);
                    item.setItemId(itemReq.getItemId());
                    item.setName(itemReq.getName());
                    item.setQuantity(itemReq.getQuantity());
                    item.setDiscPer(itemReq.getDiscPer());
                    item.setDiscAmount(itemReq.getDiscAmount());
                    item.setMrp(itemReq.getMrp());
                    item.setRate(itemReq.getRate());
                    item.setAmount(itemReq.getAmount());
                    item.setTax(itemReq.getTax());
                    item.setRateWithoutTax(itemReq.getRateWithoutTax());
                    item.setMrpWithoutTax(itemReq.getMrpWithoutTax());
                    item.setNotes(itemReq.getNotes());
                    item.setCreatedBy(JwtContext.userId());
                    item.setModifiedBy(JwtContext.userId());

                    orderItemRepo.save(item);

                    itemResponses.add(toItemResponse(item));
                }
            }

            // Prepare response
            OrderResponse response = toOrderResponse(order);
            response.setItems(itemResponses);

            return new ApiResponse<>("Order updated successfully", true, response);

        } catch (Exception e) {
            logger.error("updateOrder", e);
            return new ApiResponse<>("Failed to update order: " + e.getMessage(), false, null);
        }
    }


    public ApiResponse<List<OrderResponse>> searchOrders(OrderRequest req) {

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);

            List<Predicate> predicates = new ArrayList<>();

            /* ================= BASIC FILTERS ================= */

            if (req.getId() != null && !req.getId().isBlank()) {
                predicates.add(cb.equal(root.get("id"), req.getId()));
            }

            if (req.getOrderNo() != null && !req.getOrderNo().isBlank()) {
                predicates.add(cb.equal(root.get("orderNo"), req.getOrderNo()));
            }

            if (req.getTableNo() != null && !req.getTableNo().isBlank()) {
                predicates.add(cb.equal(root.get("tableNo"), req.getTableNo()));
            }

            if (req.getPaymentType() != null && !req.getPaymentType().isBlank()) {
                predicates.add(cb.equal(root.get("paymentType"), req.getPaymentType()));
            }

            if (req.getStatus() != null && !req.getStatus().isBlank()) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            }

            /* ================= ORDER TYPE (STRING IN QUERY) ================= */

            if (req.getOrderTypes() != null && !req.getOrderTypes().isEmpty()) {
                predicates.add(root.get("type").in(req.getOrderTypes().stream().map(String::toUpperCase).toList()));
            }

            /* ================= DATE RANGE (STRING INPUT) ================= */

            if ((req.getFromDate() != null && !req.getFromDate().isBlank()) ||
                    (req.getToDate() != null && !req.getToDate().isBlank())) {
                Path<String> datePath = root.get("date");
                if (req.getFromDate() != null && !req.getFromDate().isBlank()
                        && req.getToDate() != null && !req.getToDate().isBlank()) {
                    predicates.add(cb.between(datePath, req.getFromDate(), req.getToDate()));
                } else if (req.getFromDate() != null && !req.getFromDate().isBlank()) {
                    predicates.add(cb.greaterThanOrEqualTo(datePath, req.getFromDate()));
                } else {
                    predicates.add(cb.lessThanOrEqualTo(datePath, req.getToDate()));
                }
            }

            /* ================= AMOUNT EQUATION SEARCH ================= */

            if (req.getAmount() != null && req.getAmountOperator() != null) {
                Path<Integer> amountPath = root.get("amount");

                switch (req.getAmountOperator()) {
                    case EQ -> predicates.add(cb.equal(amountPath, req.getAmount()));
                    case GTE -> predicates.add(cb.greaterThanOrEqualTo(amountPath, req.getAmount()));
                    case LTE -> predicates.add(cb.lessThanOrEqualTo(amountPath, req.getAmount()));
                    case GT -> predicates.add(cb.greaterThan(amountPath, req.getAmount()));
                    case LT -> predicates.add(cb.lessThan(amountPath, req.getAmount()));
                }
            }

            /* ================= FINAL QUERY ================= */

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            cq.orderBy(cb.desc(root.get("orderNo")));

            List<Orders> orders = entityManager.createQuery(cq).getResultList();

            List<OrderResponse> responses = new ArrayList<>();
            for (Orders order : orders) {
                OrderResponse response = toOrderResponse(order);
                List<OrderItem> items = orderItemRepo.findByOrderId(order.getId());
                // Map items to response DTO
                List<OrderItemResponse> itemResponses = items.stream()
                        .map(this::toItemResponse)
                        .toList();
                response.setItems(itemResponses);
                responses.add(response);
            }

            return new ApiResponse<>("Orders fetched successfully", true, responses);

        } catch (Exception e) {
            logger.error("searchOrders", e);
            return new ApiResponse<>("Search failed: " + e.getMessage(), false, null);
        }
    }

    public ApiResponse<List<OrderResponse>> searchOrdersOld(OrderRequest req) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);

            List<Predicate> predicates = new ArrayList<>();

            if (req.getId() != null && !req.getId().isBlank()) {
                predicates.add(cb.equal(root.get("id"), req.getId()));
            }

            if (req.getOrderNo() != null && !req.getOrderNo().isBlank()) {
                predicates.add(cb.equal(root.get("orderNo"), req.getOrderNo()));
            }

            if (req.getTableNo() != null && !req.getTableNo().isBlank()) {
                predicates.add(cb.equal(root.get("tableNo"), req.getTableNo()));

                // 🔥 rule: table search without status
                if (req.getStatus() == null) {
                    predicates.add(cb.notEqual(root.get("status"), "completed"));
                }
            }

            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            }

            if (req.getDate() != null && !req.getDate().isBlank()) {
                // DB stores DATE only → direct equality
                predicates.add(cb.equal(root.get("date"), req.getDate()));
                // orderDate is DATE / LocalDate / String (mapped to DATE)
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            cq.orderBy(cb.desc(root.get("orderNo")));

            List<Orders> orders = entityManager
                    .createQuery(cq)
                    .getResultList();

            // 🔁 Same mapping logic as getOrder()
            List<OrderResponse> responses = new ArrayList<>();

            for (Orders order : orders) {

                List<OrderItem> items = orderItemRepo.findByOrderId(order.getId());

                List<OrderItemResponse> itemResponses = items.stream()
                        .map(this::toItemResponse)
                        .toList();

                OrderResponse response = toOrderResponse(order);
                response.setItems(itemResponses);

                responses.add(response);
            }

            return new ApiResponse<>("Orders fetched successfully", true, responses);

        } catch (Exception e) {
            logger.error("searchOrdersOld", e);
            return new ApiResponse<>("Failed to fetch orders: " + e.getMessage(), false, null);
        }
    }

    @Transactional
    public ApiResponse<Void> deleteOrders(List<String> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return new ApiResponse<>("No order IDs provided", false, null);
            }

            // Fetch orders to check existence
            List<Orders> orders = orderRepo.findAllById(ids);
            if (orders.isEmpty()) {
                return new ApiResponse<>("No matching orders found", false, null);
            }

            // Delete items for each order
            ids.forEach(orderItemRepo::deleteByOrderId);

            // Delete orders
            orderRepo.deleteAll(orders);

            return new ApiResponse<>("Orders deleted successfully", true, null);

        } catch (Exception e) {
            logger.error("deleteOrders", e);
            return new ApiResponse<>("Failed to delete orders: " + e.getMessage(), false, null);
        }
    }


    private OrderResponse toOrderResponse(Orders order) {
        return OrderResponse.builder()
                .id(order.getId())
                .seqNo(order.getSeqNo())
                .orderNo(order.getOrderNo())
                .outletId(order.getOutletId())
                .tableNo(order.getTableNo())
                .cId(order.getC_id())
                .date(order.getDate())
                .type(order.getType())
                .paymentType(order.getPaymentType())
                .status(order.getStatus())
                .noOfPrint(order.getNoOfPrint())
                .noOfPerson(order.getNoOfPerson())
                .amount(order.getAmount())
                .notes(order.getNotes())
                .cancelReason(order.getCancelReason())
                .paymentReason(order.getPaymentReason())
                .modifiedBy(order.getModifiedBy())
                .items(order.getItems() != null ? order.getItems().stream().map(this::toItemResponse).toList() : null)
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .seqNo(item.getSeqNo())
                .itemId(item.getItemId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .discPer(item.getDiscPer())
                .discAmount(item.getDiscAmount())
                .mrp(item.getMrp())
                .rate(item.getRate())
                .amount(item.getAmount())
                .tax(item.getTax())
                .rateWithoutTax(item.getRateWithoutTax())
                .mrpWithoutTax(item.getMrpWithoutTax())
                .notes(item.getNotes())
                .build();
    }

}
