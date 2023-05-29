package com.sophia.map.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "T_TAX_INFO")
public class TaxInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 社会信用代码
    @Column(name = "Social_Credit_Code")
    private String socialCreditCode;
    // 纳税人名称
    @Column(name = "TAX_PERSON_NAME")
    private String taxPersonName;
    // 主管局
    @Column(name = "Supervision_Unit")
    private String SupervisionUnit;
    // 行业名称
    @Column(name = "Industry_Name")
    private String IndustryName;
    // 年份
    @Column(name = "Year")
    private String year;
    // 销售收入
    @Column(name = "Sales_Revenue")
    private Long salesRevenue;
    // 税收收入
    @Column(name = "Tax_Revenue")
    private Long taxRevenue;

}
