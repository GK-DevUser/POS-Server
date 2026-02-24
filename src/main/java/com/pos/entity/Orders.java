package com.pos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders") // `order` is a reserved keyword in SQL, so backticks are needed
@Getter @Setter
public class Orders {

    @Id
    @Column(length = 36)
    private String id;

    // DB-managed AUTO_INCREMENT (NOT JPA-generated)
    @Column(name = "seq_no", insertable = false, updatable = false)
    private Long seqNo;

    @Column(name = "order_no", nullable = false)
    private Long orderNo;

    @Column(name = "outlet_id", length = 36)
    private String outletId;

    @Column(name = "c_id", length = 36)
    private String c_id; // assuming c_id is customer reference

    @Column(length = 45)
    private String type;

    @Column(name = "date")
    private String date;

    @Column(name = "payment_type", length = 45)
    private String paymentType;

    @Column(length = 45)
    private String status;

    @Column(name = "no_of_print")
    private Integer noOfPrint;

    @Column(name = "no_of_person")
    private Integer noOfPerson;

    @Column
    private Integer amount;

    @Column(length = 100)
    private String notes;

    @Column(name = "table_no", length = 10)
    private String tableNo;

    @Column(length = 100)
    private String cancelReason;

    @Column(length = 100)
    private String paymentReason;

    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "modified_by", length = 36)
    private String modifiedBy;

    // 👇 DB-managed fields
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", insertable = false, updatable = false)
    private LocalDateTime modifiedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<OrderItem> items;
}
