package com.example.checkcode.controller;

import com.example.checkcode.model.CheckCodeParamsDto;
import com.example.checkcode.model.CheckCodeResultDto;
import com.example.checkcode.service.CheckCodeService;
import com.example.checkcode.service.SendCodeService;
import com.example.checkcode.utils.MailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(value = "验证码服务接口")
@RestController
public class CheckCodeController {
    @Autowired
    SendCodeService sendCodeService;

    @Resource(name = "PicCheckCodeService")
    private CheckCodeService picCheckCodeService;


    @ApiOperation(value = "生成验证信息", notes = "生成验证信息")
    @PostMapping(value = "/pic")
    public CheckCodeResultDto generatePicCheckCode(CheckCodeParamsDto checkCodeParamsDto) {
        return picCheckCodeService.generate(checkCodeParamsDto);
    }

    @ApiOperation(value = "校验", notes = "校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "业务名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "key", value = "验证key", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/verify")
    public Boolean verify(String key, String code) {
        Boolean isSuccess = picCheckCodeService.verify(key, code);
        return isSuccess;
    }

    @ApiOperation(value = "发送邮箱验证码", tags = "发送邮箱验证码")
    @PostMapping("/email")
    public void sendEMail(String email) {
        String code = MailUtil.achieveCode();
        sendCodeService.sendEMail(email, code);
    }
}
