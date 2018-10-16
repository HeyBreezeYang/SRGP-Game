package com.master.module.count;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.bean.web.FormParameter;
import com.master.bean.web.QueryParameter;
import com.master.gm.service.currency.CurrencyQueryServiceIF;
import com.master.gm.service.currency.QueryType;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/7/5.
 *
 * @ClassName QueryCountController
 * @Description 查询模块
 */
@IocBean
@Ok("json:full")
@At(GmModule.QUERY_URL)
@Fail("http:500")
public class QueryCountController {

    private static final String THIS_URL="jsp:jsp/other/query/currency";
    @Inject
    private CurrencyQueryServiceIF currencyQueryService;

    private void AndFormMsg(HttpServletRequest request,FormParameter... msg){
        List<FormParameter> formList=new ArrayList<>();
        formList.addAll(Arrays.asList(msg));
        request.setAttribute("formList",formList);
    }

    @At("/queryData")
    @Ok("json:full")
    public List<GmHashMap> queryDataList(@Param("..") QueryParameter param){
        System.out.println("接收到的参数:"+param);


        try {
            List<GmHashMap> ls = currencyQueryService.queryMassage(param.getStart(),param.getEnd(),param.getServerId(),param.getOther());
            System.out.println("返回结果:"+ls);
            return ls;
        } catch (GmException e) {
            e.printStackTrace();
            List<GmHashMap> error=new ArrayList<>();
            error.add(e.getErrorMsg());
            return error;
        }
    }

    @At("/queryR")
    @Ok(THIS_URL)
    public void queryR(HttpServletRequest request){
        FormParameter rank=new FormParameter();
        rank.setId(1);
        rank.setQueryName("VIP排名");
        rank.setThread(new String[]{"VIP等级","玩家名","vip经验"});
        rank.setKey(new String[]{"lv","pname","exp"});
        rank.setQueryType(QueryType.VIP_RANK);
        setParameter(rank,"server","channel");

        FormParameter num=new FormParameter();
        num.setId(2);
        num.setQueryName("VIP人数");
        num.setThread(new String[]{"VIP等级","人数"});
        num.setKey(new String[]{"lv","num"});
        num.setQueryType(QueryType.VIP_NUM);
        setParameter(num,"server","channel");


        FormParameter all=new FormParameter();
        all.setId(3);
        all.setQueryName("总VIP人数");
        all.setThread(new String[]{"服务器","人数"});
        all.setKey(new String[]{"sid","num"});
        all.setQueryType(QueryType.VIP_NUM_ALL);
        setParameter(all,"channel");

        AndFormMsg(request,rank,num,all);
    }

    @At("/queryTask")
    @Ok(THIS_URL)
    public void queryTask(HttpServletRequest request){
        FormParameter rw=new FormParameter();
        rw.setId(1);
        rw.setQueryName("每日任务统计");
        rw.setThread(new String[]{"日期","任务名","任务","完成人数"});
        rw.setKey(new String[]{"date","taskName","task","num"});
        rw.setQueryType(QueryType.TASK_MR);
        setParameter(rw,"server","channel","start","end");

//        FormParameter hd=new FormParameter();
//        hd.setId(2);
//        hd.setQueryName("活跃度统计");
//        hd.setThread(new String[]{"日期","活动阶段","完成人数"});
//        hd.setKey(new String[]{"date","task","num"});
//        hd.setQueryType(QueryType.TASK_HD);
//        setParameter(hd,"server","channel","start","end");

        FormParameter gk=new FormParameter();
        gk.setId(2);
        gk.setQueryName("关卡完成统计");
        gk.setThread(new String[]{"日期","任务名","章节","通关人数","通关次数"});
        gk.setKey(new String[]{"date","taskName","task","passNum","passFre"});
        gk.setQueryType(QueryType.TASK_GK);
        setParameter(gk,"server","channel","start","end");

        AndFormMsg(request,rw,gk);
    }

