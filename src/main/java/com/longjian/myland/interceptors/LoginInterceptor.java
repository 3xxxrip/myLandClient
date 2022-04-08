package com.longjian.myland.interceptors;

import com.longjian.myland.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //查看session是否存入了user信息，存入了就放行，没有就跳转到登录页面
        User user = (User) session.getAttribute("user");
        if(user!=null){
            //放行
            return true;
        }else{
            //没有用户信息，跳转到登录页面
            request.getRequestDispatcher("/login.html").forward(request, response);
            return false;
        }
    }
}
