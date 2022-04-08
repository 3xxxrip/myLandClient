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
//房屋图片实体类
public class HouseImg {
    private Integer id;
    @TableField("house_id")
    private Integer houseId;
    private String img;
}
