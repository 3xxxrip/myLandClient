package com.longjian.myland.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//房源实体类
public class House {
    private Integer id;
    private String category;
    private BigDecimal price;
    private String city;
    @TableField("house_info")
    private String houseInfo;
    private Integer belong;
    private Integer status;
    @TableField(exist = false)
    private List<HouseImg> imgs;
}
