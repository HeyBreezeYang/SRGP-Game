package com.javaweb;

import com.alibaba.fastjson.JSONObject;
import com.service.JQueryOrderServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
@RestController
@RequestMapping("/queryOrder")
public class QueryOrderController {

    @Autowired
    private JQueryOrderServiceIF orderService;

    @RequestMapping("/queryAllSetMeal")
    public String queryAllSetMeal(){
        List<Map<String,Object>> allSetMeal =  orderService.queryAllSetMeal();
        return JSONObject.toJSONString(allSetMeal);
    }

    @RequestMapping("/queryOrder")
    public String queryOrder(@Param("msg") String msg){
        List<Map<String,Object>> orders =  orderService.queryOrder(msg);

        return JSONObject.toJSONString(orders);
    }
}
