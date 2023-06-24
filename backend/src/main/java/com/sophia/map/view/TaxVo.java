package com.sophia.map.view;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

@Data
public class TaxVo {
    private Integer year;
    private Long salesRevenue;
    private Long taxRevenue;
}
