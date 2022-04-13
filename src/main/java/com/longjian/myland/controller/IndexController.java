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

    //跳转到我的出租
    @RequestMapping("/myHouse.html")
    public String toMyHouse(){
        return "house/myHouse";
    }

    //跳转到付款页面
    @RequestMapping("/payPage.html")
    public String toPayPage(){
        return "business/payPage";
    }

    //跳转到付款之后的显示订单页面
    @RequestMapping("/afterPayPage.html")
    public String toAfterPayPage(){
        return "business/afterPayPage";
    }

    //跳转个人的租房记录页面
    @RequestMapping("/rentHis.html")
    public String toRentHis(){
        return "user/rentHis.html";
    }

    //跳转订单详情页
    @RequestMapping("/orderInfo.html")
    public String toOrderInfo(){
        return "user/orderInfo.html";
    }

    //跳转求租页面
    @RequestMapping("/askForRent.html")
    public String toAskForRent(){
        return "house/askForRent.html";
    }

    //跳转添加求租信息页面
    @RequestMapping("/addAsks.html")
    public String toAddAsks(){
        return "house/addAsks.html";
    }

    //跳转添加求租信息详情页
    @RequestMapping("/askInfo.html")
    public String toAskInfo(){
        return "house/askInfo.html";
    }
}
