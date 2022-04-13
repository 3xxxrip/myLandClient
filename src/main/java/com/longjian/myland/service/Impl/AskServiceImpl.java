package com.longjian.myland.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longjian.myland.mapper.AskMapper;
import com.longjian.myland.pojo.Asks;
import com.longjian.myland.service.AskService;
import org.springframework.stereotype.Service;

@Service
public class AskServiceImpl extends ServiceImpl<AskMapper, Asks> implements AskService {
}
