package com.master.module.count;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.ItemsUtil;
import com.master.bean.back.BanBack;
import com.master.bean.dispoly.GmLog;
import com.master.cache.GmDocConfig;
import com.master.gm.service.pay.PayServiceIF;
import com.master.gm.service.player.impl.PlayerService;
import com.master.module.GmModule;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/7/8.
 *
 * @ClassName PlayerController
 * @Description 角色操作
 */
@IocBean
@Ok("json:full")
@At(GmModule.PLAYER_URL)
@Fail("http:500")
public class PlayerController {

    private static Log log = Logs.get();

    @Inject
    private PlayerService playerService;
    @Inject
    private PayServiceIF payService;

    @At("/control")
    @Ok("jsp:jsp/other/operate/player")
    public void operatePlayer(HttpServletRequest request){
        List<Record> allServcer = playerService.getAllServer();
        request.getSession().setAttribute("allServer",allServcer);

    }

    @At("/replacementOrder")
    @Ok("jsp:jsp/other/operate/replacementOrder")
    public void replacementOrder(HttpServletRequest request){
        List<Record> allServcer = playerService.getAllServer();
        request.getSession().setAttribute("allServer",allServcer);

    }

    @At("/queryPlayerState")
    @Ok("json:full")
    public GmHashMap queryState(@Param("server") String sid,@Param("user") String name){
        try {
            return playerService.queryBanState(sid,name);
        } catch (GmException e) {
            log.error(e.getMessage());
            GmHashMap res=new GmHashMap();
            res.put("error",e.getMessage());
            return res;
        }
    }

    @At("/banOperate")
    @Ok("json:full")
    public GmHashMap ban(HttpSession session,@Param("serverId") String sid,@Param("pName") String name,@Param("cid") int id,@Param(value = "time",df = "0") long time){
        try {
            if(time>0){
                time=System.currentTimeMillis()+time*60000;
            }
            GmHashMap res=playerService.setBan(sid,name,id,time);
            UserForService user = (UserForService) session.getAttribute("GM");
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("player");
            bean.setContext("ban:".concat(name).concat(id+" of ").concat(res.toString()));
            this.playerService.getDao().insert(bean);
            return res;
        } catch (GmException e) {
            log.error(e.getMessage());
            GmHashMap res=new GmHashMap();
            res.put("error",e.getMessage());
            return res;
        }
    }
    @At("/orderOperate")
    @Ok("json:full")
    public GmHashMap orderSub(HttpSession session,@Param("server") String sid,@Param("orderId") String order){
        try {
            if(order.length()<1){
                throw new GmException("prams is null !");
            }
            GmHashMap res=playerService.sendOrder(sid,order);
            UserForService user = (UserForService) session.getAttribute("GM");
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("player");
            bean.setContext("order:".concat(order).concat("of ").concat(res.toString()));
            this.playerService.getDao().insert(bean);
            return res;
        }catch (GmException e){
            log.error(e.getMessage());
            GmHashMap res=new GmHashMap();
            res.put("error",e.getMessage());
            return res;
        }
    }


    @At("/msg")
    @Ok("jsp:jsp/other/query/player")
    public void comePlayerMsg(HttpServletRequest request){
        request.setAttribute("heroMsg", JSON.toJSONString(GmDocConfig.HERO));
        request.setAttribute("goodsMsg",JSON.toJSONString(GmDocConfig.GOODS));
        request.setAttribute("skillMsg",JSON.toJSONString(GmDocConfig.SKILL));
    }

