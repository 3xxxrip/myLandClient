package com.longjian.myland.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.longjian.myland.mapper.HouseImgMapper;
import com.longjian.myland.mapper.OrderMapper;
import com.longjian.myland.mapper.OrderProdMapper;
import com.longjian.myland.mapper.PurchaseHisMapper;
import com.longjian.myland.pojo.*;
import com.longjian.myland.service.Impl.HouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class BusinessController {
    @Autowired
    private HouseServiceImpl houseService;
    @Autowired
    private PurchaseHisMapper purchaseHisMapper;
    @Autowired
    private HouseImgMapper houseImgMapper;
    @Autowired
    private OrderProdMapper orderProdMapper;
    @Autowired
    private OrderMapper orderMapper;
    //跳转到paypage，显示要租用的房屋信息
    @RequestMapping("/payPage")
    public String pay(Integer houseId, Model model){
        House house = houseService.getById(houseId);
        //这里要做一个判断，看看当前时刻房源是否被售出了，如果被购买
        if(house.getStatus()==1){
            QueryWrapper<HouseImg> imgQueryWrapper = new QueryWrapper<>();
            imgQueryWrapper.eq("house_id", house.getId());
            List<HouseImg> imgs = houseImgMapper.selectList(imgQueryWrapper);
            house.setImgs(imgs);
            //添加房源信息到request域中
            model.addAttribute("house",house);

        }else{
            //添加信息提示用户该房源已被出售
            model.addAttribute("payPageMsg", "抱歉，该房子以及被其他用户刚刚租用了");
        }
        return "forward:/payPage.html";
    }

    //处理租房业务
    @RequestMapping("/rent")
    public String rent(Integer houseId, HttpSession session, BigDecimal price,Model model){
        //首先获取用户信息
        User user = (User) session.getAttribute("user");
        //修改房源信息为已出租
        UpdateWrapper<House> houseUpdateWrapper = new UpdateWrapper<>();
        //状态2就是已出租
        houseUpdateWrapper.set("status", 2).eq("id", houseId);
        //提交修改
        houseService.update(houseUpdateWrapper);

        //创建订单信息
        String orderId=user.getId().toString()+System.currentTimeMillis();
        System.out.println(orderId);

        Order order = new Order(0, orderId, user.getId(), price, new Timestamp(System.currentTimeMillis()));
        orderMapper.insertAndGetIdBack(order);
        System.out.println(order);
        //添加个人租房记录
        purchaseHisMapper.insert(new PurchaseHis(0, user.getId(), houseId, new Timestamp(System.currentTimeMillis()),price,orderId));
        //添加订单信息回传
        model.addAttribute("afterPayMsg", orderId);
        //添加订单商品绑定添加到数据库
        orderProdMapper.insert(new OrderProd(0,orderId,houseId));
        //返回租房之后的订单显示页面
        return "forward:/afterPayPage.html";
    }
}
