package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.URLTool;
import com.master.bean.dispoly.Parameter;
import com.master.gm.BaseService;
import com.master.gm.service.manage.IpWhiteListServiceIF;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
@IocBean
public class IpWhiteListService extends BaseService implements IpWhiteListServiceIF {
    @Override
    public List<Map> getAllIpWhiteList() {
        String url=this.dao.fetch(Parameter.class,"loginUrl").getPrams().concat("/getAllIpWhiteList");
        List allIpWhiteList = new ArrayList();
        try {
            String ret = URLTool.sendMsg(url,null);
            allIpWhiteList = JSONObject.parseObject(ret,List.class);
            return allIpWhiteList;
        } catch (GmException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addIpWhiteList(String ip, String remark) throws GmException {
        String url=this.dao.fetch(Parameter.class,"loginUrl").getPrams().concat("/addIpWhiteList");
        Map map = new HashMap();
        map.put("ip",ip);
        map.put("remark",remark);
        String params = "msg="+JSONObject.toJSONString(map);
        String ret = URLTool.sendMsg(url,params);
        Map retMap = JSONObject.parseObject(ret,Map.class);
        if (Integer.parseInt(retMap.get("code").toString()) == 1){
            String url1=this.dao.fetch(Parameter.class,"deliverUrl").getPrams().concat("/updateAllowLoginIp");
            String paramJson = "ips="+retMap.get("data").toString();
            System.out.println(paramJson);
            String ret1 = URLTool.sendMsg(url1,paramJson);
            System.out.println(ret1);

        }else {
            throw new GmException("增加失败");
        }
    }

    @Override
    public void delIpWhiteList(String id) throws GmException {
        String url=this.dao.fetch(Parameter.class,"loginUrl").getPrams().concat("/delIpWhiteList?id=");
        Map map = new HashMap();
        String ret = URLTool.sendMsg(url.concat(id),null);
        Map retMap = JSONObject.parseObject(ret,Map.class);
        if (Integer.parseInt(retMap.get("code").toString()) == 1){
            String url1=this.dao.fetch(Parameter.class,"deliverUrl").getPrams().concat("/updateAllowLoginIp");
            String paramJson = "ips="+retMap.get("data").toString();
            System.out.println(paramJson);
            String ret1 = URLTool.sendMsg(url1,paramJson);
            System.out.println(ret1);

        }else {
            throw new GmException("增加失败");
        }
    }
}
