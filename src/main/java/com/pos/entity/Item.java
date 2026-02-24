package com.pos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@Setter
public class Item {

    @Id
    @Column(length = 36)
    private String id;

    // DB-managed AUTO_INCREMENT (NOT JPA-generated)
    @Column(name = "seq_no", insertable = false, updatable = false)
    private Long seqNo;

    @Column(name = "outlet_id", nullable = false, length = 36)
    private String outletId;

    @Column(name = "category_id", nullable = false, length = 36)
    private String categoryId;

    @Column(name = "tax_id", length = 36)
    private String taxId;

    @Column(nullable = false)
    private String name;

    @Column(name = "short_code", nullable = false)
    private String shortCode;

    @Column(name = "online_display_name")
    private String onlineDisplayName;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "dietary_type")
    private DietaryType dietaryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gst_type")
    private GstType gstType;

    private Boolean allowDineIn = true;
    private Boolean allowTakeaway = true;
    private Boolean allowDelivery = true;
    private Boolean onlineExpose = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "modified_by", length = 36)
    private String modifiedBy;

    // 👇 DB managed fields
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", insertable = false, updatable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

}
