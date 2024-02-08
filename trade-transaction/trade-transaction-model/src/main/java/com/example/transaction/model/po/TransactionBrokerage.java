package com.example.transaction.model.po;

import lombok.Data;

@Data
public class TransactionBrokerage {
    private Integer brokerageId;
    private String brokerageName;
    private String brokerageRegion;

    public TransactionBrokerage(Integer brokerageId, String brokerageName, String brokerageRegion) {
        this.brokerageId = brokerageId;
        this.brokerageName = brokerageName;
        this.brokerageRegion = brokerageRegion;
    }
}
