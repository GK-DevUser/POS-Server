package com.pos.dto;

import com.pos.entity.AmountOperator;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderRequest {

    private String id; // for search
    private String orderNo; // for search
    private String outletId;      // required
    private String tableNo;
    private String cId;           // customer id
    private String date;   // order date
    private String type;          // e.g., dine-in, takeaway
    private String paymentType;   // e.g., cash, card
    private String status;        // e.g., pending, completed
    private Integer noOfPrint;
    private Integer noOfPerson;
    private Integer amount;
    private String notes;
    private String cancelReason;
    private String paymentReason;
    private String modifiedBy;
    private List<OrderItemRequest> items;

    // 🔹 MULTI VALUE SEARCH
    private List<String> orderTypes;
    // 🔹 EQUATION BASED SEARCH
    private AmountOperator amountOperator;

    // 🔹 DATE AS STRING
    private String fromDate;   // yyyy-MM-dd
    private String toDate;     // yyyy-MM-dd

}