    @At
    @Ok("json:full")
    public GmHashMap queryMsg(@Param("server") String sid,@Param("user") String name,@Param("type") int type){
        try {
            GmHashMap allMsg=playerService.queryPlayerMsg(sid,name,type);
            if(!allMsg.get("code").toString().equals("0")){
                GmHashMap msg=new GmHashMap();
                msg.put("error",allMsg.get("code").toString());
                return msg;
            }
            Map player= (Map) allMsg.get("ret");
            GmHashMap playerMsg=new GmHashMap();
            Map pInfo= (Map) player.get("pInfo");
            playerMsg.putAll(pInfo);
            playerMsg.remove("att");
            playerMsg.remove("times");
            List att= (List) pInfo.get("att");
            playerMsg.put("kou_cai",att.get(1));
            playerMsg.put("zhi_shang",att.get(2));
            playerMsg.put("qing_shang",att.get(3));
            playerMsg.put("yan_guang",att.get(4));
            playerMsg.put("bao_ji",att.get(5));
            playerMsg.put("bao_shang",att.get(6));
            playerMsg.put("fu_hao",pInfo.get("ff"));
            Map page= (Map) player.get("dptInfo");
            playerMsg.putAll(resetDptInfoCur((Map) page.get("cur")));
            playerMsg.put("bei_bao", ItemsUtil.glCodeToName(JSONObject.parseObject(page.get("gl").toString(),List.class)));
            Map titleInfo= (Map) player.get("titleInfo");
            playerMsg.put("chInfo",titleInfo.get("lst"));
            playerMsg.put("aId",player.get("actId"));
            Map buty= (Map) ((Map)player.get("butyInfo")).get("lst");
            List<Map> mns = new ArrayList<>();
            mns.addAll(buty.values());

            playerMsg.put("mnList",addMNName(mns));
            Map wrk= (Map) ((Map)player.get("wrkInfo")).get("lst");
            List<GmHashMap> wrkMap=new ArrayList<>();
            for(Object w:wrk.values()){
                GmHashMap res=new GmHashMap();
                res.put("cid",((Map)w).get("cid"));
                res.put("name",((Map)w).get("pname"));
                res.put("lv",((Map)w).get("lv"));
                res.put("exp",((Map)w).get("exp"));
                res.put("kc",((List)((Map)w).get("att")).get(1));
                res.put("zs",((List)((Map)w).get("att")).get(2));
                res.put("qs",((List)((Map)w).get("att")).get(3));
                res.put("yg",((List)((Map)w).get("att")).get(4));
                wrkMap.add(res);
            }
            playerMsg.put("xdList",addXDName(wrkMap));
            return playerMsg;
        } catch (GmException e) {
            e.printStackTrace();
            return e.getErrorMsg();
        }
    }

    @At("/playerList")
    @Ok("jsp:jsp/other/query/playerList")
    public void gotoPlayerList(HttpServletRequest request){
        List<BanBack> banBacks = playerService.queryPlayerList();
        request.getSession().setAttribute("banBacks",banBacks);
    }