    @At("/queryLeague")
    @Ok(THIS_URL)
    public void queryLeague(HttpServletRequest request){

        FormParameter rank=new FormParameter();
        rank.setId(1);
        rank.setQueryName("联盟排行");
        rank.setThread(new String[]{"联盟","资金","人数","等级","主人"});
        rank.setKey(new String[]{"nm","val","num","lv","leder"});
        rank.setQueryType(QueryType.LEAGUE_R);
        setParameter(rank,"server");

        FormParameter boss=new FormParameter();
        boss.setId(2);
        boss.setQueryName("联盟BOSS（查询参数为联盟名字）");
        boss.setThread(new String[]{"日期","副本ID","开启个数","完成个数"});
        boss.setKey(new String[]{"date","bid","openNum","endNum"});
        boss.setQueryType(QueryType.LEAGUE_B);
        setParameter(boss,"server","oName","start","end");

        FormParameter user=new FormParameter();
        user.setId(3);
        user.setQueryName("联盟成员（查询参数为联盟名字）");
        user.setThread(new String[]{"名字","权限","战斗力","等级","贡献","加入时间","登录","离线"});
        user.setKey(new String[]{"nm","right","ff","lv","hisCoin","join","logout","login"});
        user.setQueryType(QueryType.LEAGUE_M);
        setParameter(user,"server","oName");

        AndFormMsg(request,rank,boss,user);
    }

    @At("/queryShop")
    @Ok(THIS_URL)
    public void gameShop(HttpServletRequest request){
        FormParameter shopCount=new FormParameter();
        shopCount.setId(1);
        shopCount.setQueryName("商店统计(查询参数为商店ID)");
        shopCount.setThread(new String[]{"日期","道具","数量"});
        shopCount.setKey(new String[]{"date","goodsName","num"});
        shopCount.setQueryType(QueryType.SHOP_C);
        setParameter(shopCount,"server","channel","oName","start","end");

        FormParameter shopDetail=new FormParameter();
        shopDetail.setId(2);
        shopDetail.setQueryName("商品详情(查询参数为商品ID)");
        shopDetail.setThread(new String[]{"时间","玩家","数量","商店类型","途径"});
        shopDetail.setKey(new String[]{"date","pname","value","spType","cmdName"});
        shopDetail.setQueryType(QueryType.SHOP_D);
        setParameter(shopDetail,"server","oName","start","end");

        AndFormMsg(request,shopCount,shopDetail);
    }

    @At("/queryGoods")
    @Ok(THIS_URL)
    public void gameGoods(HttpServletRequest request){
        FormParameter goods=new FormParameter();
        goods.setId(1);
        goods.setQueryName("道具查询");
        goods.setThread(new String[]{"时间","道具","数量","途径"});
        goods.setKey(new String[]{"date","goodsName","num","cmdName"});
        goods.setQueryType(QueryType.GOODS);
        setParameter(goods,"server","pName","start","end");

        AndFormMsg(request,goods);
    }

