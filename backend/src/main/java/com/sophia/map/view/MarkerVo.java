package com.sophia.map.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class MarkerVo {
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
    // 区县
    private String county;
    // 乡镇
    private String township;
    // 工业园区
    private String industryPark;
    // 税收情况
    private List<TaxVo> taxInfos;
}
