package com.longjian.myland.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//订单商品详情表实体类
public class OrderProd {
    private Integer id;
    //这里对应的是订单表中的id不是orderId
    @TableField("order_id")
    private String orderId;
    @TableField("house_id")
    private Integer houseId;
}