    @At("/countBoss")
    @Ok(THIS_URL)
    public void gameWorldBoss(HttpServletRequest request){
        FormParameter ws_boss=new FormParameter();
        ws_boss.setId(1);
        ws_boss.setQueryName("晚上BOSS统计");
        ws_boss.setThread(new String[]{"日期","次数","人数"});
        ws_boss.setKey(new String[]{"logTime","frequency","num"});
        ws_boss.setQueryType(QueryType.WS_BOSS_2);
        setParameter(ws_boss,"server","channel","start","end");

        FormParameter zw_boss=new FormParameter();
        zw_boss.setId(2);
        zw_boss.setQueryName("中午BOSS统计");
        zw_boss.setThread(new String[]{"日期","通关层数","人数"});
        zw_boss.setKey(new String[]{"date","nop","num"});
        zw_boss.setQueryType(QueryType.ZW_BOSS_2);
        setParameter(zw_boss,"server","start","end");

        AndFormMsg(request,ws_boss,zw_boss);
    }
    @At("/detailedBoss")
    @Ok(THIS_URL)
    public void gameDetailedBoss(HttpServletRequest request){
        FormParameter ws_boss=new FormParameter();
        ws_boss.setId(1);
        ws_boss.setQueryName("晚上BOSS个人详情");
        ws_boss.setThread(new String[]{"时间","伤害值","员工CID"});
        ws_boss.setKey(new String[]{"date","hurt","worker"});
        ws_boss.setQueryType(QueryType.WS_BOSS_1);
        setParameter(ws_boss,"server","pName","start","end");

        FormParameter zw_boss=new FormParameter();
        zw_boss.setId(2);
        zw_boss.setQueryName("中午BOSS个人详情");
        zw_boss.setThread(new String[]{"时间","层数"});
        zw_boss.setKey(new String[]{"date","index"});
        zw_boss.setQueryType(QueryType.ZW_BOSS_1);
        setParameter(zw_boss,"server","pName","start","end");

        AndFormMsg(request,ws_boss,zw_boss);
    }


    @At("/systemDetailed")
    @Ok(THIS_URL)
    public void gameCurrencyDetailed(HttpServletRequest request){
        FormParameter ml=new FormParameter();
        ml.setId(1);
        ml.setQueryName("个人魅力值");
        ml.setThread(new String[]{"时间","变化","当前值","美女ID","用途"});
        ml.setKey(new String[]{"date","change","after","cid","cmdName"});
        ml.setQueryType(QueryType.CURR_MLZ_1);
        setParameter(ml,"server","pName","start","end");

        FormParameter qmd=new FormParameter();
        qmd.setId(2);
        qmd.setQueryName("个人亲密度");
        qmd.setThread(new String[]{"时间","变化","当前值","美女ID","用途"});
        qmd.setKey(new String[]{"date","change","after","cid","cmdName"});
        qmd.setQueryType(QueryType.CURR_QMD_1);
        setParameter(qmd,"server","pName","start","end");

//        FormParameter cl=new FormParameter();
//        cl.setId(3);
//        cl.setQueryName("个人茶楼分数");
//        cl.setThread(new String[]{"时间","变化","当前值","用途"});
//        cl.setKey(new String[]{"date","change","after","cmdName"});
//        cl.setQueryType(QueryType.CURR_CL_1);
//        setParameter(cl,"server","pName","start","end");

        FormParameter ylz=new FormParameter();
        ylz.setId(3);
        ylz.setQueryName("个人娱乐值");
        ylz.setThread(new String[]{"时间","积分","参加","捣乱"});
        ylz.setKey(new String[]{"date","score","join","breach"});
        ylz.setQueryType(QueryType.CURR_YLZ_1);
        setParameter(ylz,"server","pName","start","end");

        AndFormMsg(request,ml,qmd,ylz);
    }


    @At("/systemForServer")
    @Ok(THIS_URL)
    public void gameSystemForServer(HttpServletRequest request){
        FormParameter qmd=new FormParameter();
        qmd.setId(1);
        qmd.setQueryName("亲密度统计");
        qmd.setThread(new String[]{"日期","变化","人数","操作"});
        qmd.setKey(new String[]{"date","change","num","cmdName"});
        qmd.setQueryType(QueryType.CURR_QMD_2);
        setParameter(qmd,"server","start","end","channel");

        FormParameter mlz=new FormParameter();
        mlz.setId(2);
        mlz.setQueryName("魅力值统计");
        mlz.setThread(new String[]{"日期","变化","人数","操作"});
        mlz.setKey(new String[]{"date","change","num","cmdName"});
        mlz.setQueryType(QueryType.CURR_MLZ_2);
        setParameter(mlz,"server","start","end","channel");

//        FormParameter cl=new FormParameter();
//        cl.setId(3);
//        cl.setQueryName("茶楼统计");
//        cl.setThread(new String[]{"日期","变化","人数","操作"});
//        cl.setKey(new String[]{"date","change","num","cmdName"});
//        cl.setQueryType(QueryType.CURR_CL_2);
//        setParameter(cl,"server","start","end","channel");

//        FormParameter ylz=new FormParameter();
//        ylz.setId(4);
//        ylz.setQueryName("娱乐值统计");
//        ylz.setThread(new String[]{"日期","积分","人数","CID"});
//        ylz.setKey(new String[]{"date","change","num","cid"});
//        ylz.setQueryType(QueryType.CURR_YLZ_2);
//        setParameter(ylz,"server","start","end","channel");

        AndFormMsg(request,qmd,mlz);
    }

