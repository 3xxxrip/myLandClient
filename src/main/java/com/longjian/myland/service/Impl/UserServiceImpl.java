package com.longjian.myland.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longjian.myland.mapper.UserMapper;
import com.longjian.myland.pojo.User;
import com.longjian.myland.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
