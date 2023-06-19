package com.sophia.map.service;

import com.sophia.map.common.Request;
import com.sophia.map.view.Marker;

import java.util.List;
import java.util.Map;

public interface TaxInfoService {

    List<Marker> queryMarkers();

    Map<String, Object> getChart(Integer id);


    Map<String, Object> getChartByIds(Request request);

    List<Marker> queryMarkersByName(String name);

    List<String> getIndustryPark();

    Map<String, Object> getChartByIndustryParkName(String name);

    Map<String, Object> getChartByMarkerIds(List<Integer> ids);
}
