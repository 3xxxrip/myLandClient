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
//求租信息实体类
public class Asks {
    private Integer id;
    private Integer belong;
    @TableField("request_info")
    private String requestInfo;
    @TableField("create_time")
    private Timestamp createTime;
}