    @At("/currencyForServer")
    @Ok(THIS_URL)
    public void gameCurrencyForServer(HttpServletRequest request){
        FormParameter jy=new FormParameter();
        jy.setId(1);
        jy.setQueryName("机遇统计");
        jy.setThread(new String[]{"日期","产出/消耗","人数"});
        jy.setKey(new String[]{"date","change","num"});
        jy.setQueryType(QueryType.CURR_JY_2);
        setParameter(jy,"server","start","end","channel");

        FormParameter rm=new FormParameter();
        rm.setId(2);
        rm.setQueryName("人脉统计");
        rm.setThread(new String[]{"日期","产出/消耗","人数"});
        rm.setKey(new String[]{"date","change","num"});
        rm.setQueryType(QueryType.CURR_RM_2);
        setParameter(rm,"server","start","end","channel");

        FormParameter xj=new FormParameter();
        xj.setId(3);
        xj.setQueryName("现金统计");
        xj.setThread(new String[]{"日期","产出/消耗","人数"});
        xj.setKey(new String[]{"date","change","num"});
        xj.setQueryType(QueryType.CURR_XJ_2);
        setParameter(xj,"server","start","end","channel");

        FormParameter zs=new FormParameter();
        zs.setId(4);
        zs.setQueryName("钻石统计");
        zs.setThread(new String[]{"日期","产出/消耗","人数"});
        zs.setKey(new String[]{"date","change","num"});
        zs.setQueryType(QueryType.CURR_ZS_2);
        setParameter(zs,"server","start","end","channel");

        FormParameter ktv=new FormParameter();
        ktv.setId(5);
        ktv.setQueryName("积分统计");
        ktv.setThread(new String[]{"日期","变化","人数"});
        ktv.setKey(new String[]{"date","change","num"});
        ktv.setQueryType(QueryType.CURR_KTV_2);
        setParameter(ktv,"server","start","end","channel");

        AndFormMsg(request,jy,rm,xj,zs,ktv);
    }

    @At("/currency")
    @Ok(THIS_URL)
    public void gameCurrency(HttpServletRequest request){
        FormParameter jy=new FormParameter();
        jy.setId(1);
        jy.setQueryName("个人机遇");
        jy.setThread(new String[]{"时间","产出/消耗","当前值","用途"});
        jy.setKey(new String[]{"date","change","after","cmdName"});
        jy.setQueryType(QueryType.CURR_JY_1);
        setParameter(jy,"server","start","end","pName");

        FormParameter rm=new FormParameter();
        rm.setId(2);
        rm.setQueryName("个人人脉");
        rm.setThread(new String[]{"时间","产出/消耗","当前值","用途"});
        rm.setKey(new String[]{"date","change","after","cmdName"});
        rm.setQueryType(QueryType.CURR_RM_1);
        setParameter(rm,"server","start","end","pName");

        FormParameter xj=new FormParameter();
        xj.setId(3);
        xj.setQueryName("个人现金");
        xj.setThread(new String[]{"时间","产出/消耗","当前值","用途"});
        xj.setKey(new String[]{"date","change","after","cmdName"});
        xj.setQueryType(QueryType.CURR_XJ_1);
        setParameter(xj,"server","start","end","pName");

        FormParameter zs=new FormParameter();
        zs.setId(4);
        zs.setQueryName("个人钻石");
        zs.setThread(new String[]{"时间","产出/消耗","当前值","用途"});
        zs.setKey(new String[]{"date","change","after","cmdName"});
        zs.setQueryType(QueryType.CURR_ZS_1);
        setParameter(zs,"server","start","end","pName");

        FormParameter ktv=new FormParameter();
        ktv.setId(5);
        ktv.setQueryName("个人积分");
        ktv.setThread(new String[]{"时间","变化","当前值","用途"});
        ktv.setKey(new String[]{"date","change","after","cmdName"});
        ktv.setQueryType(QueryType.CURR_KTV_1);
        setParameter(ktv,"server","pName","start","end");
        AndFormMsg(request,jy,rm,xj,zs,ktv);
    }

