package com.pos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "taxes")
@Getter @Setter
public class Tax {

    @Id
    @Column(length = 36)
    private String id;

    // DB-managed AUTO_INCREMENT (NOT JPA-generated)
    @Column(name = "seq_no", insertable = false, updatable = false)
    private Long seqNo;

    @Column(name = "outlet_id", nullable = false, length = 36)
    private String outletId;

    @Column(nullable = false)
    private String name;

    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;

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
}
