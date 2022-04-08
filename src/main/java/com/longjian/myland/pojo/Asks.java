package com.longjian.myland.pojo;

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
    private String requestInfo;
    private Timestamp createTime;
}