    @At("/playerMsg")
    @Ok(THIS_URL)
    public void playerMsg(HttpServletRequest request){
        FormParameter player=new FormParameter();
        player.setId(1);
        player.setQueryName("部分详情");
        player.setThread(new String[]{"角色名","累计充值","当前钻石","等级","战斗力","当前金币","最后登录","最后充值"});
        player.setKey(new String[]{"name","payment","treasure","lv","fight","money","lastLoginTime","lastPayTime"});
        player.setQueryType(QueryType.PLAYER);
        player.getParameter().put("server",true);
        player.getParameter().put("player",true);

        FormParameter rank=new FormParameter();
        rank.setId(2);
        rank.setQueryName("玩家排名");
        rank.setThread(new String[]{"角色名","等级","公会","称号","排行数据"});
        rank.setKey(new String[]{"nm","lv","guinm","ttl","val"});
        rank.setQueryType(QueryType.PLAYER_RANK);
        rank.getParameter().put("server",true);
        Map<String,String> other=new HashMap<>();
        other.put("1","战斗力");
        other.put("2","剧情");
        other.put("3","亲密度");
        other.put("4","世界BOSS");
        other.put("5","宴会");
        other.put("6","讲道理");
        other.put("7","辩论");
        other.put("8","剁手");
        other.put("9","流行");
        rank.setOtherParameter(other);
        AndFormMsg(request,player,rank);
    }

    @At("/snapshot")
    @Ok(THIS_URL)
    public void everyDateSnapshot(HttpServletRequest request){
        FormParameter login=new FormParameter();
        login.setId(1);
        login.setQueryName("登录时长");
        login.setThread(new String[]{"日期","DAU","平均在线时长(分钟/m)"});
        login.setKey(new String[]{"logTime","dau","olTime"});
        login.setQueryType(QueryType.LOGIN_TIME);
        setParameter(login,"server","start","end","channel");

       /* FormParameter cu=new FormParameter();
        cu.setId(2);
        cu.setQueryName("在线数");
        cu.setThread(new String[]{"日期","PCU","ACU"});
        cu.setKey(new String[]{"logTime","pcu","acu"});
        cu.setQueryType(QueryType.ONLINE_NUM);
        setParameter(cu,"server","start","end","channel");*/

        AndFormMsg(request,login);
    }
//
//    @At("/macCount")
//    @Ok(THIS_URL)
//    public void macCount(HttpServletRequest request){
//        FormParameter mac=new FormParameter();
//        mac.setId(1);
//        mac.setQueryName("设备统计");
//        mac.setThread(new String[]{"渠道","日期","激活APP","设备绑定","注册人数"});
//        mac.setKey(new String[]{"platform","date","midNum","imiNum","aidNum"});
//        mac.setQueryType(QueryType.MAC);
//        mac.getParameter().put("start",true);
//        mac.getParameter().put("end",true);
//        AndFormMsg(request,mac);
//    }
//
//    @At("/payStatistics")
//    @Ok(THIS_URL)
//    public void payMsgCount(HttpServletRequest request){
//        FormParameter payMsg=new FormParameter();
//        payMsg.setId(1);
//        payMsg.setQueryName("充值详情");
//        payMsg.setThread(new String[]{"服务器","名字","发货状态","充值金额","订单号","物品","充值时间"});
//        payMsg.setKey(new String[]{"serverId","name","sendState","price","orderNumber","goodsId","logTime"});
//        payMsg.setQueryType(QueryType.PAY_MSG);
//        payMsg.getParameter().put("server",true);
//        payMsg.getParameter().put("serverAll",true);
//        payMsg.getParameter().put("start",true);
//        payMsg.getParameter().put("end",true);
//        payMsg.getParameter().put("pName",true);
//
//        FormParameter payLv=new FormParameter();
//        payLv.setId(2);
//        payLv.setQueryName("充值等级分布");
//        payLv.setThread(new String[]{"等级","首充人数","二次充值人数"});
//        payLv.setKey(new String[]{"lv","firstPay","secondPay"});
//        payLv.setQueryType(QueryType.PAY_LV);
//        payLv.getParameter().put("server",true);
//        payLv.getParameter().put("start",false);
//        payLv.getParameter().put("end",true);
//
//        AndFormMsg(request,payMsg,payLv);
//    }
//
    @At("/lvDistribution")
    @Ok(THIS_URL)
    public void distribution(HttpServletRequest request){
        FormParameter lv=new FormParameter();
        lv.setId(1);
        lv.setQueryName("等级分布");
        lv.setThread(new String[]{"等级","玩家人数"});
        lv.setKey(new String[]{"lv","num"});
        lv.setQueryType(QueryType.LV_DISTRIBUTION);
        setParameter(lv,"server","end","player");
        AndFormMsg(request,lv);

    }

