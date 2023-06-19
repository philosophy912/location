package com.sophia.map.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/2/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request implements Serializable {
    private Float x1;
    private Float y1;
    private Float x2;
    private Float y2;
    // 名字， 支持工业园区名字（精确查询)和纳税人的名字（模糊查询）
    private String name;
    private List<Integer> ids;


}
