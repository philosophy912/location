package com.sophia.map.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/5/30
 */
@Data
@Entity
@Table(name = "T_COMPANY_INFO")
public class CompanyInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 社会信用代码
    @Column(name = "Social_Credit_Code", nullable = false)
    private String socialCreditCode;
    // 纳税人名称
    @Column(name = "TAX_PERSON_NAME", nullable = false)
    private String taxPersonName;
    // 主管局
    @Column(name = "Supervision_Unit", nullable = false)
    private String supervisionUnit;
    // 行业名称
    @Column(name = "Industry_Name", nullable = false)
    private String industryName;
    // 经度信息
    @Column(name = "Longitude", nullable = false)
    private Float longitude;
    // 纬度信息
    @Column(name = "Latitude", nullable = false)
    private Float latitude;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "Location_Info_Id")
    private List<TaxInfo> taxInfos = new LinkedList<>();
}
