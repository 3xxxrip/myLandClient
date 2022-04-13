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
//收藏表对应实体类
public class Favorites {
    private Integer id;
    private Integer belong;
    @TableField("house_id")
    private Integer houseId;
}
