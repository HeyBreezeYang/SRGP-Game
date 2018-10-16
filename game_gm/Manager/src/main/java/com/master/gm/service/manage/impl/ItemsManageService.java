package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.ItemsUtil;
import com.gmdesign.util.SendUtil;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.manage.ItemsManageServiceIF;
import com.master.util.ManageConf;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/30.
 */
@IocBean
public class ItemsManageService extends BaseService implements ItemsManageServiceIF {
    @Override
    public Map queryRoleItems(String rolename) {
        Map retsultMap = new HashMap();
        retsultMap.put("rolename",rolename);
        String u=this.dao.query(GameServer.class, Cnd.where("serverID","=", ManageConf.SERVERID)).get(0).getServerUrl().concat("/loadPlayer?playerName="+rolename);
        try {
            String res= SendUtil.sendHttpMsg(u,null);
            Map resmap = JSONObject.parseObject(res,Map.class);
            Map retMap = new HashMap();
            if (resmap != null && resmap.get("code").toString().equals("0")){
                retMap = JSONObject.parseObject(resmap.get("ret").toString(),Map.class);
            }else {
                retsultMap.put("code",1);
                return retsultMap;
            }
            Map dptInfoMap = JSONObject.parseObject(retMap.get("dptInfo").toString(),Map.class);
            retsultMap.put("cur",dptInfoMap.get("cur"));
            List<Map> glList = ItemsUtil.allGlCodeToName(JSONObject.parseObject(dptInfoMap.get("gl").toString(),List.class));
            retsultMap.put("gl",glList);
            retsultMap.put("ge",dptInfoMap.get("ge"));
            retsultMap.put("code",0);
            Map pInfoMap = JSONObject.parseObject(retMap.get("pInfo").toString(),Map.class);
            retsultMap.put("pid",pInfoMap.get("pid"));
            return retsultMap;
        } catch (GmException e) {
            retsultMap.put("code",1);
            e.printStackTrace();
            return retsultMap;
        }
    }

    @Override
    public void changeCurrency(String pid, String goodsId, String changeNum) throws GmException {
        String param  = "/changeCurrency?playerId="+pid+"&currency="+goodsId+"&changeNum="+changeNum;
        String u=this.dao.query(GameServer.class, Cnd.where("serverID","=",ManageConf.SERVERID)).get(0).getServerUrl().concat(param);
        String res= SendUtil.sendHttpMsg(u,null);
        System.out.println("res:"+res);
    }

    @Override
    public void changeGoods(String pid, String goodsId, String changeNum) throws GmException {
        String param  = "/changeGoods?playerId="+pid+"&goodsCid="+goodsId+"&changeNum="+changeNum;
        String u=this.dao.query(GameServer.class, Cnd.where("serverID","=",ManageConf.SERVERID)).get(0).getServerUrl().concat(param);
        String res= SendUtil.sendHttpMsg(u,null);
        System.out.println("res:"+res);
    }

}
