package com.longjian.myland.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//订单实体类
public class Order {
    private Integer id;
    private String orderId;
    private Integer belong;
    private BigDecimal amount;
    private Timestamp createTime;
    private Timestamp paymentTime;
    private Integer status;
}
