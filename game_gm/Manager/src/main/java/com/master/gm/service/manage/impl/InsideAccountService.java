package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.*;
import com.master.bean.back.InsideAccount;
import com.master.bean.back.InsideAccountLog;
import com.master.gm.BaseService;
import com.master.gm.service.manage.InsideAccountServiceIF;
import com.master.util.ManageConf;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by HP on 2018/6/23.
 */
@IocBean
public class InsideAccountService extends BaseService implements InsideAccountServiceIF {

    private static final String APP_ID="wslb0404_0001";

    @Override
    public List<Record> getAllServer() {
        return this.dao.query("gm_config.game_server",null);
    }

    @Override
    public List<Record> getAllChannel() {
        return this.dao.query("gm_config.channel",null);
    }

    @Override
    public void saveOrUpdate(String id, String sid, String channel, String rolename, String pid, String paymoney, String username,
                             String userphone, String ascription, String applyuser, String applyreason, String remark) {
        InsideAccount insideAccount = new InsideAccount();

        if (id != null){
            insideAccount.setId(id);
            insideAccount.setSid(sid);
            insideAccount.setStatus("0");
        }else {
            insideAccount.setId("");
            insideAccount.setStatus("0");
        }
        insideAccount.setSid(sid);
        insideAccount.setChannel(channel);
        insideAccount.setRolename(rolename);
        insideAccount.setPid(pid);
        insideAccount.setPaymoney(paymoney);
        insideAccount.setUsername(username);
        insideAccount.setUserphone(userphone);
        insideAccount.setAscription(ascription);
        insideAccount.setApplyuser(applyuser);
        insideAccount.setApplyreason(applyreason);
        insideAccount.setRemark(remark);
        JSONObject result = null;
        try {
            String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/saveOrUpdate?msg=".concat(URLTool.Encode(JSONObject.toJSONString(insideAccount))), null);
            result = JSON.parseObject(ret);
            if (Integer.parseInt(result.get("code").toString()) == 0){
                throw new GmException(result.get("msg").toString());
            }
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }

    }

