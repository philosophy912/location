package com.sophia.map.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

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
    // 项目名称
    private String projectName;
    // 项目编号
    private Integer projectId;
    // 资源占用的类型
    private String type;
    // 查询的名字
    private String name;
    // 表ID 如29
    private Integer id;
    // 名字 如"CPU占用率_20230217"
    private String label;
    // log的唯一标识符
    private String logID;
    // vin码
    private String vin;
    // tuid码
    private String tuid;
    // 收藏类型
    private Boolean isFav;
    // 严重问题
    private Boolean isSerious;
}
