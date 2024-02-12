package com.example.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.ucenter.mapper.MenuMapper;
import com.example.ucenter.mapper.UserMapper;
import com.example.ucenter.model.dto.AuthParamsDto;
import com.example.ucenter.model.dto.UserExt;
import com.example.ucenter.model.po.Menu;
//import com.example.ucenter.model.po.User;
import com.example.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserDetailsImpl implements UserDetailsService {
    @Autowired
    UserMapper UserMapper;
    @Autowired
    MenuMapper MenuMapper;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * @param s 用户输入的登录账号
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            log.error("认证请求数据格式不对：{}", s);
            throw new RuntimeException("认证请求数据格式不对");
        }
        // 获取认证类型，beanName就是 认证类型 + 后缀，例如 password + _authservice = password_authservice
        String authType = authParamsDto.getAuthType();
        // 根据认证类型，从Spring容器中取出对应的bean
        AuthService authService = applicationContext.getBean(authType + "_authservice", AuthService.class);
        UserExt user = authService.execute(authParamsDto);
        return getUserPrincipal(user);
    }

    public UserDetails getUserPrincipal(UserExt user) {
        // 获取用户id
        String userId = user.getId();
        // 根据用户id查询用户权限
        List<Menu> menus = MenuMapper.selectPermissionByUserId(userId);
        ArrayList<String> permissions = new ArrayList<>();
        // 没权限，给一个默认的
        if (menus.isEmpty()) {
            permissions.add("test");
        } else {
            // 获取权限，加入到集合里
            menus.forEach(menu -> {
                permissions.add(menu.getCode());
            });
        }
        // 设置权限
        user.setPermissions(permissions);
        String[] authorities = permissions.toArray(new String[0]);
        String password = user.getPassword();
        user.setPassword(null);
        String userJsonStr = JSON.toJSONString(user);
        UserDetails userDetails = User.withUsername(userJsonStr).password(password).authorities(authorities).build();
        return userDetails;
    }
}
