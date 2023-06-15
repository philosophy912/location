package com.sophia.map.service;

import com.sophia.map.common.Pair;
import com.sophia.map.common.Request;
import com.sophia.map.dao.CompanyInfoDao;
import com.sophia.map.entity.CompanyInfo;
import com.sophia.map.entity.TaxInfo;
import com.sophia.map.view.Marker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
@Slf4j
public class TaxInfoServiceImpl implements TaxInfoService {

    @Resource
    private CompanyInfoDao companyInfoDao;

    private List<Marker> setMarkers(List<CompanyInfo> companyInfos) {
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


    @Override
    public List<Marker> queryMarkers() {
        List<CompanyInfo> companyInfos = companyInfoDao.findAll();
        List<Marker> markers = setMarkers(companyInfos);
        log.debug("markers size is {}", markers.size());
        return markers;
    }

    private Map<String, Object> getChart(String title, Collection<Integer> xAxis, List<Long> yAxis) {
        Map<String, Object> map = new HashMap<>();
        map.put(TITLE_NAME, title);
        map.put(XAXIS_NAME, xAxis);
        map.put(YAXIS_NAME, yAxis);
        return map;
    }

    private Map<String, Object> setchartMap(String salesTitle, String taxTitle, Map<Integer, Pair<Long, Long>> map) {
        Pair<Collection<Integer>, Pair<List<Long>, List<Long>>> pair = getYearMap(map);
        Collection<Integer> years = pair.getFirst();
        List<Long> sales = pair.getSecond().getFirst();
        List<Long> taxes = pair.getSecond().getSecond();
        Map<String, Object> salesChart = getChart(salesTitle, years, sales);
        Map<String, Object> taxesChart = getChart(taxTitle, years, taxes);
        Map<String, Object> chartMap = new HashMap<>();
        chartMap.put(SALES_NAME, salesChart);
        chartMap.put(TAXES_NAME, taxesChart);
        return chartMap;
    }


    private Map<Integer, Pair<Long, Long>> getLineData(List<TaxInfo> taxInfos) {
        Map<Integer, Pair<Long, Long>> map = new HashMap<>();
        // 排序，保证数据按照从大到小排
        Collections.sort(taxInfos);
        for (TaxInfo info : taxInfos) {
            Integer year = info.getYear();
            log.debug("year is {}", year);
            Long salesRevenue = info.getSalesRevenue();
            Long taxRevenue = info.getTaxRevenue();
            log.debug("sales revenue = {}", salesRevenue);
            log.debug("tax revenue = {}", taxRevenue);
            map.put(year, new Pair<>(salesRevenue, taxRevenue));
        }
        return map;
    }


    private Pair<Collection<Integer>, Pair<List<Long>, List<Long>>> getYearMap(Map<Integer, Pair<Long, Long>> map) {
        Set<Integer> years = map.keySet();
        List<Long> sales = new LinkedList<>();
        List<Long> taxes = new LinkedList<>();
        for (Integer year : years) {
            Pair<Long, Long> pair = map.get(year);
            sales.add(pair.getFirst());
            taxes.add(pair.getSecond());
        }
        return new Pair<>(years, new Pair<>(sales, taxes));
    }


    @Override
    public Map<String, Object> getChart(Integer id) {
        CompanyInfo companyInfo = companyInfoDao.getCompanyInfoById(id);
        log.debug("companyInfo is {}", companyInfo);
        String salesTitle = "【" + SALES + "】";
        String taxTitle = "【" + TAXES + "】";
        List<TaxInfo> taxInfos = companyInfo.getTaxInfos();
        Map<Integer, Pair<Long, Long>> map = getLineData(taxInfos);
        // 组合数据
        return setchartMap(salesTitle, taxTitle, map);
    }

    @Override
    public Map<String, Object> getChartByIds(Request request) {
        Float x1 = request.getX1();
        Float x2 = request.getX2();
        Float y1 = request.getY1();
        Float y2 = request.getY2();
        log.debug("x1 = {}, x2 = {}, y1 = {}, y2 = {}", x1, x2, y1, y2);
        // 这里还是查询所有的数据做数据过滤
        List<CompanyInfo> companyInfos = companyInfoDao.findAll();
        log.info("total size is {}", companyInfos.size());
        List<CompanyInfo> infos = new ArrayList<>();
        for (CompanyInfo info : companyInfos) {
            Integer id = info.getId();
            Float longitude = info.getLongitude();
            Float latitude = info.getLatitude();
            if (longitude >= y1 && longitude <= y2 && latitude >= x1 && latitude <= x2) {
                log.debug("id = {} ============> longitude = {}, latitude = {}", id, longitude, latitude);
                infos.add(info);
            }
        }
        Map<Integer, Pair<Long, Long>> map = getChartInfo(infos);
        String salesTitle = "【" + SALES + "】";
        String taxTitle = "【" + TAXES + "】";
        // 组合数据
        return setchartMap(salesTitle, taxTitle, map);
    }

    @Override
    public List<Marker> queryMarkersByName(String name) {
        String queryName = "%" + name + "%";
        List<CompanyInfo> companyInfos = companyInfoDao.findCompanyInfosByTaxPersonNameLikeIgnoreCase(queryName);
        return setMarkers(companyInfos);
    }

    @Override
    public List<String> getIndustryPark() {
        return companyInfoDao.findIndustryParks();
    }

    @Override
    public Map<String, Object> getChartByIndustryParkName(String name) {
        List<CompanyInfo> infos = companyInfoDao.findCompanyInfosByIndustryPark(name);
        log.info("total size is {}", infos.size());
        Map<Integer, Pair<Long, Long>> map = getChartInfo(infos);
        String salesTitle = "【" + SALES + "】";
        String taxTitle = "【" + TAXES + "】";
        // 组合数据
        return setchartMap(salesTitle, taxTitle, map);
    }

    private Map<Integer, Pair<Long, Long>> getChartInfo(List<CompanyInfo> infos) {
        log.info("filter marker size = {}", infos.size());
        Map<Integer, Pair<Long, Long>> map = new HashMap<>();
        List<Map<Integer, Pair<Long, Long>>> markers = new ArrayList<>();
        for (CompanyInfo info : infos) {
            log.info("id = {}", info.getId());
            List<TaxInfo> taxInfos = info.getTaxInfos();
            Map<Integer, Pair<Long, Long>> lineMap = getLineData(taxInfos);
            markers.add(lineMap);
        }
        for (Map<Integer, Pair<Long, Long>> marker : markers) {
            for (Map.Entry<Integer, Pair<Long, Long>> entry : marker.entrySet()) {
                Integer year = entry.getKey();
                // 销售和税收数据
                Pair<Long, Long> pair = entry.getValue();
                if (map.containsKey(year)) {
                    Pair<Long, Long> oldPair = map.get(year);
                    Long sales = oldPair.getFirst() + pair.getFirst();
                    Long taxes = oldPair.getSecond() + pair.getSecond();
                    map.put(year, new Pair<>(sales, taxes));
                } else {
                    map.put(year, entry.getValue());
                }
            }
        }
        for (Map.Entry<Integer, Pair<Long, Long>> entry : map.entrySet()) {
            log.warn("key = {}, value = {}", entry.getKey(), entry.getValue());
        }
        return map;
    }

}
