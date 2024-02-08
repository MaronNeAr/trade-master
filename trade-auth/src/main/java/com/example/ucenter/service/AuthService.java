package com.example.ucenter.service;

import com.example.ucenter.model.dto.AuthParamsDto;
import com.example.ucenter.model.dto.UserExt;

/**
 * @description 认证接口
 * @author Mr.M
 * @date 2022/10/20 14:48
 * @version 1.0
 */
public interface AuthService {

  /**
   * @description 认证方法
   * @param authParamsDto 认证参数
   * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
   * @author Mr.M
   * @date 2022/9/29 12:11
   */
  UserExt execute(AuthParamsDto authParamsDto);

}
