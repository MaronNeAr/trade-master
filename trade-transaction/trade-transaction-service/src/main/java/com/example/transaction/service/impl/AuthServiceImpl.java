package com.example.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.transaction.model.dto.UserDetail;
import com.example.transaction.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String getUidByPrincipal(Object principal) {
        try{
            UserDetail user = JSONObject.parseObject(principal.toString(),UserDetail.class);
            return user.getId();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
