package com.longjian.myland.controller;

import com.longjian.myland.mapper.HouseImgMapper;
import com.longjian.myland.pojo.House;
import com.longjian.myland.pojo.HouseImg;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.Impl.HouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class HouseController {
    @Autowired
    private HouseServiceImpl houseService;
    @Autowired
    private HouseImgMapper houseImgMapper;
    //添加房源,以及处理房源图片信息
    @RequestMapping("/addHouse")
    public String addHouse(House house, HttpSession session, @RequestPart("img")MultipartFile[] imgs, Model model) throws IOException {
        System.out.println(house);
        //获取当前session中的用户信息
        User user = (User) session.getAttribute("user");
        //同user注册同理，id设置为0，status默认为0即未审核不需要设置,belong设置为当前用户的id
        house.setId(0);
        house.setBelong(user.getId());
        //提交房屋添加,主键id会回填到house里面
         houseService.insertAndGetIdBack(house);
        System.out.println(house);
        //添加房屋图片,如果房屋图片大于0就添加
        if(imgs.length>0){
            for (MultipartFile img: imgs) {
                //通过uuid+文件上传原始名称生成一个不会重复的文件名，避免图片被覆盖
                String imgName = UUID.randomUUID().toString()+img.getOriginalFilename();
                String filePath="D:\\毕业设计\\house_img\\"+imgName+".jpg";
                //保存图片到本地
                img.transferTo(new File(filePath));
                //保存房间图片信息到数据库
                houseImgMapper.insert(new HouseImg(0, house.getId(), filePath));
            }
        }
        model.addAttribute("addHouseMsg", "房源上传成功，等待审核！");
        return "forward:/addHouse.html";
    }
}
