package com.sophia.map.dao;

import com.sophia.map.entity.TaxInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaxInfoDao  extends JpaRepository<TaxInfo, Integer>, JpaSpecificationExecutor<TaxInfo> {
}
