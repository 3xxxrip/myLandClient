package com.longjian.myland.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
//用户实体类
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private BigDecimal balance;
    private Integer status;
}
