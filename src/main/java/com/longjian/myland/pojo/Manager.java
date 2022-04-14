package com.longjian.myland.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//管理员实体类
public class Manager {
    private Integer id;
    private String userName;
    private String password;
    @TableField("real_name")
    private String realName;
    private String phone;
    private String email;
}
