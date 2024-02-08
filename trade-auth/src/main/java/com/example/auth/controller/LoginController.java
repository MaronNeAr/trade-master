package com.example.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ucenter.mapper.UserMapper;
import com.example.ucenter.model.dto.FindPswDto;
import com.example.ucenter.model.dto.RegisterDto;
import com.example.ucenter.model.po.User;
import com.example.ucenter.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试controller
 * @date 2022/9/27 17:25
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    VerifyService verifyService;


    @RequestMapping("/login-success")
    public String loginSuccess() {
        return "登录成功";
    }


    @RequestMapping("/user/{id}")

    public User getuser(@PathVariable("id") String id) {
        User user = userMapper.selectById(id);
        return user;
    }

    @RequestMapping("/r/r1")
    @PreAuthorize("hasAuthority('p1')")
    public String r1() {
        return "访问r1资源";
    }

    @RequestMapping("/r/r2")
    @PreAuthorize("hasAuthority('p2')")
    public String r2() {
        return "访问r2资源";
    }


    @ApiOperation(value = "找回密码", tags = "找回密码")
    @PostMapping("/findpassword")
    public void findPassword(@RequestBody FindPswDto findPswDto) {
        verifyService.findPassword(findPswDto);
    }

    @ApiOperation(value = "注册", tags = "注册")
    @PostMapping("/register")
    public void register(@RequestBody RegisterDto registerDto) {
        verifyService.register(registerDto);
    }
}
