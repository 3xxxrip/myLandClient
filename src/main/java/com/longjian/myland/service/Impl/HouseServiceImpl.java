package com.longjian.myland.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longjian.myland.mapper.HouseMapper;
import com.longjian.myland.pojo.House;
import com.longjian.myland.service.HouseService;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {
}
