package com.master.gm.service.manage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.SendUtil;
import com.master.bean.dispoly.GameServer;
import com.master.bean.web.ShopDesign;
import com.master.gm.BaseService;
import com.master.gm.service.manage.GameConfigServiceIF;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/8/2.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
public class GameConfigService extends BaseService implements GameConfigServiceIF{
    @Override
    public List<GmHashMap> getAllActivity() throws GmException {
        List<GmHashMap> res =new ArrayList<>();
        List<GameServer> sidList=this.dao.query(GameServer.class,null);
        for (GameServer sc:sidList){
            String url=sc.getServerUrl().concat("/queryActivity");
            try {
              Map m= JSON.parseObject(SendUtil.sendHttpMsg(url,null),GmHashMap.class);
              List<Map> ls= (List<Map>) m.get("ret");
              for(Map map:ls){
                  GmHashMap ghm=new GmHashMap();
                  ghm.put("sid",sc.getServerID());
                  ghm.put("aid",map.get("id"));
                  ghm.put("status",map.get("status"));
                  ghm.put("start", DateUtil.formatDateTime(Long.parseLong(map.get("startDate").toString())));
                  ghm.put("end",DateUtil.formatDateTime(Long.parseLong(map.get("endDate").toString())));
                  try{
                      String ct=map.get("clientAtts").toString();
                      GmHashMap t= JSON.parseObject(ct,GmHashMap.class);
                      ghm.put("title",t.get("title"));
                  }catch (Exception e){
                      ghm.put("title","error!");
                  }
                  res.add(ghm);
              }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public void deleteActivity(String sid, String id) throws GmException {
        String url=this.dao.fetch(GameServer.class,Cnd.where("serverID","=",sid)).getServerUrl().concat("/deleteActivity?");
        try {
            Map m= JSON.parseObject(SendUtil.sendHttpMsg(url,"activityId=".concat(id)),GmHashMap.class);
            System.out.println(m);
        } catch (Exception e) {
           throw new GmException(e.getMessage());
        }
    }


    @Override
    public List<GameServer> queryAllConfig() throws GmException {
        return this.dao.query(GameServer.class,null);
    }

    @Override
    public GmHashMap changeServerState(String sid,int state) throws GmException {
        String[] sidArray=sid.split(",");
        String url;
        GmHashMap res=new GmHashMap();
        for(String s:sidArray){
            try {
                url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",s)).getServerUrl().concat("/stateChange");
                Map r=JSON.parseObject(SendUtil.sendHttpMsg(url,"state=".concat(String.valueOf(state))),Map.class);
                res.put(s,r);
                if (r.get("code").toString().equals("0")){
                    this.dao.update("gm_config.game_server",
                            Chain.make("state",state),Cnd.where("serverID","=",s));
                }
            } catch (Exception e) {
                res.put(s,e.getMessage());
            }
        }
        return res;
    }

    @Override
    public void changeServerRecommend(String sid) throws GmException {
        int i=this.dao.update("gm_config.game_server",
                Chain.make("recommend",0),null);
        System.out.println(i);
        int j=this.dao.update("gm_config.game_server",
                Chain.make("recommend",1),Cnd.where("serverID","=",sid));
        System.out.println(j);
    }

    @Override
    public List<ShopDesign> getAllShop() throws GmException {
        List<GameServer> sidList=this.dao.query(GameServer.class,null);
        List<ShopDesign> beans=new ArrayList<>();
        for (GameServer sc:sidList){
            String url=sc.getServerUrl().concat("/queryStore");
            try {
                Map m= JSON.parseObject(SendUtil.sendHttpMsg(url,null),GmHashMap.class);
                List<Map> ls= (List<Map>) m.get("ret");
                for(Map map:ls){
                    beans.add(new ShopDesign(map,sc.getServerID()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return beans;
    }

    @Override
    public void updateShop(ShopDesign bean) throws GmException {
        if(bean.getId().equals("-1")){
            if(bean.getServer().equals("all")){
                List<GameServer> sidList=this.dao.query(GameServer.class,null);
                for (GameServer sc:sidList){
                    String url=sc.getServerUrl().concat("/createStore");
                    try {
                        SendUtil.sendHttpMsg(url,"store=".concat(JSON.toJSONString(bean.getMapData())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
                String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",bean.getServer())).getServerUrl().concat("/createStore");
                try {
                    SendUtil.sendHttpMsg(url,"store=".concat(JSON.toJSONString(bean.getMapData())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",bean.getServer())).getServerUrl().concat("/updateStore");
            try {
                SendUtil.sendHttpMsg(url,"store=".concat(JSON.toJSONString(bean.getMapData())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delShop(int id,String sid) throws GmException {
       String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",sid)).getServerUrl().concat("/deleteStore");
        try {
            SendUtil.sendHttpMsg(url,"storeId=".concat(String.valueOf(id)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateExc(String sid,String data) throws GmException {
        String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",sid)).getServerUrl().concat("/updateConvert");
        try {
            SendUtil.sendHttpMsg(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateExp(String sid,String data) throws GmException {
        String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",sid)).getServerUrl().concat("/updateExplore");
        try {
            SendUtil.sendHttpMsg(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GmHashMap> getAllExc() throws GmException {
        List<GameServer> sidList=this.dao.query(GameServer.class,null);
        List<GmHashMap> beans=new ArrayList<>();
        for (GameServer sc:sidList){
            String url=sc.getServerUrl().concat("/queryConvert");
            try {
                Map m= JSON.parseObject(SendUtil.sendHttpMsg(url,null),Map.class);
                List<JSONObject> ls= (List<JSONObject>) m.get("ret");
                for(JSONObject map:ls){
                    GmHashMap res=new GmHashMap();
                    res.putAll(map);
                    res.put("sid",sc.getServerID());
                    beans.add(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beans;
    }

    @Override
    public List<GmHashMap> getAllExp() throws GmException {
        List<GameServer> sidList=this.dao.query(GameServer.class,null);
        List<GmHashMap> beans=new ArrayList<>();
        for (GameServer sc:sidList){
            String url=sc.getServerUrl().concat("/queryExplore");
            try {
                Map m= JSON.parseObject(SendUtil.sendHttpMsg(url,null),Map.class);
                List<JSONObject> ls= (List<JSONObject>) m.get("ret");
                for(JSONObject map:ls){
                    GmHashMap res=new GmHashMap();
                    res.putAll(map);
                    res.put("sid",sc.getServerID());
                    beans.add(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beans;
    }

    @Override
    public Object getAllServer() {
        return this.dao.query(GameServer.class,null);
    }
}