    @At("/playerStepCount")
    @Ok(THIS_URL)
    public void gotoStep( HttpServletRequest request){
        FormParameter stepMsg=new FormParameter();
        stepMsg.setId(1);
        stepMsg.setQueryName("新手阶段统计");
        stepMsg.setThread(new String[]{"引导ID","引导名称","玩家人数","流失人数"});
        stepMsg.setKey(new String[]{"proc","dsp","num","lose"});
        stepMsg.setQueryType(QueryType.LOSE_STEP);
        setParameter(stepMsg,"server","end","player");
        AndFormMsg(request,stepMsg);
    }
    private void setParameter(FormParameter bean,String...key){
        for(String k:key){
            bean.getParameter().put(k,true);
        }
    }

    @At("/queryColumn")
    @Ok(THIS_URL)
    public void queryColumn(HttpServletRequest request){
        FormParameter zs=new FormParameter();
        zs.setId(1);
        zs.setQueryName("子嗣栏位");
        zs.setThread(new String[]{"日期","栏位号","人数"});
        zs.setKey(new String[]{"logTime","no","num"});
        zs.setQueryType(QueryType.LW_ZS);
        setParameter(zs,"server","start","end","channel");

        FormParameter jj=new FormParameter();
        jj.setId(1);
        jj.setQueryName("家教栏位");
        jj.setThread(new String[]{"日期","栏位号","人数"});
        jj.setKey(new String[]{"logTime","no","num"});
        jj.setQueryType(QueryType.LW_JJ);
        setParameter(jj,"server","start","end","channel");

        AndFormMsg(request,zs,jj);
    }

    @At("/lianYin")
    @Ok(THIS_URL)
    public void lianYinData(HttpServletRequest request){
        FormParameter ly=new FormParameter();
        ly.setId(1);
        ly.setQueryName("联姻");
        ly.setThread(new String[]{"日期","子嗣品级","联姻子嗣"});
        ly.setKey(new String[]{"logTime","sonLv","marriage"});
        ly.setQueryType(QueryType.CYD_LY);
        setParameter(ly,"server","start","end","channel");

        FormParameter zs=new FormParameter();
        zs.setId(2);
        zs.setQueryName("子嗣获得");
        zs.setThread(new String[]{"日期","子嗣获得"});
        zs.setKey(new String[]{"logTime","zxNum"});
        zs.setQueryType(QueryType.CYD_ZS);
        setParameter(zs,"server","start","end","channel");

        AndFormMsg(request,ly,zs);
    }

