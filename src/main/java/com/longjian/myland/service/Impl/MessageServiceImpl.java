package com.longjian.myland.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longjian.myland.mapper.MessageMapper;
import com.longjian.myland.pojo.Message;
import com.longjian.myland.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}
