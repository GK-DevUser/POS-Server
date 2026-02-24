package com.pos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter @Setter
public class OrderItem {

    @Id
    @Column(length = 36)
    private String id;

    // DB-managed AUTO_INCREMENT (NOT JPA-generated)
    @Column(name = "seq_no", insertable = false, updatable = false)
    private Long seqNo;

    @Column(name = "order_id", length = 36, nullable = false)
    private String orderId;  // foreign key to Order.id

    @Column(name = "item_id", length = 36)
    private String itemId;  // optional reference to item

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "disc_per")
    private BigDecimal discPer;  // discount percentage

    @Column(name = "disc_amount")
    private BigDecimal discAmount;

    @Column
    private BigDecimal mrp;

    @Column
    private BigDecimal rate;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal tax;

    @Column(name = "rate_with_out_tax")
    private BigDecimal rateWithoutTax;

    @Column(name = "mrp_with_out_tax")
    private BigDecimal mrpWithoutTax;

    @Column(length = 100)
    private String notes;

    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "modified_by", length = 36)
    private String modifiedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", insertable = false, updatable = false)
    private LocalDateTime modifiedAt;
}
