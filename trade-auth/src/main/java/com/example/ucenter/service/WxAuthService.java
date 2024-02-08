package com.example.ucenter.service;

import com.example.ucenter.model.po.User;

public interface WxAuthService {
    User wxAuth(String code);
}
