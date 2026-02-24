package com.pos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends AuditableEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    private UUID orderId;

    private BigDecimal amount;

    @Column(nullable = false)
    private String mode; // CASH, CARD, UPI

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
