package com.master.gm.service.player.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.context.DataType;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.ReadFile;
import com.gmdesign.util.SendUtil;
import com.gmdesign.util.URLTool;
import com.master.bean.back.BanBack;
import com.master.bean.back.Player;
import com.master.bean.dispoly.GameServer;
import com.master.bean.dispoly.Parameter;
import com.master.gm.BaseService;
import com.master.gm.job2.ObjectJob;
import com.master.gm.service.player.PlayerServiceIF;
import com.master.util.ManageConf;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.SchedulerException;

/**
 * Created by DJL on 2017/7/8.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
public class PlayerService extends BaseService implements PlayerServiceIF{

    @Inject
    private ObjectJob objectJob;

    @Override
    public GmHashMap queryPlayerMsg(String sid, String name, int type) throws GmException {
        String p="playerId=";
        if(type==1){
            p="playerName=";
        }else if(type==3){
            String u=this.dao.fetch(Parameter.class,"loginUrl").getPrams().concat("/getUid?account="+name);
            String res=SendUtil.sendHttpMsg(u,null);
            if(res.equals("30001")){
                throw new GmException("账号不存在`");
            }
            Player player=this.dao.fetch(Player.class,Cnd.where("sid","=",sid).and("uid","=",res));
            if(player==null){
                List<GmHashMap> list=new ReadFile(sid,DataType.PLAYER,DateUtil.getDateString(new Date())).getMsg();
                for (GmHashMap m:list){
                    if(m.get("actId").toString().equals(res)){
                        name=m.get("pid").toString();
                        break;
                    }
                }
            }else{
                name=player.getPid();
            }
        }
        String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",sid)).getServerUrl().concat("/loadPlayer?".concat(p)+name);
        try {
            return JSON.parseObject(SendUtil.sendHttpMsg(url,null),GmHashMap.class);
        } catch (Exception e) {
           throw new GmException(e.getMessage());
        }
    }

    @Override
    public GmHashMap queryBanState(String sid, String name) throws GmException {
        if(name==null){
            throw new GmException("player name is null!~");
        }
        GmHashMap res =new GmHashMap();
        res.put("ban1",0);
        res.put("ban2",0);
        List<BanBack> banBacks=this.dao.query(BanBack.class, Cnd.where("sid","=",sid).and("playerName","=",name));
        if(banBacks!=null&&!banBacks.isEmpty()){
            for(BanBack bean:banBacks){
                if(bean.getType()==1){
                    res.put("ban1",1);
                }else if (bean.getType()==2){
                    res.put("ban2",1);
                }
            }
        }
        return res;
    }

    @Override
    public GmHashMap setBan(String sid, String name, int cid,long time) throws GmException {
        String param="playerName="+name+"&state=";
        String url;
        String qurtzUrl = null;
        BanBack bean =new BanBack();
        bean.setName(name);
        bean.setServer(sid);
        if(cid==101){
            url=this.dao.fetch(GameServer.class,sid).getServerUrl().concat("/setPlayerState?".concat(param)+1);
            if (time > 0) {
                qurtzUrl=this.dao.fetch(GameServer.class,sid).getServerUrl().concat("/resetPlayerState?".concat(param)+1);
            }
            bean.setType(1);
            bean.setLogTime(System.currentTimeMillis());
        }else if(cid==102){
            url=this.dao.fetch(GameServer.class,sid).getServerUrl().concat("/setPlayerState?".concat(param)+2);
            if (time > 0) {
                qurtzUrl=this.dao.fetch(GameServer.class,sid).getServerUrl().concat("/resetPlayerState?".concat(param)+2);
            }
            bean.setType(2);
            bean.setLogTime(System.currentTimeMillis());
        }else if(cid==103){
            bean.setType(1);
            url=this.dao.fetch(GameServer.class,sid).getServerUrl().concat("/resetPlayerState?".concat(param)+1);
        }else if(cid==104){
            bean.setType(2);
            url=this.dao.fetch(GameServer.class,sid).getServerUrl().concat("/resetPlayerState?".concat(param)+2);
        }else{
            throw new GmException("cid is error`~~");
        }
        if(time>0){
            try {
                objectJob.addJob(time,qurtzUrl,bean.getServer(),bean.getName(),bean.getType());
            } catch (SchedulerException e) {
                e.printStackTrace();
                throw new GmException("定时任务创建失败");
            }
        }
        try {
            Map r = JSONObject.parseObject(SendUtil.sendHttpMsgResponseByte(url,null),Map.class);
            System.out.println("调用游戏服返回："+r);
            if(!r.get("code").toString().equals("0")){
                throw new GmException("error:"+r.get("code").toString());
            }
            if(cid==101||cid==102){
                this.dao.insert(bean);
            }else{
                bean=this.dao.fetch(BanBack.class,Cnd.where("sid","=",bean.getServer()).and("playerName","=",bean.getName()).and("type","=",bean.getType()));
                this.dao.delete(bean);
            }
        } catch (Exception e) {
            throw new GmException(e.getMessage());
        }

        return queryBanState(sid,name);
    }

    @Override
    public GmHashMap sendOrder(String sid,String order) throws GmException {

        Map param = new HashMap();
        param.put("sid",sid);
        param.put("orderNum",order);
        String paramStr = "msg="+JSONObject.toJSONString(param);
        List<Map> orders = JSONObject.parseObject(URLTool.sendMsg(ManageConf.URL+":9895/queryOrder/queryOrder",paramStr),List.class);
        GmHashMap res=new GmHashMap();
        sid="deliverUrl";
        String url=this.dao.fetch(Parameter.class,sid).getPrams().concat("/reissueOrder?playerId="+orders.get(0).get("pid").toString()+"&orderNumber="+orders.get(0).get("orderNumber").toString()+"&goodsId="+orders.get(0).get("goodsId").toString());
        res.put("res",URLTool.sendMsg(url,null));
        return res;
    }

    @Override
    public List<Record> getAllServer() {
        return this.dao.query("gm_config.game_server",null);
    }

    @Override
    public List<BanBack> queryPlayerList() {
        return this.dao.query(BanBack.class,null);
    }
}
