package com.longjian.myland.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longjian.myland.mapper.HouseMapper;
import com.longjian.myland.pojo.House;
import com.longjian.myland.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;
    //插入并且获得自增的主键id值
    public Integer insertAndGetIdBack(House house){
       return houseMapper.insertAndGetIdBack(house);
    }
}
