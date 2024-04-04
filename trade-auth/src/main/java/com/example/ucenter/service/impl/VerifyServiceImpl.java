package com.example.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.auth.config.common.FatalMessage;
import com.example.auth.config.common.SuccessMessage;
import com.example.ucenter.mapper.UserMapper;
import com.example.ucenter.mapper.UserRoleMapper;
import com.example.ucenter.model.dto.FindPswDto;
import com.example.ucenter.model.dto.RegisterDto;
import com.example.ucenter.model.po.User;
import com.example.ucenter.model.po.UserRole;
import com.example.ucenter.service.VerifyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerifyServiceImpl implements VerifyService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    public Boolean verify(String email, String checkcode) {
        // 1. 从redis中获取缓存的验证码
        String codeInRedis = redisTemplate.opsForValue().get(email);
        // 2. 判断是否与用户输入的一致
        if (codeInRedis.equalsIgnoreCase(checkcode)) {
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }

    @Override
    public Object findPassword(FindPswDto findPswDto) {
        String email = findPswDto.getEmail();
        String checkcode = findPswDto.getCheckcode();
        Boolean verify = verify(email, checkcode);
        if (!verify) {
//            throw new RuntimeException("验证码输入错误");
            System.out.println("验证码输入错误");
            return new FatalMessage("验证码输入错误").getMessage();
        }
        String password = findPswDto.getPassword();
        String confirmpwd = findPswDto.getConfirmpwd();
        if (!password.equals(confirmpwd)) {
//            throw new RuntimeException("两次输入的密码不一致");
            System.out.println("两次输入的密码不一致");
            return new FatalMessage("两次输入的密码不一致").getMessage();
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, findPswDto.getEmail());
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if (user == null) {
//            throw new RuntimeException("用户不存在");
            System.out.println("用户不存在");
            return new FatalMessage("用户不存在").getMessage();
        }
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userMapper.updateById(user);
        return new SuccessMessage("找回密码成功").getMessage();
    }

    @Override
    @Transactional
    public Object register(RegisterDto registerDto) {
        String uuid = UUID.randomUUID().toString();
        String email = registerDto.getEmail();
        String checkcode = registerDto.getCheckcode();
        Boolean verify = verify(email, checkcode);
        if (!verify) {
//            throw new RuntimeException("验证码输入错误");
            System.out.println("验证码输入错误");
            return new FatalMessage("验证码输入错误").getMessage();
        }
        String password = registerDto.getPassword();
        String confirmpwd = registerDto.getConfirmpwd();
        if (!password.equals(confirmpwd)) {
//            throw new RuntimeException("两次输入的密码不一致");
            System.out.println("两次输入的密码不一致");
            return new FatalMessage("两次输入的密码不一致").getMessage();
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, registerDto.getEmail());
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if (user != null) {
//            throw new RuntimeException("用户已存在，一个邮箱只能注册一个账号");
            System.out.println("用户已存在，一个邮箱只能注册一个账号");
            return new FatalMessage("用户已存在，一个邮箱只能注册一个账号").getMessage();
        }
        User xcUser = new User();
        BeanUtils.copyProperties(registerDto, xcUser);
        xcUser.setPassword(new BCryptPasswordEncoder().encode(password));
        xcUser.setId(uuid);
        xcUser.setUtype("101001");  // 学生类型
        xcUser.setStatus("1");
        xcUser.setName(registerDto.getNickname());
        xcUser.setCreateTime(LocalDateTime.now());
        int insert = userMapper.insert(xcUser);
        if (insert <= 0) {
//            throw new RuntimeException("新增用户信息失败");
            System.out.println("新增用户信息失");
            return new FatalMessage("新增用户信息失败").getMessage();
        }
        UserRole userRole = new UserRole();
        userRole.setId(uuid);
        userRole.setUserId(uuid);
        userRole.setRoleId("17");
        userRole.setCreateTime(LocalDateTime.now());
        int insert1 = userRoleMapper.insert(userRole);
        if (insert1 <= 0) {
//            throw new RuntimeException("新增用户角色信息失败");
            System.out.println("新增用户角色信息失败");
            return new FatalMessage("新增用户角色信息失败").getMessage();
        }
        return new SuccessMessage("新增用户角色成功").getMessage();
    }
}
