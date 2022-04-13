package com.longjian.myland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longjian.myland.pojo.House;
import com.longjian.myland.pojo.Order;

public interface OrderMapper extends BaseMapper<Order> {
    //插入order的时候回填自增的id
    Integer insertAndGetIdBack(Order order);
}
