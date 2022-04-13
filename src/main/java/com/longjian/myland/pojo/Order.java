package com.longjian.myland.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField("order_id")
    private String orderId;
    private Integer belong;
    private BigDecimal amount;
    @TableField("payment_time")
    private Timestamp paymentTime;
}
