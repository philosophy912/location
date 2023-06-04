package com.sophia.map.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "T_TAX_INFO")
public class TaxInfo implements Serializable, Comparable<TaxInfo> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 年份
    @Column(name = "Year", nullable = false)
    private Integer year;
    // 销售收入
    @Column(name = "Sales_Revenue", nullable = false)
    private Long salesRevenue;
    // 税收收入
    @Column(name = "Tax_Revenue", nullable = false)
    private Long taxRevenue;
    // 方便排序
    @Override
    public int compareTo(TaxInfo info) {
        return info.getYear() - this.getYear();
    }
}
