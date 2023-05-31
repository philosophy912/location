package com.sophia.map.controller;

import com.sophia.map.common.Constant;
import com.sophia.map.common.Response;
import com.sophia.map.service.TaxInfoService;
import com.sophia.map.view.Marker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tax")
@Slf4j
public class TaxInfoController {
    @Resource
    private TaxInfoService taxInfoService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Response getMarkers() {
        Response response = new Response();
        try {
            List<Marker> markers = taxInfoService.queryMarkers();
            response.setData(markers);
        } catch (Exception e) {
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public Response getChartById(@RequestParam Integer id) {
        Response response = new Response();
        try {
            Map<String, Object> data = taxInfoService.getChart(id);
            response.setData(data);
        } catch (Exception e) {
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

}
