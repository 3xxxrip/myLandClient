package com.longjian.myland.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
//此控制页管理所有跳转.html请求，以及首页跳转
public class IndexController {
    @Autowired
    private UserServiceImpl userService;
    //管理首页跳转
    @RequestMapping(value = {"/","/index","/index.html"})
    public String index(){
        return "index";
    }

    //用户点击登录之后跳转到登录页面
    @RequestMapping("/login.html")
    public String toLogin(){
        return "user/login";
    }

    //跳转到登陆页面
    @RequestMapping("/regist.html")
    public String toRegist(){
        return "user/regist";
    }

    //跳转到个人资料页面
    @RequestMapping("/userInfo.html")
    public String toInfo(){
        return "user/userInfo";
    }

    //跳转到增加房源信息页面
    @RequestMapping("/addHouse.html")
    public String toAddHouse(){
        return "house/addHouse";
    }

    //跳转到租房页面
    @RequestMapping("/pageHouse.html")
    public String toPageHouse(){
        return "house/pageHouse";
    }

    //跳转到收藏页面
    @RequestMapping("/favoriteHouse.html")
    public String toFavoriteHouse(){
        return "house/favoriteHouse";
    }
}
