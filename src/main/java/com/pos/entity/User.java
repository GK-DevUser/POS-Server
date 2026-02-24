package com.pos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @Column(length = 36)
    private String id;

    // DB-managed AUTO_INCREMENT (NOT JPA-generated)
    @Column(name = "seq_no", insertable = false, updatable = false)
    private Long seqNo;

    @Column(name = "outlet_id", length = 36)
    private String outletId; // NULL for SUPER_ADMIN

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

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
