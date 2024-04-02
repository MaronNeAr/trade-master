package com.example.transaction.service;

import com.alibaba.fastjson.JSONObject;

public interface QuantService {
    JSONObject rollingWindowQuant(String json);
    JSONObject inOutsideDiscQuant(String json);
}
