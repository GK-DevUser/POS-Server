package com.pos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "outlets")
@Getter
@Setter
public class Outlet {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "seq_no", insertable = false, updatable = false)
    private Long seqNo;

    @Column(nullable = false)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    private String gstin;

    private Boolean isActive = true;

    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "modified_by", length = 36)
    private String modifiedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", insertable = false, updatable = false)
    private LocalDateTime modifiedAt;
}
