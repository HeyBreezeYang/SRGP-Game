package com.web.pay;

import com.alibaba.fastjson.JSONObject;
import com.design.util.URLTool;
import com.service.PayStatiscServiceIF;
import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2018/6/15.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/payStatisc", method = RequestMethod.POST)
public class PayStatiscController {

    @Autowired
    private PayStatiscServiceIF payStatisService;

    @RequestMapping(value = "/getToDayPayNum")
    public String getToDayPayNum(String msg, HttpServletRequest request){
        System.out.println(request.getParameter("msg"));
        Map<String,Object> map = payStatisService.getToDayPayNum(URLTool.Dncode(request.getParameter("msg")));
        return JSONObject.toJSONString(map);
    }
}
