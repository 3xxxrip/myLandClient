package com.longjian.myland.interceptors;

import com.longjian.myland.pojo.Manager;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ManagerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Manager manager = (Manager) session.getAttribute("manager");
        if(manager!=null){
            //管理员登录，不拦截放行
            return true;
        }else{
            //返回管理员登录页
            request.getRequestDispatcher("/managerLogin.html").forward(request, response);
            return false;
        }
    }
}
