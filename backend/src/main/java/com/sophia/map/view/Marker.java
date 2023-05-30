package com.sophia.map.view;

import lombok.Data;

/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/5/30
 */
@Data
public class Marker {
    private Integer id;
    // 社会信用代码
    private String socialCreditCode;
    // 纳税人名称
    private String taxPersonName;
    // 主管局
    private String supervisionUnit;
    // 行业名称
    private String industryName;
    // 经度信息
    private Float longitude;
    // 纬度信息
    private Float latitude;
}
