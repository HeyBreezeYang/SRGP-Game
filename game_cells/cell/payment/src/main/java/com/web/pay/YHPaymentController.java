package com.web.pay;

import com.alibaba.fastjson.JSONObject;
import com.design.exception.PlatformException;
import com.design.util.ResultUtil;
import com.design.util.URLTool;
import com.service.PaymentServiceIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2018/6/1.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/yhPay", method = RequestMethod.POST)
public class YHPaymentController {

    private Logger log = LoggerFactory.getLogger(YHPaymentController.class);

    @Autowired
    @Qualifier("yhPaymentService")
    private PaymentServiceIF yhPayment;

    @RequestMapping(value = "/payment")
    public String yingHunPay(String gameId, String channelId, String appId, String userId, String cpOrderId, String bfOrderId, String channelOrderId,
                             Integer money, String callbackInfo, Integer orderStatus, String channelInfo, String time, String sign, HttpServletRequest request) {
        /*log.info("参数：MSG: gameId:"+gameId+"==channelId:"+channelId+"==userId:"+userId+"==cpOrderId:"+cpOrderId+"==bfOrderId"+bfOrderId+
                "==channelOrderId:"+channelOrderId+"==money:"+money+"==$callbackInfo:"+callbackInfo+"==orderStatus:"+orderStatus+
                "==channelInfo:"+channelInfo+"==time:"+time+"==sign:"+sign);*/
        Map msg = new HashMap();
        msg.put("gameId",gameId);
        msg.put("channelId",channelId);
        msg.put("appId",appId);
        msg.put("userId",userId);
        msg.put("cpOrderId",cpOrderId);
        msg.put("bfOrderId",bfOrderId);
        msg.put("channelOrderId",channelOrderId);
        msg.put("money",money);
        msg.put("callbackInfo",callbackInfo);
        msg.put("callbackInfo1", URLTool.Dncode(request.getParameter("callbackInfo1")));
        msg.put("orderStatus",orderStatus);
        msg.put("channelInfo",channelInfo);
        msg.put("time",time);
        msg.put("sign",sign);
        log.info("参数：MSG: gameId:"+msg);
        try {
            yhPayment.payVerification(msg);

        } catch (PlatformException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return "{\"ret\": 1}";
        }
        return "{\"ret\": 0}";
    }

    @RequestMapping(value = "/insideAccountPay")
    public String insideAccountPay(String msg){
        System.out.println("msg:"+msg);
        ResultUtil result = new ResultUtil();
        try{
            yhPayment.insideAccountPay(msg);
            result.setCode(1);
            result.setMsg("发放成功");

        }catch (PlatformException p){
            result.setCode(0);
            result.setMsg(p.getMessage());
        }

        return  JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/queryGoodsByMoney")
    public String queryGoodsByMoney(String money,String appid){
        ResultUtil result = new ResultUtil();
        Map<String ,Object> goods = (Map<String, Object>) yhPayment.queryGoodsByMoney(money,appid);
        if (goods != null && goods.size()>0){
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(goods);
        }else {
            result.setCode(0);
            result.setMsg("没有查询到对应货品数据");
        }
        return  JSONObject.toJSONString(result);
    }
}
