package com.example.transaction.model.po;

import lombok.Data;

@Data
public class TransactionSecurity {

    private Integer securityId;
    private String securityCode;
    private String securityName;
    private String securityType;
    private String securitySubtype;
    private String securityExchange;
    private Boolean sse50;
    private Boolean hs300;
    private Boolean csi500;

    public TransactionSecurity(Integer securityId, String securityCode, String securityName, String securityType, String securitySubtype, String securityExchange, Boolean sse50, Boolean hs300, Boolean csi500) {
        this.securityId = securityId;
        this.securityCode = securityCode;
        this.securityName = securityName;
        this.securityType = securityType;
        this.securitySubtype = securitySubtype;
        this.securityExchange = securityExchange;
        this.sse50 = sse50;
        this.hs300 = hs300;
        this.csi500 = csi500;
    }
}
