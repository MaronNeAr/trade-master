package com.example.transaction.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantMarketVo {
    private String lastPrice;
    private String insideDisc;
    private String outsideDisc;
    private Timestamp createTime;
}
