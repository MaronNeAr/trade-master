package com.example.transaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 放行全部请求类型
                .allowedMethods("*")
                // .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
                // 是否发送Cookie
                .allowCredentials(true)
                //暴露哪些头部信息
                .exposedHeaders("*");
    }
}