    public List<Map> addMNName(List<Map> mns){

        for (Map map:mns) {
            if (map.get("cid").toString().equals("20200001")){
                map.put("pname","林思琦");
            }if (map.get("cid").toString().equals("20200002")){
                map.put("pname","李一玲");
            }if (map.get("cid").toString().equals("20200003")){
                map.put("pname","金敏恩");
            }if (map.get("cid").toString().equals("20200004")){
                map.put("pname","徐晨橙");
            }if (map.get("cid").toString().equals("20200005") ){
                map.put("pname","宋琪琪");
            }if (map.get("cid").toString().equals("20200006")){
                map.put("pname","长泽菲雅");
            }if (map.get("cid").toString().equals("20200007")){
                map.put("pname","韩娅");
            }if (map.get("cid").toString().equals("20200008")){
                map.put("pname","高桥椿");
            }if (map.get("cid").toString().equals("20200009")){
                map.put("pname","洛小桐");
            }if (map.get("cid").toString().equals("20200010")){
                map.put("pname","谢蜜儿");
            }if (map.get("cid").toString().equals("20200011")){
                map.put("pname","山崎樱");
            }if (map.get("cid").toString().equals("20200012")){
                map.put("pname","牧云");
            }if (map.get("cid").toString().equals("20200013")){
                map.put("pname","瑟薇拉");
            }if (map.get("cid").toString().equals("20200014")){
                map.put("pname","方岚");
            }if (map.get("cid").toString().equals("20200015")){
                map.put("pname","尹珍熙");
            }if (map.get("cid").toString().equals("20200016") ){
                map.put("pname","苏娅");
            }if (map.get("cid").toString().equals("20200017")){
                map.put("pname","赵馨");
            }if (map.get("cid").toString().equals("20200018")){
                map.put("pname","宋慧伊");
            }if (map.get("cid").toString().equals("20200019")){
                map.put("pname","莫可维");
            }if (map.get("cid").toString().equals("20200020")){
                map.put("pname","曲烟");
            }if (map.get("cid").toString().equals("20200021")){
                map.put("pname","罗薇薇");
            }if (map.get("cid").toString().equals("20200033")){
                map.put("pname","陆可可");
            }if (map.get("cid").toString().equals("20200034")){
                map.put("pname","米朵儿");
            }if (map.get("cid").toString().equals("20200035")){
                map.put("pname","菲比");
            }if (map.get("cid").toString().equals("20200036")){
                map.put("pname","奥尔维拉");
            }if (map.get("cid").toString().equals("20200037")){
                map.put("pname","石原绫濑");
            }if (map.get("cid").toString().equals("20200038")){
                map.put("pname","林悦");
            }if (map.get("cid").toString().equals("20200039")){
                map.put("pname","白凰语");
            }
        }

        return mns;
    }

