package com.longjian.myland.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.longjian.myland.Utils.UserUtils;
import com.longjian.myland.mapper.FavoritesMapper;
import com.longjian.myland.mapper.HouseImgMapper;
import com.longjian.myland.mapper.OrderProdMapper;
import com.longjian.myland.mapper.PurchaseHisMapper;
import com.longjian.myland.pojo.*;
import com.longjian.myland.service.Impl.HouseServiceImpl;
import com.longjian.myland.service.Impl.PurchaseHisServiceImpl;
import com.longjian.myland.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HouseServiceImpl houseService;
    @Autowired
    private PurchaseHisServiceImpl purchaseHisService;
    @Autowired
    private PurchaseHisMapper purchaseHisMapper;
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

    //注册实现,只接受post请求,防止通过url跳过js检查
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
    //ajax查询用户名是否存在,用于注册的时候提醒用户,暂时应用失败
    @RequestMapping("/existUsername")
    @ResponseBody
    public String existUsername(String username){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User one = userService.getOne(userQueryWrapper);
        if(one!=null){
            return "用户名已存在";
        }else {
            return "用户名不存在";
        }
    }

    //注销用户
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        //把user属性从session移除
//        session.removeAttribute("user");
        //清除seesion中的所有信息，不然用户注销之后登录另一个账户会session域中还有之前用户访问的缓存
        session.invalidate();
        //移出之后重定向到首页
        return "redirect:/index";
    }

    //修改个人资料
    @RequestMapping("/update")
    public String update(User user,HttpSession session,Model model){
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
        model.addAttribute("updateMsg", "修改成功");
        //修改成功，跳回修改页面
        return "forward:/userInfo.html";
    }

    @Autowired
    private HouseImgMapper houseImgMapper;
    //查看我的收藏房源
    @RequestMapping("/favoriteHouse")
    public String myFavoriteHouse(HttpSession session,Model model,@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        //此时session总的user信息是完整的，就是说是完整的在数据库中的资料
        User user = (User) session.getAttribute("user");
        QueryWrapper<House> houseQueryWrapper = new QueryWrapper<>();
        //子查询条件，找出该用户所有的收藏
        houseQueryWrapper.inSql("id", "SELECT house_id FROM favorites WHERE belong="+user.getId());
        //生成page
        Page<House> page=new Page<>(pn, 2);
        Page<House> favoriteHouses = houseService.page(page, houseQueryWrapper);
        //添加图片信息到house对象中
        List<House> records = favoriteHouses.getRecords();
        for (House house:records) {
            //轮番查询各个房屋所包含的图片设置到house对象里面
            QueryWrapper<HouseImg> imgQueryWrapper = new QueryWrapper<>();
            imgQueryWrapper.eq("house_id", house.getId());
            List<HouseImg> imgs = houseImgMapper.selectList(imgQueryWrapper);
            house.setImgs(imgs);
        }
        //将收藏信息保存到请求域中
        model.addAttribute("collections", favoriteHouses);
        return "forward:/favoriteHouse.html";
    }

    @Autowired
    private FavoritesMapper favoritesMapper;
    //取消收藏
    @RequestMapping("/deleteFavorite")
    public String deleteFavorite(Integer houseid,Model model,HttpSession session){
        //获取当前用户id
        User user = (User) session.getAttribute("user");
        QueryWrapper<Favorites> favoritesQueryWrapper = new QueryWrapper<>();
        favoritesQueryWrapper.eq("belong", user.getId()).and(i->i.eq("house_id", houseid));
        favoritesMapper.delete(favoritesQueryWrapper);
        //删除收藏之后重新访问收藏页面
        return "redirect:/favoriteHouse";
    }

    //查看我的出租房
    @RequestMapping("/myHouse")
    public String myHouse(HttpSession session,@RequestParam(value = "pn",defaultValue = "1") Integer pn,Model model,
                          @RequestParam(value = "houseId",required = false) Integer houseId,
                          @RequestParam(value = "addHouseId",required = false) Integer addHouseId){
        //下架商品
        if(houseId!=null){
            noSell(houseId);
        }
        //上架商品
        if(addHouseId!=null){
            addSell(addHouseId);
        }
        User user = (User) session.getAttribute("user");
        QueryWrapper<House> houseQueryWrapper = new QueryWrapper<>();
        //查询所有用户的房源信息
        houseQueryWrapper.eq("belong", user.getId());
        Page<House> page = new Page<>(pn, 2);
        Page<House> myHousePage = houseService.page(page, houseQueryWrapper);
        //输出查看一下
        List<House> records = myHousePage.getRecords();
        for (House house:records) {
            //轮番查询各个房屋所包含的图片设置到house对象里面
            QueryWrapper<HouseImg> imgQueryWrapper = new QueryWrapper<>();
            imgQueryWrapper.eq("house_id", house.getId());
            List<HouseImg> imgs = houseImgMapper.selectList(imgQueryWrapper);
            house.setImgs(imgs);
        }
        //添加页面信息到request域中
        model.addAttribute("myHousePage", myHousePage);
        //请求转发到myHouse.html页面
        return "forward:/myHouse.html";
    }
    //下架我的出租
    public void noSell(Integer houseId){
        UpdateWrapper<House> houseUpdateWrapper = new UpdateWrapper<>();
        //把id位houseid的status设置位3，即下架
        houseUpdateWrapper.set("status", 3).eq("id", houseId);
        //提交修改
        houseService.update(houseUpdateWrapper);
    }
    //重新上架我的出租
    public void addSell(Integer houseId){
        UpdateWrapper<House> houseUpdateWrapper = new UpdateWrapper<>();
        //把id位houseid的status设置位0，即未审核
        houseUpdateWrapper.set("status", 0).eq("id", houseId);
        //提交修改
        houseService.update(houseUpdateWrapper);
    }

    //查看个人租房记录
    @RequestMapping("/rentHis")
    public String rentHis(HttpSession session,@RequestParam(value = "pn",defaultValue = "1") Integer pn,Model model){
        User user = (User) session.getAttribute("user");
        QueryWrapper<PurchaseHis> purchaseHisQueryWrapper = new QueryWrapper<>();
        //查询用户所有的租房记录，并且保存分页对象
        purchaseHisQueryWrapper.eq("belong", user.getId());
        Page<PurchaseHis> purchaseHisPage = new Page<>(pn, 5);
        //获取到分页对象
        Page<PurchaseHis> page = purchaseHisService.page(purchaseHisPage, purchaseHisQueryWrapper);
        model.addAttribute("page", page);

        //请求转发到/rentHis.html页面
        return "forward:/rentHis.html";
    }

    @Autowired
    private OrderProdMapper orderProdMapper;
    //根据订单号查看订单信息
    @RequestMapping("/orderInfo")
    public String orderInfo(String orderId,Model model,Integer pn){
        QueryWrapper<OrderProd> orderProdQueryWrapper = new QueryWrapper<>();
        orderProdQueryWrapper.eq("order_id", orderId);
        OrderProd orderProd = orderProdMapper.selectOne(orderProdQueryWrapper);
        Integer houseId = orderProd.getHouseId();
        QueryWrapper<House> houseQueryWrapper = new QueryWrapper<>();
        houseQueryWrapper.eq("id", houseId);
        House house = houseService.getOne(houseQueryWrapper);
        //获取房源对应的图片
        QueryWrapper<HouseImg> imgQueryWrapper = new QueryWrapper<>();
        imgQueryWrapper.eq("house_id", house.getId());
        List<HouseImg> imgs = houseImgMapper.selectList(imgQueryWrapper);
        house.setImgs(imgs);
        //获取房东的电话号码
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",house.getBelong());
        String phone = userService.getOne(userQueryWrapper).getPhone();
        model.addAttribute("orderInfo",house);
        model.addAttribute("pn",pn);
        model.addAttribute("phone",phone);
        //请求转发去订单详情页
        return "forward:/orderInfo.html";
    }
}
