package com.example.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;

public interface ExecutionService {
    int executeBuy();

    int executeSell();
}
