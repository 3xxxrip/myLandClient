package com.longjian.myland.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    //配置生成的验证码的样式类型
    @Bean
    public Producer kaptchaProducer() {
        Properties properties = new Properties();
        //图片宽
        properties.setProperty("kaptcha.image.width", "100");
        //图片高
        properties.setProperty("kaptcha.image.height", "40");
        //字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        //文本集合，验证码值从此集合中获取 "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ" 表示从这些字符里选
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");
        //验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
