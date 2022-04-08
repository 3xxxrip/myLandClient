package com.longjian.myland;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//扫描mapper接口所在的包
@MapperScan(basePackages = "com.longjian.myland.mapper")
public class MyLandApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyLandApplication.class, args);
    }

}