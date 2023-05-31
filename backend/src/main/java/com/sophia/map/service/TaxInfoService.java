package com.sophia.map.service;

import com.sophia.map.view.Marker;

import java.util.List;
import java.util.Map;

public interface TaxInfoService {

    List<Marker> queryMarkers();

    Map<String, Object> getChart(Integer id);


}