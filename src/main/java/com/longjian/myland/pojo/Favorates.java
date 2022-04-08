package com.longjian.myland.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//收藏表对应实体类
public class Favorates {
    private Integer id;
    private Integer belong;
    private Integer houseId;
}
