package com.longjian.myland.config;

import com.longjian.myland.interceptors.LoginInterceptor;
import com.longjian.myland.interceptors.ManagerInterceptor;
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
        //普通用户拦截器配置
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
        //图片不拦截是为了用户访问没有的页面时能正常提示，而不是提示登录
        excludePath.add("/img/**");
        //错误页面不要求登录
        excludePath.add("/error/**");
        //查询用户名是否存在不拦截
        excludePath.add("/existUsername");
        //管理员功能不拦截
        excludePath.add("/manager/**");
        excludePath.add("/managerLogin.html");
        //登出不拦截
        excludePath.add("/logout");
        //验证码不拦截
        excludePath.add("/kaptcha");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludePath);

        //管理员拦截器配置
        ManagerInterceptor managerInterceptor = new ManagerInterceptor();
        ArrayList<String> managerEx = new ArrayList<>();
        //登录功能不拦截
        managerEx.add("/manager/login");
        registry.addInterceptor(managerInterceptor).addPathPatterns("/manager/**").excludePathPatterns(managerEx);
    }
}
