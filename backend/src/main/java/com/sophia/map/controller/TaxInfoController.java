package com.sophia.map.controller;

import com.sophia.map.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tax")
@Slf4j
public class TaxInfoController {
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public Response getSides() {
        Response response = new Response();
        response.setCode(200);
        return response;
    }

}
