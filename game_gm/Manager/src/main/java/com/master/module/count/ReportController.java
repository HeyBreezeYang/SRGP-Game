package com.master.module.count;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.gmdesign.exception.GmException;
import com.master.bean.web.QueryParameter;
import com.master.gm.service.count.ComprehensiveDataServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/7/21.
 *
 * @ClassName ReportController
 * @Description 综合数据查询
 */
@IocBean
@Ok("json:full")
@At(GmModule.REPORT_URL)
@Fail("http:500")
public class ReportController {

    @Inject
    private ComprehensiveDataServiceIF comprehensiveDataService;

    @At
    @Ok("jsp:jsp/other/query/report")
    public void gotoReport(){}

    @At("/data")
    @Ok("jsp:jsp/other/query/table/report_table")
    public void queryData(HttpServletRequest request, @Param("qid") int qid,@Param("..") QueryParameter parameter){
        System.out.println("接收到的参数qid："+qid+"===parameter:"+parameter);
        String title="";
        List<Object[]> head=new ArrayList<>();
        List<Object[]> msg=null;
        try {
            if(qid==1){
                title="综合数据";
                msg=queryComprehensiveData(head,parameter.getStart(),parameter.getEnd(),parameter.getServerId(),parameter.getOther()[0].toString());
            }else if(qid==2){
                title="留存率";
                msg=queryLRData(head,parameter.getStart(),parameter.getEnd(),parameter.getServerId(),parameter.getOther()[0].toString(),true);
            }else if(qid==4){
                title="LTV数据(比例)";
                msg=queryLTV(head,parameter.getStart(),parameter.getEnd(),parameter.getServerId(),parameter.getOther()[0].toString());
            }else if(qid==7){
                title="LTV数据(每日金额)";
                msg=queryLTVForMoney(head,parameter.getStart(),parameter.getEnd(),parameter.getServerId(),parameter.getOther()[0].toString());
            }
        } catch (GmException e) {
            e.printStackTrace();
        }
        request.setAttribute("tableName",title);
        request.setAttribute("tableHead",head);
        request.setAttribute("tableBody",msg);
    }

    private List<Object[]> queryLTVForMoney(List<Object[]> head,String start,String end,String serverId,String string) throws GmException {
        Pay30GZ(head);
        return comprehensiveDataService.LTVDataForMoney(serverId,start,end,string);
    }

    private List<Object[]> queryComprehensiveData(List<Object[]> head,String start,String end ,String sid,String platform) throws GmException {
        head.add(new Object[]{"服务器",0});
        head.add(new Object[]{"渠道",0});
        head.add(new Object[]{"日期",0});
        head.add(new Object[]{"新增角色",1,1});
        head.add(new Object[]{"总角色",1,1});
        head.add(new Object[]{"首次注册",1,1});
        head.add(new Object[]{"登录",1,2});
        head.add(new Object[]{"总充值",1,3});
        head.add(new Object[]{"日充",1,3});
        head.add(new Object[]{"首充",1,0});
        head.add(new Object[]{"总付费人数",1,4});
        head.add(new Object[]{"日充人数",1,4});
        head.add(new Object[]{"首充人数",1,0});
        head.add(new Object[]{"ARPPU",1,5});
        head.add(new Object[]{"总ARPPU",1,0});
        head.add(new Object[]{"ARPU",1,5});
        head.add(new Object[]{"付费渗透",1,5});
        head.add(new Object[]{"付费转化",1,5});
        head.add(new Object[]{"总转化",1,5});
        head.add(new Object[]{"在线转化",1,5});
        return comprehensiveDataService.Comprehensive(sid,start,end,platform);
    }
    private void Pay30GZ(List<Object[]> head){
        head.add(new Object[]{"服务器",0});
        head.add(new Object[]{"渠道",0});
        head.add(new Object[]{"日期",0});
        head.add(new Object[]{"新增",0});
        for(int i=1;i<=30;i++){
            if(i==2){
                head.add(new Object[]{"T"+i,1,3});
            }else if(i==1){
                head.add(new Object[]{"T"+i,1,2});
            }else if(i==3){
                head.add(new Object[]{"T"+i,1,4});
            }else if(i==7){
                head.add(new Object[]{"T"+i,1,5});
            }else if(i==15){
                head.add(new Object[]{"T"+i,1,6});
            }else if(i==30){
                head.add(new Object[]{"T"+i,1,7});
            }else{
                head.add(new Object[]{"T"+i,1,0});
            }
        }
    }

    private List<Object[]> queryLTV(List<Object[]> head,String start,String end ,String sid,String platform) throws GmException{
        Pay30GZ(head);
        return comprehensiveDataService.LTVData(sid,start,end,platform);
    }
    private List<Object[]> queryLRData(List<Object[]> head,String start,String end ,String sid,String platform,boolean lr) throws GmException {
        head.add(new Object[]{"服务器",0});
        head.add(new Object[]{"渠道",0});
        head.add(new Object[]{"日期",0});
        head.add(new Object[]{"新增",0});
        for(int i=2;i<=30;i++){
            if(i==2){
                head.add(new Object[]{"D"+i,1,2});
            }else if(i==3){
                head.add(new Object[]{"D"+i,1,3});
            }else if(i==7){
                head.add(new Object[]{"D"+i,1,4});
            }else if(i==15){
                head.add(new Object[]{"D"+i,1,5});
            }else if(i==21){
                head.add(new Object[]{"D"+i,1,6});
            }else if(i==30){
                head.add(new Object[]{"D"+i,1,7});
            }else{
                head.add(new Object[]{"D"+i,1,0});
            }
        }
        return comprehensiveDataService.LRRate(sid,start,end,platform,lr);
    }
}
