package com.longjian.myland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longjian.myland.pojo.House;

public interface HouseMapper extends BaseMapper<House> {
    //插入house的时候回填自增的id
   Integer insertAndGetIdBack(House house);
}
