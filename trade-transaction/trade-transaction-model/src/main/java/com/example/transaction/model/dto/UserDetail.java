package com.example.transaction.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserDetail implements Serializable {

    private static final long serialVersionUID = -919201640201914789L;

    private String id;

    private String username;

    private String nickname;

    private String name;

    private String email;

    private String cellphone;

    private LocalDateTime createTime;

    private String status;

    private String utype;

    public UserDetail(String id, String username, String nickname, String name, String email, String cellphone, LocalDateTime createTime, String status, String utype) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.cellphone = cellphone;
        this.createTime = createTime;
        this.status = status;
        this.utype = utype;
    }
}

