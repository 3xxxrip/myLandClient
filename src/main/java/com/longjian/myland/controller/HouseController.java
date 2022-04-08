package com.longjian.myland.controller;

import com.longjian.myland.pojo.House;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.Impl.HouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HouseController {
    @Autowired
    private HouseServiceImpl houseService;

    //添加房源
    @RequestMapping("/addHouse")
    public String addHouse(House house, HttpSession session){
        System.out.println(house);
        //获取当前session中的用户信息
        User user = (User) session.getAttribute("user");
        //同user注册同理，id设置为0，status设置为0即未审核,belong设置为当前用户的id
        house.setId(0);
        house.setStatus(0);
        house.setBelong(user.getId());
        //提交添加
        houseService.save(house);
        return "forward:/addHouse.html";
    }
}
