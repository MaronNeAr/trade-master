package com.example.transaction.model.vo;

import com.example.transaction.model.po.TransactionPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPositionVo {
    private Integer positionId;
    private String userId;
    private String securityCode;
    private String securityName;
    private Integer positionQuantity;
    private BigDecimal positionCost;
    private String positionStatus;
    private BigDecimal currentPrice;

    public TransactionPositionVo(TransactionPosition position, String securityName, BigDecimal currentPrice) {
        this.positionId = position.getPositionId();
        this.userId = position.getUserId();
        this.securityCode = position.getSecurityCode();
        this.securityName = securityName;
        this.positionQuantity = position.getPositionQuantity();
        this.positionCost = position.getPositionCost();
        this.positionStatus = position.getPositionStatus();
        this.currentPrice = currentPrice;
    }
}
