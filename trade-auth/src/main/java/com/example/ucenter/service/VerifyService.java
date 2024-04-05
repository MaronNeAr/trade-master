package com.example.ucenter.service;

import com.example.ucenter.model.dto.FindPswDto;
import com.example.ucenter.model.dto.RegisterDto;

public interface VerifyService {
    Object findPassword(FindPswDto findPswDto);

    Object register(RegisterDto registerDto);
}