    @Override
    public List<Map<String, Object>> queryInsideAccountList() {
        JSONObject result = null;
        try {
            String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/queryInsideAccount", null);
            result = JSON.parseObject(ret);
            if (Integer.parseInt(result.get("code").toString()) == 0){
                throw new GmException(result.get("msg").toString());
            }else {
                List<Map<String ,Object>> insides = (List<Map<String, Object>>) JSON.parse(result.get("data").toString());
                return insides;
            }
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryInsideAccountById(Integer id) {
        JSONObject result = null;
        try {
            String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/queryInsideAccountById?id=".concat(String.valueOf(id)), null);
            result = JSON.parseObject(ret);
            if (Integer.parseInt(result.get("code").toString()) == 0){
                throw new GmException(result.get("msg").toString());
            }else {
                List<Map<String ,Object>> insides = (List<Map<String, Object>>) JSON.parse(result.get("data").toString());
                return insides;
            }
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }
        return null;
    }

    @Override
    public void delInsideAccount(String id) throws GmException {
        JSONObject result = null;
        String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/delInsideAccount?id=".concat(String.valueOf(id)), null);
        result = JSON.parseObject(ret);
        if (Integer.parseInt(result.get("code").toString()) == 0){
            throw new GmException(result.get("msg").toString());
        }
    }

    @Override
    public void insideAccountTE(String id, String type, String name) {
        JSONObject result = null;
        String param = id+"&type="+type+"&loginName="+name;
        try {
            String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/insideAccountTE?id=".concat(param), null);
            System.out.println("返回结果字符串："+ret);
            result = JSON.parseObject(ret);
            System.out.println("返回结果对象:"+result);
            if (Integer.parseInt(result.get("code").toString()) == 0){
                throw new GmException(result.get("msg").toString());
            }
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public List<Map<String, Object>> queryInsideAccountListCondition(String sid, String channel, String rolename, String pid, String money, String status) {
        InsideAccount insideAccount = new InsideAccount();
        insideAccount.setSid(sid);
        insideAccount.setChannel(channel);
        insideAccount.setRolename(rolename);
        insideAccount.setPaymoney(money);
        insideAccount.setStatus(status);
        insideAccount.setPid(pid);
        String msg = URLTool.Encode(JSONObject.toJSONString(insideAccount));
        JSONObject result = null;
        try {
            String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/queryInsideAccountListCondition?msg=".concat(msg), null);
            result = JSON.parseObject(ret);
            if (Integer.parseInt(result.get("code").toString()) == 0){
                throw new GmException(result.get("msg").toString());
            }else {
                List<Map<String ,Object>> insides = (List<Map<String, Object>>) JSON.parse(result.get("data").toString());
                return insides;
            }
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }
        return null;
    }

    @Override
    public void grantResource(String paymoney,String rolename,UserForService user) throws GmException{
        JSONObject result = null;
        JSONObject resultGoods = null;
        //查询是否为内部号
        String ret = URLTool.sendMsg(ManageConf.URL+":9080/insidea/queryAccountAdmin?rolename=".concat(URLTool.Encode(rolename)), null);
        result = JSON.parseObject(ret);
        if (Integer.parseInt(result.get("code").toString()) == 0){
            throw new GmException(result.get("msg").toString());
        }else {
            String param = paymoney+"&appid="+APP_ID;
            //查询跟金额匹配的货品
            String retGoods = URLTool.sendMsg(ManageConf.URL+":8012/yhPay/queryGoodsByMoney?money=".concat(param), null);
            resultGoods = JSON.parseObject(retGoods);
            if (Integer.parseInt(resultGoods.get("code").toString()) == 0){
                throw new GmException(result.get("msg").toString());
            }
           Map<String,Object> accountAdmin = (Map<String, Object>) JSONObject.parse(result.get("data").toString());
            //查询内部号信息
            String retInside = URLTool.sendMsg(ManageConf.URL+":9080/insidea/queryAccountInsideByPid?pid=".concat(accountAdmin.get("adminAccount").toString()), null);
            JSONObject insideJson = JSON.parseObject(retInside);
            Map resultData = JSONObject.parseObject(insideJson.get("data").toString());
            Map payMap = new HashMap();
            payMap.putAll(resultData);
            payMap.put("appid",APP_ID);
            payMap.put("goodsId",JSONObject.parseObject(resultGoods.get("data").toString(),Map.class).get("goodsId"));
            payMap.put("payType",6);
            payMap.put("money",paymoney);
            //保存发放记录
            InsideAccountLog insideLog = new InsideAccountLog();
            insideLog.setSid(resultData.get("sid").toString());
            insideLog.setChannel(resultData.get("channel").toString());
            insideLog.setRolename(rolename);
            insideLog.setMoney(Integer.parseInt(paymoney));
            insideLog.setOperatorBy(user.getName());
            insideLog.setLogTime(System.currentTimeMillis());

            this.dao.insert(insideLog);
            //调用发放接口
            String payParam = "msg="+JSONObject.toJSONString(payMap);
            String retPay = URLTool.sendMsg(ManageConf.URL+":8012/yhPay/insideAccountPay", payParam);
            JSONObject re = JSON.parseObject(retPay);
            if (Integer.parseInt(re.get("code").toString()) == 0){
                System.err.println("内部号资源发放失败：["+payParam+"].失败原因:"+re.get("msg"));
            }

        }
    }

    @Override
    public List<InsideAccountLog> getInsideAccountLog(String startDate, String endDate) throws ParseException {
        Criteria cri = Cnd.cri();
       if (startDate != null && !startDate.equals("")){
           long sd = DateUtil.getStringToCurrentTimeMillis(startDate,"yyyy-MM-dd");
           cri.where().andGTE("logTime",sd);
       }
       if (endDate != null && !endDate.equals("")){
           long ed = DateUtil.getStringToCurrentTimeMillis(endDate,"yyyy-MM-dd")+86399999;
           cri.where().andLTE("logTime",ed);
       }
        List<InsideAccountLog> list =this.dao.query(InsideAccountLog.class,cri);
        for (InsideAccountLog insideLog:list) {
            insideLog.setLogTimeS(DateUtil.getCurrentTimeMillisToString(insideLog.getLogTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        return list;
    }

}
