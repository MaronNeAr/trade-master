package com.example.ucenter.model.dto;

import com.example.ucenter.model.po.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 用户扩展信息
 * @author Mr.M
 * @date 2022/9/30 13:56
 * @version 1.0
 */
@Data
public class UserExt extends User {
    //用户权限
    List<String> permissions = new ArrayList<>();
}
