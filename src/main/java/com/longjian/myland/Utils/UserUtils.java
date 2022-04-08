package com.longjian.myland.Utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.Impl.UserServiceImpl;

public class UserUtils {

    public static User getUserInDatabase(UserServiceImpl userService,User user){
        //方法是通过用户信息得到用户名来获取数据库中的完整的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUserName());
        return userService.getOne(queryWrapper);
    }
}
