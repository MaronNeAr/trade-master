package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionBrokerage;

import java.util.List;

public interface TransactionBrokerageMapper {
     List<TransactionBrokerage> selectBrokerageList();
}
