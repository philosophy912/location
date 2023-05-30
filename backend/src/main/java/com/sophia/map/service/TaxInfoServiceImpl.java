package com.sophia.map.service;

import com.sophia.map.dao.CompanyInfoDao;
import com.sophia.map.entity.CompanyInfo;
import com.sophia.map.view.Marker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxInfoServiceImpl implements TaxInfoService {

    @Resource
    private CompanyInfoDao companyInfoDao;

    @Override
    public List<Marker> queryMarkers() {
        List<CompanyInfo> companyInfos = companyInfoDao.findAll();
        List<Marker> markers = new ArrayList<>();
        for(CompanyInfo info: companyInfos){
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
}
