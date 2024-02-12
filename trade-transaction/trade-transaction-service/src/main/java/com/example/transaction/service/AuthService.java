package com.example.transaction.service;

import com.example.transaction.model.dto.UserDetail;

public interface AuthService {
    String getUidByPrincipal(Object principal);
}
