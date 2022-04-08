package com.longjian.myland;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.longjian.myland.Utils.UserUtils;
import com.longjian.myland.mapper.UserMapper;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

@SpringBootTest
class MyLandApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        //添加一个用户信息
//        userMapper.insert(new User(0, "longjian", "200105", "龙建", "18216047135", "2459095104@qq.com",new BigDecimal(0), 1));
//        User user = userMapper.selectById(1);
//        System.out.println(user);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        UpdateWrapper<Object> updateWrapper = new UpdateWrapper<>();
        //查询用户名包含lon，邮箱不为null的用户
//        userQueryWrapper.like("username", "lon").isNotNull("email");
        //将用户名中包含有a并且（年龄大于20或邮箱为null）的用户信息修改
        //new Consumer<QueryWrapper<User>>() {
        //            @Override
        //            public void accept(QueryWrapper<User> userQueryWrapper) {
        //                userQueryWrapper.gt("age", "20").or().isNull("email");
        //            }
        //        }  下面and里面也可以这样写
//        userQueryWrapper.like("username", "a").and(i->i.gt("age", 20).or().isNull("email"));
        //查询id小于3的用户信息
        userQueryWrapper.lt("id", 3);
        List<User> users1 = userMapper.selectList(userQueryWrapper);
//        List<User> users = userMapper.selectList(userQueryWrapper);
        for (User user:
             users1) {
            System.out.println(user);
        }
    }

    @Autowired
    private UserServiceImpl userService;
    @Test
    public void testUtils(){
        User user = new User();
        user.setUserName("longjian");
        User userInDatabase = UserUtils.getUserInDatabase(userService, user);
        System.out.println(userInDatabase);
    }

}
