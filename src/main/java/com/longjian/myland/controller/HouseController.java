package com.longjian.myland.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.longjian.myland.mapper.AskMapper;
import com.longjian.myland.mapper.FavoritesMapper;
import com.longjian.myland.mapper.HouseImgMapper;
import com.longjian.myland.pojo.*;
import com.longjian.myland.service.Impl.AskServiceImpl;
import com.longjian.myland.service.Impl.HouseServiceImpl;
import com.longjian.myland.service.Impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Controller
public class HouseController {
    @Autowired
    private HouseServiceImpl houseService;
    @Autowired
    private HouseImgMapper houseImgMapper;
    @Autowired
    private FavoritesMapper favoritesMapper;
    @Autowired
    private AskServiceImpl askService;
    @Autowired
    private MessageServiceImpl messageService;
    //添加房源,以及处理房源图片信息
    @RequestMapping("/addHouse")
    public String addHouse(House house, HttpSession session, @RequestPart("img")MultipartFile[] imgs, Model model) throws IOException {
        //获取当前session中的用户信息
        User user = (User) session.getAttribute("user");
        //同user注册同理，id设置为0，status默认为0即未审核不需要设置,belong设置为当前用户的id
        house.setId(0);
        house.setBelong(user.getId());
        //提交房屋添加,主键id会回填到house里面
        houseService.insertAndGetIdBack(house);
        //添加房屋图片,如果房屋图片大于0就添加
        if(imgs.length>0){
            for (MultipartFile img: imgs) {
                //通过uuid+文件上传原始名称生成一个不会重复的文件名，避免图片被覆盖
                String imgName = UUID.randomUUID().toString();
                String filePath="D:\\卓景京\\myLand\\src\\main\\resources\\static\\img\\houseImg\\"+imgName+".jpg";
                //保存图片到本地
                img.transferTo(new File(filePath));
                //保存房间图片信息到数据库
                houseImgMapper.insert(new HouseImg(0, house.getId(), imgName+".jpg"));
            }
        }
        return "forward:/addHouse.html";
    }

    //商城出租页面显示
    @RequestMapping("/pageHouse")
    public String pageHouse(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model,
                            @RequestParam(value = "city",required = false) String city,
                            @RequestParam(value = "min",required = false,defaultValue = "0")String min,
                            @RequestParam(value = "max",required = false,defaultValue = "0")String max){
        if(min.equals("null")&&max.equals("null")){
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
        //清除收藏回显信息
        model.addAttribute("addFavoriteMsg", null);
        //构造分页参数,第一个参数是当前页，第二个参数是每页显示多少行
        Page<House> page=new Page<>(pn,2);
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        //有筛选的情况
        if(city!=null&&minx>=0&&maxx!=0){
            BigDecimal min1 = new BigDecimal(minx);
            BigDecimal max1 = new BigDecimal(maxx);
            //BigDecimal比较大小,这里就是说min1>0，且max1>min
            if(min1.compareTo(max1) ==-1&& min1.compareTo(new BigDecimal(0)) > -1){
                //查询价格在min和max之间的城市为city、status为1的城市
                queryWrapper.between("price", min1, max1).eq("status", 1).eq("city", city);
                model.addAttribute("city", city);
                model.addAttribute("min",minx);
                model.addAttribute("max",maxx);
            }
        }else if(city!=null&&minx==0&&maxx==0){
            //在只传过来筛选城市的房源，价格不做要求
            queryWrapper.eq("status", 1).eq("city", city);
            model.addAttribute("city", city);
            model.addAttribute("min",null);
            model.addAttribute("max",null);
        } else{
            //查询所有状态为1的房源进行显示，已出租2、未出租1、未审核0、已下架3
            model.addAttribute("city", null);
            model.addAttribute("min",null);
            model.addAttribute("max",null);
            queryWrapper.eq("status", 1);
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
        return "forward:/pageHouse.html";
    }

    //收藏房源功能实现,处理的ajax请求
    @RequestMapping("/addFavorite")
    @ResponseBody
    public String addFavorite(@RequestParam("houseId") Integer houseId,HttpSession session){
        //首先判断房源是否存在
        House byId = houseService.getById(houseId);
        if(byId!=null){//房源存在
            //获取当前用户信息
            User user = (User) session.getAttribute("user");
            //判断用户是否是是否已经收藏过这个房源了
            QueryWrapper<Favorites> favoritesQueryWrapper = new QueryWrapper<>();
            favoritesQueryWrapper.eq("belong", user.getId()).and(i->i.eq("house_id", houseId));
            Integer count = favoritesMapper.selectCount(favoritesQueryWrapper);
            if(count==0){
                //没收藏过
                Favorites favorites = new Favorites(0, user.getId(), houseId);
                //条件收藏信息
                favoritesMapper.insert(favorites);
                //添加反馈信息
                return "收藏成功";
            }else {
                return "你已经收藏过这个房源了，请勿重复收藏";
            }
        }else{
            return "房源不存在";
        }
    }

    //显示所有的求租信息
    @RequestMapping("/askForRent")
    public String askForRent(@RequestParam(value = "pn",defaultValue = "1") Integer pn,Model model){
        Page<Asks> page = new Page<>(pn, 5);
        Page<Asks> asksPage = askService.page(page);
        model.addAttribute("page", asksPage);
        //请求转发到求租页面
        return "forward:/askForRent.html";
    }

    //添加求租信息
    @RequestMapping("/addAsks")
    public String addAsks(String askInfo,HttpSession session){
        User user = (User) session.getAttribute("user");
        //添加求租信息
        askService.save(new Asks(0, user.getId(), askInfo, new Timestamp(System.currentTimeMillis())));
        //请求转发到求租页面
        return "forward:/askForRent";
    }

    //求租信息详情页面，并且加载留言
    @RequestMapping("/askInfo")
    public String askInfo(Integer askId,@RequestParam(value = "pn",defaultValue = "1")Integer pn,Model model){
        //首先加载求租信息内容
        Asks ask = askService.getOne(new QueryWrapper<Asks>().eq("id", askId));
        model.addAttribute("ask", ask);
        //获取该条求租信息的所有留言
        Page<Message> page = new Page<>(pn, 5);
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("ask_id", askId);
        Page<Message> messagePage = messageService.page(page, messageQueryWrapper);
        //添加所有的留言信息到request域中
        model.addAttribute("messagePage", page);
        return "forward:/askInfo.html";
    }
    //留言实现
    @RequestMapping("/addMessage")
    public String addMessage(Integer pn,Integer askId,HttpSession session,String messageInfo){
        User user = (User) session.getAttribute("user");
        //添加留言
        messageService.save(new Message(0, user.getUserName(), messageInfo, askId, new Timestamp(System.currentTimeMillis())));
        return "redirect:/askInfo?pn="+pn+"&askId="+askId;
    }
}
