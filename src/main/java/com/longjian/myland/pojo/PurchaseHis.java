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
//购买记录表实体类
public class PurchaseHis {
    private Integer id;
    private Integer belong;
    @TableField("house_id")
    private Integer houseId;
    @TableField("purchase_time")
    private Timestamp purchaseTime;
    private BigDecimal amount;
    @TableField("order_id")
    private String OrderId;
}
