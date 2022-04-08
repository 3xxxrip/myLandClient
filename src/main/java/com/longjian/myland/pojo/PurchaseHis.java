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
//购买记录表实体类
public class PurchaseHis {
    private Integer id;
    private Integer belong;
    private Integer houseId;
    private Timestamp purchaseTime;
    private String houseInfo;
}
