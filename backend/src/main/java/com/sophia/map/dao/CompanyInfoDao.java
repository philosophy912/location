package com.sophia.map.dao;

import com.sophia.map.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/5/30
 */
public interface CompanyInfoDao extends JpaRepository<CompanyInfo, Integer>, JpaSpecificationExecutor<CompanyInfo> {
}
