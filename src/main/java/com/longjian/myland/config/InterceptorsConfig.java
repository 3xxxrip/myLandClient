package com.longjian.myland.config;

import com.longjian.myland.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
//配置类文件注解
@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        List<String> excludePath=new ArrayList<>();
        //首页不拦截
        excludePath.add("/");
        excludePath.add("/index");
        excludePath.add("/index.html");
        //登录页不拦截，登录请求不拦截
        excludePath.add("/login.html");
        excludePath.add("/login");
        //注册页不拦截，注册请求不拦截
        excludePath.add("/regist.html");
        excludePath.add("/regist");
        //静态资源不拦截
        excludePath.add("/img/**");
        //查询用户名是否存在不拦截
        excludePath.add("/existUsername");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludePath);
    }
}