    public List<GmHashMap> addXDName(List<GmHashMap> xds){
        for (GmHashMap map:xds) {
            if (map.get("cid").toString().equals("20100001")){
                map.put("pname","王三多");
            }if (map.get("cid").toString().equals("20100002") ){
                map.put("pname","易教授");
            }if (map.get("cid").toString().equals("20100003") ){
                map.put("pname","周星星");
            }if (map.get("cid").toString().equals("20100004") ){
                map.put("pname","葛大爷");
            }if (map.get("cid").toString().equals("20100005") ){
                map.put("pname","终结者");
            }if (map.get("cid").toString().equals("20100006") ){
                map.put("pname","周董");
            }if (map.get("cid").toString().equals("20100007") ){
                map.put("pname","陈浩南");
            }if (map.get("cid").toString().equals("20100008") ){
                map.put("pname","楚留香");
            }if (map.get("cid").toString().equals("20100009") ){
                map.put("pname","千玺");
            }if (map.get("cid").toString().equals("20100010") ){
                map.put("pname","何老师");
            }if (map.get("cid").toString().equals("20100011") ){
                map.put("pname","谭校长");
            }if (map.get("cid").toString().equals("20100012") ){
                map.put("pname","彪哥");
            }if (map.get("cid").toString().equals("20100013") ){
                map.put("pname","张哥哥");
            }if (map.get("cid").toString().equals("20100014") ){
                map.put("pname","天后菲");
            }if (map.get("cid").toString().equals("20100015") ){
                map.put("pname","呆萌兴");
            }if (map.get("cid").toString().equals("20100016") ){
                map.put("pname","鹿校草");
            }if (map.get("cid").toString().equals("20100017") ){
                map.put("pname","徐老怪");
            }if (map.get("cid").toString().equals("20100018") ){
                map.put("pname","梅姑");
            }if (map.get("cid").toString().equals("20100019") ){
                map.put("pname","王首富");
            }if (map.get("cid").toString().equals("20100020") ){
                map.put("pname","朱哥哥");
            }if (map.get("cid").toString().equals("20100021") ){
                map.put("pname","Jackie陈");
            }if (map.get("cid").toString().equals("20100022") ){
                map.put("pname","张老谋");
            }if (map.get("cid").toString().equals("20100023") ){
                map.put("pname","巴股神");
            }if (map.get("cid").toString().equals("20100024") ){
                map.put("pname","金馆长");
            }if (map.get("cid").toString().equals("20100025") ){
                map.put("pname","费玉污");
            }if (map.get("cid").toString().equals("20100026") ){
                map.put("pname","万人迷");
            }if (map.get("cid").toString().equals("20100027") ){
                map.put("pname","本山大叔");
            }if (map.get("cid").toString().equals("20100028") ){
                map.put("pname","大锤");
            }if (map.get("cid").toString().equals("20100029") ){
                map.put("pname","逍遥哥");
            }if (map.get("cid").toString().equals("20100030") ){
                map.put("pname","姚大囧");
            }if (map.get("cid").toString().equals("20100031") ){
                map.put("pname","老干妈");
            }if (map.get("cid").toString().equals("20100032") ){
                map.put("pname","项少龙");
            }if (map.get("cid").toString().equals("20100033") ){
                map.put("pname","福尔摩斯");
            }if (map.get("cid").toString().equals("20100034")){
                map.put("pname","乔帮主");
            }if (map.get("cid").toString().equals("20100035") ){
                map.put("pname","王校长");
            }if (map.get("cid").toString().equals("20100036") ){
                map.put("pname","杰克马");
            }if (map.get("cid").toString().equals("20100037") ){
                map.put("pname","小马哥");
            }if (map.get("cid").toString().equals("20100038") ){
                map.put("pname","哈林哥");
            }if (map.get("cid").toString().equals("20100039") ){
                map.put("pname","李超人");
            }if (map.get("cid").toString().equals("20100040") ){
                map.put("pname","华仔");
            }if (map.get("cid").toString().equals("20100041") ){
                map.put("pname","张歌神");
            }if (map.get("cid").toString().equals("20100042") ){
                map.put("pname","黎天王");
            }if (map.get("cid").toString().equals("20100043") ){
                map.put("pname","城城");
            }if (map.get("cid").toString().equals("20100044")){
                map.put("pname","灵儿");
            }if (map.get("cid").toString().equals("20100045")){
                map.put("pname","月如");
            }if (map.get("cid").toString().equals("20100046") ){
                map.put("pname","雪见");
            }if (map.get("cid").toString().equals("20100047") ){
                map.put("pname","紫萱");
            }if (map.get("cid").toString().equals("20100048")){
                map.put("pname","龙葵");
            }if (map.get("cid").toString().equals("20100049")){
                map.put("pname","葛尔丹");
            }
        }
        return xds;
    }

    public Map resetDptInfoCur(Map cur){

        if (!cur.containsKey("80400001")){
            cur.put("80400001",0);
        }
        if (!cur.containsKey("80400002")){
            cur.put("80400002",0);
        }
        if (!cur.containsKey("80400003")){
            cur.put("80400003",0);
        }
        if (!cur.containsKey("80400004")){
            cur.put("80400004",0);
        }
        if (!cur.containsKey("80400005")){
            cur.put("80400005",0);
        }
        if (!cur.containsKey("80400006")){
            cur.put("80400006",0);
        }
        if (!cur.containsKey("80400007")){
            cur.put("80400007",0);
        }
        if (!cur.containsKey("80400008")){
            cur.put("80400008",0);
        }
        if (!cur.containsKey("80400009")){
            cur.put("80400009",0);
        }
        if (!cur.containsKey("80400011")){
            cur.put("80400011",0);
        }
        if (!cur.containsKey("80400012")){
            cur.put("80400012",0);
        }
        if (!cur.containsKey("80400013")){
            cur.put("80400013",0);
        }
        if (!cur.containsKey("80400014")){
            cur.put("80400014",0);
        }
        if (!cur.containsKey("80400015")){
            cur.put("80400015",0);
        }
        if (!cur.containsKey("80400016")){
            cur.put("80400016",0);
        }

        return cur;
    }

}
