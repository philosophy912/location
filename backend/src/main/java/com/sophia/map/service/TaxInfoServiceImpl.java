package com.sophia.map.service;

import com.sophia.map.common.Pair;
import com.sophia.map.common.Request;
import com.sophia.map.dao.CompanyInfoDao;
import com.sophia.map.entity.CompanyInfo;
import com.sophia.map.entity.TaxInfo;
import com.sophia.map.view.Marker;
import com.sophia.map.view.MarkerVo;
import com.sophia.map.view.TaxVo;
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
import java.util.Objects;
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


    private Map<String, Object> parseCompanyInfos(List<CompanyInfo> infos) {
        log.debug("total size is {}", infos.size());
        Map<Integer, Pair<Long, Long>> map = getChartInfo(infos);
        String salesTitle = "【" + SALES + "】";
        String taxTitle = "【" + TAXES + "】";
        // 组合数据
        return setchartMap(salesTitle, taxTitle, map);
    }

    @Override
    public Map<String, Object> getChartByIndustryParkName(String name) {
        List<CompanyInfo> infos = companyInfoDao.findCompanyInfosByIndustryPark(name);
        return parseCompanyInfos(infos);
    }

    @Override
    public Map<String, Object> getChartByMarkerIds(List<Integer> ids) {
        List<CompanyInfo> infos = new LinkedList<>();
        for (Integer id : ids) {
            infos.add(companyInfoDao.getCompanyInfoById(id));
        }
        return parseCompanyInfos(infos);
    }

    @Override
    public List<Marker> queryMarkersByArea(String area) {
        log.debug("area is {}", area);
        List<CompanyInfo> companyInfos = companyInfoDao.findCompanyInfosByIndustryPark(area);
        List<Marker> markers = setMarkers(companyInfos);
        log.debug("markers size is {}", markers.size());
        return markers;
    }

    private void checkInfo(Object info, String tips) {
        if (info == null) {
            throw new RuntimeException(tips + "不能为空");
        }
    }


    private List<TaxInfo> convert(List<TaxVo> taxVos) {
        List<TaxInfo> taxInfos = new LinkedList<>();
        for (TaxVo vo : taxVos) {
            TaxInfo taxInfo = new TaxInfo();
            taxInfo.setYear(vo.getYear());
            taxInfo.setTaxRevenue(vo.getTaxRevenue());
            taxInfo.setSalesRevenue(vo.getSalesRevenue());
            taxInfos.add(taxInfo);
        }
        return taxInfos;
    }

    private void update(List<TaxInfo> taxInfos, List<TaxVo> taxVos, boolean updateFlag) {
        for (TaxInfo taxInfo : taxInfos) {
            for (TaxVo taxVo : taxVos) {
                // 年份相同更新
                if (Objects.equals(taxInfo.getYear(), taxVo.getYear())) {
                    // 数据不为空更新
                    if (taxVo.getTaxRevenue() != null) {
                        updateFlag = true;
                        taxInfo.setTaxRevenue(taxVo.getTaxRevenue());
                    }
                    if (taxVo.getSalesRevenue() != null) {
                        updateFlag = true;
                        taxInfo.setSalesRevenue(taxVo.getSalesRevenue());
                    }
                }
            }
        }
    }

    private boolean updateMain(CompanyInfo companyInfo, String taxPersonName, String supervisionUnit, String industryName, Float longitude, Float latitude, String country, String township, String industryPark) {
        boolean updateFlag = false;
        // 更新数据
        if (taxPersonName != null) {
            updateFlag = true;
            companyInfo.setTaxPersonName(taxPersonName);
        }
        if (supervisionUnit != null) {
            updateFlag = true;
            companyInfo.setSupervisionUnit(supervisionUnit);
        }
        if (industryName != null) {
            updateFlag = true;
            companyInfo.setIndustryName(industryName);
        }
        if (longitude != null) {
            updateFlag = true;
            companyInfo.setLongitude(longitude);
        }
        if (latitude != null) {
            updateFlag = true;
            companyInfo.setLatitude(latitude);
        }
        if (country != null) {
            updateFlag = true;
            companyInfo.setCounty(country);
        }
        if (township != null) {
            updateFlag = true;
            companyInfo.setTownship(township);
        }
        if (industryPark != null) {
            updateFlag = true;
            companyInfo.setIndustryPark(industryPark);
        }
        return updateFlag;
    }


    private CompanyInfo setCompanyInfo(String taxPersonName, String supervisionUnit, String industryName, Float longitude, Float latitude, String industryPark, String socialCreditCode, String county, String township) {
        // 插入数据
        CompanyInfo companyInfo = new CompanyInfo();
        // 非空字段不允许为空
        checkInfo(taxPersonName, "纳税人名称");
        checkInfo(supervisionUnit, "主管局");
        checkInfo(industryName, "行业名称");
        checkInfo(longitude, "经度信息");
        checkInfo(latitude, "纬度信息");
        checkInfo(industryPark, "工业园区");
        checkInfo(county, "区县");
        checkInfo(township, "乡镇");
        // 设置内容
        companyInfo.setSocialCreditCode(socialCreditCode);
        companyInfo.setTaxPersonName(taxPersonName);
        companyInfo.setSupervisionUnit(supervisionUnit);
        companyInfo.setIndustryName(industryName);
        companyInfo.setLongitude(longitude);
        companyInfo.setLatitude(latitude);
        companyInfo.setIndustryPark(industryPark);
        companyInfo.setCounty(county);
        companyInfo.setTownship(township);
        return companyInfo;
    }

    @Override
    public void update(MarkerVo markerVo) {
        String socialCreditCode = markerVo.getSocialCreditCode();
        String taxPersonName = markerVo.getTaxPersonName();
        String supervisionUnit = markerVo.getSupervisionUnit();
        String industryName = markerVo.getIndustryPark();
        Float longitude = markerVo.getLongitude();
        Float latitude = markerVo.getLatitude();
        String county = markerVo.getCounty();
        String township = markerVo.getTownship();
        String industryPark = markerVo.getIndustryPark();
        if (socialCreditCode == null) {
            throw new RuntimeException("唯一标识符socialcreditcode不能为空");
        }
        // 首先查询有没有对应的数据，如果有表示更新数据，如果没有表示新增数据
        CompanyInfo companyInfo = companyInfoDao.getCompanyInfoBySocialCreditCode(socialCreditCode);
        if (companyInfo == null) {
            log.info("insert data");
            CompanyInfo insertCompanyInfo = setCompanyInfo(taxPersonName, supervisionUnit, industryName, longitude, latitude, industryPark, socialCreditCode, county, township);
            List<TaxVo> taxVos = markerVo.getTaxInfos();
            if (taxVos.size() < 1) {
                throw new RuntimeException("没有填写税收信息");
            }
            insertCompanyInfo.setTaxInfos(convert(taxVos));
            // 插入到数据库中
            companyInfoDao.saveAndFlush(insertCompanyInfo);

        } else {
            log.info("update data");
            boolean updateFlag = updateMain(companyInfo, taxPersonName, supervisionUnit, industryName, longitude, latitude, county, township, industryPark);
            // 插入的数据
            List<TaxVo> taxVos = markerVo.getTaxInfos();
            // 数据库的数据
            List<TaxInfo> taxInfos = companyInfo.getTaxInfos();
            update(taxInfos, taxVos, updateFlag);
            if (updateFlag) {
                // 更新数据到数据库中
                companyInfoDao.saveAndFlush(companyInfo);
            } else {
                log.info("数据没有变化");
            }

        }
    }

    /**
     * 根据乡镇查询内容
     *
     * @param name 乡镇名字
     * @return chart
     */
    @Override
    public Map<String, Object> getChartByTownship(String name) {
        List<CompanyInfo> infos = companyInfoDao.findCompanyInfosByTownship(name);
        return parseCompanyInfos(infos);
    }

    @Override
    public Map<String, Object> getChartByCounty(String name) {
        List<CompanyInfo> infos = companyInfoDao.findCompanyInfosByCounty(name);
        return parseCompanyInfos(infos);
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
        return map;
    }

}
