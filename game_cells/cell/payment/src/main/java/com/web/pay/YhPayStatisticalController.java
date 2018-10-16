package com.web.pay;

import com.alibaba.fastjson.JSONObject;
import com.design.util.URLTool;
import com.service.YhPayStatisticalServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/23.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/yhPayStatistical", method = RequestMethod.POST)
public class YhPayStatisticalController {

    @Autowired
    private YhPayStatisticalServiceIF statisicalService;

    @RequestMapping(value = "/playerDatePay")
    public String queryPlayerDatePay(String msg){
        System.out.println("接收到的参数:msg:"+URLTool.Dncode(msg));
        List ret = null;
        try {
            ret = statisicalService.queryPlayerDatePay(URLTool.Dncode(msg));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(ret);
    }
}
