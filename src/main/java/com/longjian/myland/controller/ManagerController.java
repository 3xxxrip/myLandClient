package com.longjian.myland.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.longjian.myland.Utils.UserUtils;
import com.longjian.myland.mapper.HouseImgMapper;
import com.longjian.myland.mapper.ManagerMapper;
import com.longjian.myland.pojo.*;
import com.longjian.myland.service.Impl.HouseServiceImpl;
import com.longjian.myland.service.Impl.MessageServiceImpl;
import com.longjian.myland.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private HouseServiceImpl houseService;
    @Autowired
    private HouseImgMapper houseImgMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MessageServiceImpl messageService;

    //管理员登录实现
    @RequestMapping("/login")
    public String Login(String username, String password, Model model, HttpSession session){
        //查询条件
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<>();
        //根据用户名和密码查询用户
        queryWrapper.eq("username", username).and(i->i.eq("password", password));
        Manager manager = managerMapper.selectOne(queryWrapper);

        //如果为空说明用户存在，登录成功
        if(manager!=null){
            //清除之前登录失败产生的错误信息
            model.addAttribute("loginMsg", null);
            //把用户信息存入session，代表已经登录过
            session.setAttribute("manager", manager);
            //登陆成功，重定向到首页
            return "redirect:/manager/managerIndex.html";
        }else{
            //登陆失败，返回错误信息，返回登录页
            model.addAttribute("loginMsg", "用户信息错误请重新输入");
            return "forward:/managerLogin.html";
        }
    }

    //审核房源实现
    @RequestMapping("/checkHouse")
    public String checkHouse(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model,
                             @RequestParam(value = "city",required = false) String city,
                             @RequestParam(value = "min",required = false,defaultValue = "0")String min,
                             @RequestParam(value = "max",required = false,defaultValue = "0")String max,
                             @RequestParam(value = "houseId",required = false)Integer houseId,
                             @RequestParam(value = "yon",required = false)String yon){
        //首先实现房源通过或者不通过
        if(yon!=null){
            if(yon.equals("Y")){
                //通过
                pass(houseId);
            }else if(yon.equals("N")){
                //不通过，直接下架
                unPass(houseId);
            }

        }
        if(min.equals("null")&&max.equals("null")){
            //如果前端传过来null字符串，就转化成0
            min="0";
            max="0";
        }
        if(city!=null){
            //把null字符串转化为null值
            if(city.equals("null")){
                city=null;
            }
            else if(city.equals("")){
                city=null;
            }
        }

        Integer minx = new Integer(min);
        Integer maxx = new Integer(max);
        //构造分页参数,第一个参数是当前页，第二个参数是每页显示多少行
        Page<House> page=new Page<>(pn,2);
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        //有筛选的情况
        if(city!=null&&minx>=0&&maxx!=0){
            BigDecimal min1 = new BigDecimal(minx);
            BigDecimal max1 = new BigDecimal(maxx);
            //BigDecimal比较大小,这里就是说min1>0，且max1>min
            if(min1.compareTo(max1) ==-1&& min1.compareTo(new BigDecimal(0)) > -1){
                //查询价格在min和max之间的城市为city、status为0的城市
                queryWrapper.between("price", min1, max1).eq("status", 0).eq("city", city);
                model.addAttribute("city", city);
                model.addAttribute("min",minx);
                model.addAttribute("max",maxx);
            }
        }else if(city!=null&&minx==0&&maxx==0){
            //在只传过来筛选城市的房源，价格不做要求
            queryWrapper.eq("status", 0).eq("city", city);
            model.addAttribute("city", city);
            model.addAttribute("min",null);
            model.addAttribute("max",null);
        } else{
            //查询所有状态为0的房源进行显示，已出租2、未出租1、未审核0、已下架3
            model.addAttribute("city", null);
            model.addAttribute("min",null);
            model.addAttribute("max",null);
            queryWrapper.eq("status", 0);
        }
        Page<House> housePage = houseService.page(page, queryWrapper);
        //添加图片信息到house对象中
        List<House> records = housePage.getRecords();
        for (House house:records) {
            //轮番查询各个房屋所包含的图片设置到house对象里面
            QueryWrapper<HouseImg> imgQueryWrapper = new QueryWrapper<>();
            imgQueryWrapper.eq("house_id", house.getId());
            List<HouseImg> imgs = houseImgMapper.selectList(imgQueryWrapper);
            house.setImgs(imgs);
        }
        //添加page信息到request域中
        model.addAttribute("houses",housePage);
        //请求转发到租房页面
        return "forward:/manager/managerCheckHouse.html";
    }
    //审核不通过商品
    public void unPass(Integer houseId){
        UpdateWrapper<House> houseUpdateWrapper = new UpdateWrapper<>();
        //不通过直接下架
        houseUpdateWrapper.set("status", 3).eq("id", houseId);
        houseService.update(houseUpdateWrapper);
    }
    //审核通过房源
    public void pass(Integer houseId){
        UpdateWrapper<House> houseUpdateWrapper = new UpdateWrapper<>();
        //上架房源
        houseUpdateWrapper.set("status", 1).eq("id", houseId);
        houseService.update(houseUpdateWrapper);
    }

    //管理用户信息
    @RequestMapping("/checkUser")
    public String checkUser(@RequestParam(value = "pn",defaultValue = "1") Integer pn,
                            @RequestParam(value = "userId",required = false) Integer userId,
                            @RequestParam(value = "bou",required = false)String bou,
                            @RequestParam(value = "username",required = false)String username,
                            Model model){
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        if(userId!=null){
            //传过来userid说明要操作用户，判断是封禁还是解封
            if(bou.equals("B")){
                //封禁，设置status为0，B代表要封禁
                userUpdateWrapper.set("status",0).eq("id", userId);
                userService.update(userUpdateWrapper);
            }else  if(bou.equals("U")){
                //解封
                userUpdateWrapper.set("status", 1).eq("id", userId);
                userService.update(userUpdateWrapper);
            }
        }else if(username!=null){
            //按照用户名查询用户信息
            userUpdateWrapper.eq("username", username);
            Page<User> userPage = new Page<>(pn, 5);
            Page<User> page = userService.page(userPage,userUpdateWrapper);
            model.addAttribute("users", page);
            model.addAttribute("username",username);
            return "forward:/manager/managerCheckUsers.html";
        }
        //查询所有的user
        Page<User> userPage = new Page<>(pn, 5);
        Page<User> page = userService.page(userPage);
        model.addAttribute("users", page);
        return "forward:/manager/managerCheckUsers.html";
    }

    //管理留言信息
    @RequestMapping("/checkMessage")
    public String checkMessage(@RequestParam(value = "pn",defaultValue = "1") Integer pn,
                               @RequestParam(value = "messageId",required = false) Integer messageId,
                               @RequestParam(value = "username",required = false)String username,
                               @RequestParam(value = "start",required = false)String start,
                               @RequestParam(value = "end",required = false)String end,
                               @RequestParam(value = "keyword",required = false)String keyword,
                               Model model) throws ParseException {
        if("null".equals(username)||"".equals(username)){
            username=null;
        }
        if("null".equals(start)||"".equals(start)){
            start=null;
        }
        if("null".equals(end)||"".equals(end)){
            end=null;
        }
        if("null".equals(keyword)||"".equals(keyword)){
            keyword=null;
        }
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        Page<Message> page = new Page<>(pn, 5);
        //先执行删除留言
        if(messageId!=null){
            //执行删除
            messageQueryWrapper.eq("id", messageId);
            messageService.remove(messageQueryWrapper);
        }
        if(username!=null&&start==null&&end==null&&keyword==null){
            //只按照用户名查找留言信息，不管发布时间、关键字
            //用同一个 QueryWrapper时，先清空上一个message查询产生的条件
            messageQueryWrapper.clear();
            messageQueryWrapper.eq("belong", username);
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", username);
            model.addAttribute("start", null);
            model.addAttribute("end", null);
            model.addAttribute("keyword", null);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }else  if(username==null&&start!=null&&end!=null&&keyword==null){
            //只按照发布时间查找留言信息，不管发布用户、关键字
            //转化前端穿过来的数据格式
            String startr = start.replace('T', ' ')+":00";
            String endr = end.replace('T', ' ')+":00";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date startd = simpleDateFormat.parse(startr);
            Date endd = simpleDateFormat.parse(endr);
            //这是对应数据库的datatime的日期类型
            Timestamp startt = new Timestamp(startd.getTime());
            Timestamp endt = new Timestamp(endd.getTime());
            messageQueryWrapper.clear();
//            messageQueryWrapper.between("create_time", startt, endt);mysql由于使用between时候是识别不了24的，会自动转化为下一天0点，所以用大于等于、小于等于来筛选
            messageQueryWrapper.ge("create_time", startt).and(i->i.le("create_time", endt));
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", null);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
            model.addAttribute("keyword", null);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }else if(username==null&&start==null&&end==null&&keyword!=null){
            //只按关键字查，不管用户和时间
            messageQueryWrapper.clear();
            messageQueryWrapper.like("message_info", "%"+keyword+"%");
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", null);
            model.addAttribute("start", null);
            model.addAttribute("end", null);
            model.addAttribute("keyword", keyword);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }
        else if(username!=null&&start!=null&&end!=null&&keyword==null){
            //按照用户名和时间来筛选信息、不管关键字
            //转化前端穿过来的数据格式
            String startr = start.replace('T', ' ')+":00";
            String endr = end.replace('T', ' ')+":00";
            //转化时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date startd = simpleDateFormat.parse(startr);
            Date endd = simpleDateFormat.parse(endr);
            //这是对应数据库的datatime的日期类型
            Timestamp startt = new Timestamp(startd.getTime());
            Timestamp endt = new Timestamp(endd.getTime());
            //开始查询
            messageQueryWrapper.clear();
//            messageQueryWrapper.eq("belong",username).between("create_time", startt, endt);
            messageQueryWrapper.eq("belong",username).ge("create_time", startt).and(i->i.le("create_time", endt));
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", username);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
            model.addAttribute("keyword", null);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }else if(username!=null&&start==null&&end==null&&keyword!=null){
            //只按照用户名和关键字查询
            messageQueryWrapper.clear();
            messageQueryWrapper.eq("belong", username).like("message_info", "%"+keyword+"%");
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", username);
            model.addAttribute("start", null);
            model.addAttribute("end", null);
            model.addAttribute("keyword", keyword);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }else if(username==null&&start!=null&&end!=null&&keyword!=null){
            //只按照时间和关键字来查询
            //转化前端穿过来的数据格式
            String startr = start.replace('T', ' ')+":00";
            String endr = end.replace('T', ' ')+":00";
            //转化时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date startd = simpleDateFormat.parse(startr);
            Date endd = simpleDateFormat.parse(endr);
            //这是对应数据库的datatime的日期类型
            Timestamp startt = new Timestamp(startd.getTime());
            Timestamp endt = new Timestamp(endd.getTime());
            messageQueryWrapper.clear();
            messageQueryWrapper.like("message_info", "%"+keyword+"%").ge("create_time", startt).and(i->i.le("create_time", endt));
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", null);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
            model.addAttribute("keyword", keyword);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }else if(username!=null&&start!=null&&end!=null&&keyword!=null){
            //根据用户名、关键字、时间段来查询
            messageQueryWrapper.clear();
            //转化前端穿过来的数据格式
            String startr = start.replace('T', ' ')+":00";
            String endr = end.replace('T', ' ')+":00";
            //转化时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date startd = simpleDateFormat.parse(startr);
            Date endd = simpleDateFormat.parse(endr);
            //这是对应数据库的datatime的日期类型
            Timestamp startt = new Timestamp(startd.getTime());
            Timestamp endt = new Timestamp(endd.getTime());
            messageQueryWrapper.eq("belong", username).like("message_info", "%"+keyword+"%").ge("create_time", startt).and(i->i.le("create_time", endt));
            Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
            model.addAttribute("username", username);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
            model.addAttribute("keyword", keyword);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }
        else{
            //没有条件，直接查询所有的信息
            Page<Message> messagePage = messageService.page(page);
            model.addAttribute("messages", messagePage);
            //请求转发到管理信息页面
            return "forward:/manager/managerCheckMessage.html";
        }
    }
}
