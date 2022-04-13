package com.longjian.myland.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//留言实体类
public class Message {
    private Integer id;
    private String belong;
    @TableField("message_info")
    private String messageInfo;
    @TableField("ask_id")
    private Integer askId;
    @TableField("create_time")
    private Timestamp createTime;
}