    @At("/involvement")
    @Ok(THIS_URL)
    public void involvement(HttpServletRequest request){

        FormParameter zlz=new FormParameter();
        zlz.setId(1);
        zlz.setQueryName("找乐子");
        zlz.setThread(new String[]{"日期","档位","开包间数","开包间人数","参加人数","积分涨幅","捣乱"});
        zlz.setKey(new String[]{"logTime","cid","room","num","actor","change","breach"});
        zlz.setQueryType(QueryType.CYD_ZLZ);
        setParameter(zlz,"server","start","end","channel");

        FormParameter xb=new FormParameter();
        xb.setId(2);
        xb.setQueryName("当学霸");
        xb.setThread(new String[]{"日期","创建队伍次数","创建队伍人数","参加人数"});
        xb.setKey(new String[]{"logTime","createTeam","createPlayer","actor"});
        xb.setQueryType(QueryType.CYD_XB);
        setParameter(xb,"server","start","end","channel");

        FormParameter jj=new FormParameter();
        jj.setId(3);
        jj.setQueryName("家教");
        jj.setThread(new String[]{"日期","员工ID","完成人数","参与人数"});
        jj.setKey(new String[]{"logTime","seat","openNum","actor"});
        jj.setQueryType(QueryType.CYD_JJ);
        setParameter(jj,"server","start","end","channel");

        FormParameter dsj=new FormParameter();
        dsj.setId(4);
        dsj.setQueryName("剁手节");
        dsj.setThread(new String[]{"日期","参与人数","最高层数"});
        dsj.setKey(new String[]{"logTime","actor","maxLevel"});
        dsj.setQueryType(QueryType.CYD_DS);
        setParameter(dsj,"server","start","end","channel");

        FormParameter qdp=new FormParameter();
        qdp.setId(5);
        qdp.setQueryName("抢地盘");
        qdp.setThread(new String[]{"日期","参与人数","最高层数"});
        qdp.setKey(new String[]{"logTime","actor","maxLevel"});
        qdp.setQueryType(QueryType.CYD_QDP);
        setParameter(qdp,"server","start","end","channel");

        FormParameter sx=new FormParameter();
        sx.setId(6);
        sx.setQueryName("寻访");
        sx.setThread(new String[]{"日期","参与人数","参与次数"});
        sx.setKey(new String[]{"logTime","num","fre"});
        sx.setQueryType(QueryType.CYD_XF);
        setParameter(sx,"server","start","end","channel");

        AndFormMsg(request,zlz,xb,jj,dsj,qdp,sx);
    }

    @At("/teaHome")
    @Ok(THIS_URL)
    public void teaHome(HttpServletRequest request){
        FormParameter fqi=new FormParameter();
        fqi.setId(1);
        fqi.setQueryName("茶楼发起");
        fqi.setThread(new String[]{"日期","类型","次数","人数"});
        fqi.setKey(new String[]{"logTime","type","fre","num"});
        fqi.setQueryType(QueryType.CL_FQ);
        setParameter(fqi,"server","start","end","channel");

        FormParameter wc=new FormParameter();
        wc.setId(2);
        wc.setQueryName("茶楼完成");
        wc.setThread(new String[]{"日期","类型","次数","人数"});
        wc.setKey(new String[]{"logTime","type","fre","num"});
        wc.setQueryType(QueryType.CL_WC);
        setParameter(wc,"server","start","end","channel");

        FormParameter goods=new FormParameter();
        goods.setId(3);
        goods.setQueryName("茶楼道具");
        goods.setThread(new String[]{"日期","CID","数量","人数"});
        goods.setKey(new String[]{"logTime","cid","fre","num"});
        goods.setQueryType(QueryType.CL_GOODS);
        setParameter(goods,"server","start","end","channel");

        AndFormMsg(request,fqi,wc,goods);
    }

}
