package com.sophia.map.service;

import com.sophia.map.dao.CompanyInfoDao;
import com.sophia.map.entity.CompanyInfo;
import com.sophia.map.entity.TaxInfo;
import com.sophia.map.view.Marker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sophia.map.common.Constant.SALES;
import static com.sophia.map.common.Constant.SALES_NAME;
import static com.sophia.map.common.Constant.TAXES;
import static com.sophia.map.common.Constant.TAXES_NAME;
import static com.sophia.map.common.Constant.TITLE_NAME;
import static com.sophia.map.common.Constant.XAXIS_NAME;
import static com.sophia.map.common.Constant.YAXIS_NAME;

@Service
public class TaxInfoServiceImpl implements TaxInfoService {

    @Resource
    private CompanyInfoDao companyInfoDao;

    @Override
    public List<Marker> queryMarkers() {
        List<CompanyInfo> companyInfos = companyInfoDao.findAll();
        List<Marker> markers = new ArrayList<>();
        for (CompanyInfo info : companyInfos) {
            Marker marker = new Marker();
            marker.setId(info.getId());
            marker.setSocialCreditCode(info.getSocialCreditCode());
            marker.setTaxPersonName(info.getTaxPersonName());
            marker.setSupervisionUnit(info.getSupervisionUnit());
            marker.setIndustryName(info.getIndustryName());
            marker.setLongitude(info.getLongitude());
            marker.setLatitude(info.getLatitude());
            markers.add(marker);
        }
        return markers;
    }

    private Map<String, Object> getChart(String title, List<String> xAxis, List<Long> yAxis) {
        Map<String, Object> map = new HashMap<>();
        map.put(TITLE_NAME, title);
        map.put(XAXIS_NAME, xAxis);
        map.put(YAXIS_NAME, yAxis);
        return map;
    }


    @Override
    public Map<String, Object> getChart(Integer id) {
        CompanyInfo companyInfo = companyInfoDao.getCompanyInfoById(id);
//        String taxPersonName = companyInfo.getTaxPersonName();
        String salesTitle = "【" + SALES + "】";
        String taxTitle = "【" + TAXES + "】";
        Set<TaxInfo> taxInfos = companyInfo.getTaxInfos();
        List<String> years = new LinkedList<>();
        List<Long> sales = new LinkedList<>();
        List<Long> taxes = new LinkedList<>();
        for (TaxInfo info : taxInfos) {
            years.add(info.getYear());
            sales.add(info.getSalesRevenue());
            taxes.add(info.getTaxRevenue());
        }
        // 组合数据
        Map<String, Object> salesChart = getChart(salesTitle, years, sales);
        Map<String, Object> taxesChart = getChart(taxTitle, years, taxes);
        Map<String, Object> chartMap = new HashMap<>();
        chartMap.put(SALES_NAME, salesChart);
        chartMap.put(TAXES_NAME, taxesChart);
        return chartMap;
    }


}
