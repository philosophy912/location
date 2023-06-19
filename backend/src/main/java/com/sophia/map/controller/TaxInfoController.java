package com.sophia.map.controller;

import com.sophia.map.common.Constant;
import com.sophia.map.common.Request;
import com.sophia.map.common.Response;
import com.sophia.map.service.TaxInfoService;
import com.sophia.map.view.Marker;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 这个接口主要作用是获取markers
     *
     * @param request， 请求参数
     * @return Response
     */
    @RequestMapping(value = "/markers", method = RequestMethod.POST)
    public Response getMarkers(@RequestBody Request request) {
        Response response = new Response();
        try {
            String name = request.getName();
            List<Marker> markers;
            if (Strings.isEmpty(name)) {
                markers = taxInfoService.queryMarkers();
            } else {
                markers = taxInfoService.queryMarkersByName(name);
            }
            response.setData(markers);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

    /**
     * 获取工业园区的种类(非重复)
     *
     * @return 工业园区的所有名称
     */
    @RequestMapping(value = "/industryPark", method = RequestMethod.GET)
    public Response getIndustryPark() {
        Response response = new Response();
        try {
            List<String> data = taxInfoService.getIndustryPark();
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

    /**
     * 根据mark点的信息获取税收数据
     * @param id mark的点数据
     * @return marker点的税收信息（单点)
     */
    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public Response getChartById(@RequestParam Integer id) {
        log.debug("request id is {}", id);
        Response response = new Response();
        try {
            Map<String, Object> data = taxInfoService.getChart(id);
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

    /**
     * 画框获取marker点，然后统计所有点的税收数据
     * @param request 请求数据，其中x1, x2, y1, y2不能为空
     * @return 画框内的marker点的税收信息
     */
    @RequestMapping(value = "/ids", method = RequestMethod.POST)
    public Response getChartByIds(@RequestBody Request request) {
        Response response = new Response();
        log.info("request is {}", request);
        try {
            Map<String, Object> data = taxInfoService.getChartByIds(request);
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

    /**
     * 获取某个工业园区的税收信息
     * @param request 请求数据，name不能为空，这个地方是全匹配
     * @return 工业园区税收信息
     */
    @RequestMapping(value = "/area", method = RequestMethod.POST)
    public Response getChartByArea(@RequestBody Request request) {
        Response response = new Response();
        log.info("request is {}", request);
        try {
            String name = request.getName();
            if (Strings.isEmpty(name)) {
                response.setCode(Constant.NOK);
                response.setMessage("can not found markers when name is null");
            } else {
                Map<String, Object> data = taxInfoService.getChartByIndustryParkName(name);
                response.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;

    }

    /**
     * 根据所有的Marker点来查询数据
     * @param request 请求数据，ids不能为空，这个地方是全匹配
     * @return
     */

    @RequestMapping(value = "/markerIds", method = RequestMethod.POST)
    public Response getChartByMarkerIds(@RequestBody Request request) {
        Response response = new Response();
        log.info("request is {}", request);
        try {
            List<Integer> ids = request.getIds();
            Map<String, Object> data = taxInfoService.getChartByMarkerIds(ids);
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorInfo(e.getMessage());
            response.setCode(Constant.NOK);
        }
        return response;
    }

}
