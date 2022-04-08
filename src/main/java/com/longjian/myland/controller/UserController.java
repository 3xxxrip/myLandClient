package com.longjian.myland.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.longjian.myland.Utils.UserUtils;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    //登录实现
    @PostMapping("/login")
    public String login(String username, String password, Model model, HttpSession session){
        //查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //根据用户名和密码查询用户
        queryWrapper.eq("username", username).and(i->i.eq("password", password));
        User user = userService.getOne(queryWrapper);

        //如果为空说明用户存在，登录成功
        if(user!=null){
            //清除之前登录失败产生的错误信息
            model.addAttribute("loginMsg", null);
            //把用户信息存入session，代表已经登录过
            session.setAttribute("user", user);
            //登陆成功，重定向到首页
            return "redirect:/index";
        }else{
            //登陆失败，返回错误信息，返回登录页
            model.addAttribute("loginMsg", "用户信息错误请重新输入");
            return "user/login";
        }
    }

    //注册实现,只接受post请求
    @PostMapping("/regist")
    public String regist(User user,Model model){
        //首先判断用户名是否已经存在了
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUserName());
        User exist = userService.getOne(queryWrapper);
        //如果等于空说明没有这个用户，可以注册
        if(exist==null){
            //清除之前注册失败产生的错误信息
            model.addAttribute("registMsg", null);
            //id其实在数据库中是自增长的，但是这里不设置一个值的会mybatis就会报错，所以这里设置一下0，在数据库中id还是会正常增长
            user.setId(0);
            //用户不存在,现在注册，设置余额为0，状态因为数据库默认值为1，就是激活状态，所以不需要设置status
            user.setBalance(new BigDecimal(0));
            userService.save(user);
            //注册成功，重定向到登录页面
            return "redirect:/login.html";
        }else {
            model.addAttribute("registMsg", "用户名已存在，请重新选一个用户名");
            //注册失败，跳转到注册页面
            return "forward:/regist.html";
        }
    }

    //注销用户
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        //把user属性从session移除
        session.removeAttribute("user");
        //移出之后重定向到首页
        return "redirect:/index";
    }

    //修改个人资料
    @RequestMapping("/update")
    public String update(User user,HttpSession session){
        //因为username是不可以修改的，而且只有在登录之后才可以访问个人资料页，所以直接从session里面获取用户名，从前端网页发送过来，也一并存入user里面
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password", user.getPassword())
                .set("realname", user.getRealName())
                .set("phone", user.getPhone())
                .set("email", user.getEmail())
                .eq("username",user.getUserName());
        //提交修改
        userService.update(updateWrapper);
        //通过工具类获取数据库中user的完整信息，包括id之类的
        User userAfterUpdate = UserUtils.getUserInDatabase(userService,user);
        //更新session里面的user信息
        session.setAttribute("user", userAfterUpdate);
        //修改成功，跳回修改页面
        return "forward:/userInfo.html";
    }
}
