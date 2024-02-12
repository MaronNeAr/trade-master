package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransactionPosition {

    private Integer positionId;
    private String userId;
    private String securityCode;
    private Integer positionQuantity;
    private BigDecimal positionCost;
    private String positionStatus;

    public TransactionPosition(Integer positionId, String userId, String securityCode, Integer positionQuantity, BigDecimal positionCost, String positionStatus) {
        this.positionId = positionId;
        this.userId = userId;
        this.securityCode = securityCode;
        this.positionQuantity = positionQuantity;
        this.positionCost = positionCost;
        this.positionStatus = positionStatus;
    }
}
