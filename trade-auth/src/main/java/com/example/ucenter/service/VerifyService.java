package com.example.ucenter.service;

import com.example.ucenter.model.dto.FindPswDto;
import com.example.ucenter.model.dto.RegisterDto;

public interface VerifyService {
    void findPassword(FindPswDto findPswDto);

    void register(RegisterDto registerDto);
}
