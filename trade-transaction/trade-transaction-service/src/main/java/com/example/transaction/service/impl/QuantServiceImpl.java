package com.example.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.base.utils.HttpClient;
import com.example.transaction.service.QuantService;
import org.springframework.stereotype.Service;

@Service
public class QuantServiceImpl implements QuantService {

    @Override
    public JSONObject rollingWindowQuant(String json) {
        String res = HttpClient.sendPostRequest("http://localhost:5000/quant/rolling-window", json);
        return JSONObject.parseObject(res);
    }

    @Override
    public JSONObject inOutsideDiscQuant(String json) {
        String res = HttpClient.sendPostRequest("http://localhost:5000/quant/in-outside-disc", json);
        return JSONObject.parseObject(res);
    }
}
