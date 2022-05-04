package com.longjian.myland.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class VerificationController {
    @Autowired
    Producer kaptchaProducer;
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //调用KaptchaConfig配置类里的kaptchaProducer的Producer接口中的生成干扰后的文本方法
        String text = kaptchaProducer.createText();
        //将文本转变为图片
        BufferedImage image = kaptchaProducer.createImage(text);
        //保存验证码到session中，名字为kaptcha
        session.setAttribute("kaptcha",text);
        //设置格式
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            //设置输出的图片格式
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
