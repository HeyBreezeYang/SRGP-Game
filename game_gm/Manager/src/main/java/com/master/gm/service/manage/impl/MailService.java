package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.SendUtil;
import com.gmdesign.util.StringUtil;
import com.master.bean.dispoly.GameServer;
import com.master.bean.dispoly.GmMail;
import com.master.gm.BaseService;
import com.master.gm.service.manage.MailServiceIF;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.IocBean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/4.
 */
@IocBean
public class MailService extends BaseService implements MailServiceIF {
    @Override

    public  List<Record> getAllMail(String mailStatus,String startDate,String endDate) throws ParseException {
        Cnd cnd = null;
       if (StringUtil.paramJudge(mailStatus)){
           cnd = Cnd.where("1","=",1);
       }
        if (StringUtil.paramJudge(mailStatus)){
            if (mailStatus.equals("all")){

            }else {
                cnd.and("status","=",Integer.parseInt(mailStatus) );
            }
        }
        if (StringUtil.paramJudge(startDate)){
            cnd.and("createTime",">=",DateUtil.getStringToCurrentTimeMillis(startDate,"yyyy-MM-dd"));
        }
        if (StringUtil.paramJudge(endDate)){
            cnd.and("createTime","<=",DateUtil.getStringToCurrentTimeMillis(endDate,"yyyy-MM-dd"));
        }
        List<Record> mails = this.dao.query("gm_config.gm_mail_data",cnd);

        for (Record r: mails) {
            Map<String,Object> map = JSONObject.parseObject(r.get("email").toString(),Map.class);
            r.put("title",map.get("title"));
            int i = Integer.parseInt(r.get("status").toString());
            if (i==1){
                r.put("statusS", "未审核");
            }
            if (i==2){
                r.put("statusS","通过");
            }
            if (i==3){
                r.put("statusS","驳回");
            }
            r.put("date",DateUtil.getCurrentTimeMillisToString(Long.parseLong(r.get("createtime").toString()),"yyyyMMdd HH:mm:ss"));
        }
        return mails;
    }

    @Override
    public List<Record> getAllServer() {
        return this.dao.query("gm_config.game_server",null);
    }

    @Override
    public List<Record> getAllChannel() {
        return this.dao.query("gm_config.channel",null);
    }

    @Override
    public void saveOrUpdateMail(String mailMsg, GmMail mail,UserForService user,String operation) throws GmException{
        mail.setEmail(mailMsg);
        mail.setStatus(1);
        if ( mail.getId() <= 0 ){
            //新增
            mail.setCreateBy(user.getName());
            mail.setCreateTime(System.currentTimeMillis());
            this.dao.insert("gm_config.gm_mail_data",Chain.from(mail,FieldMatcher.create(true)));
        }else {
            //修改
            if (operation.equals("edit")){
                mail.setUpdateBy(user.getName());
                this.dao.update(mail);
            }
            //编辑并审核通过
            if (operation.equals("toExamine")){
                mail.setToexamineBy(user.getName());
                mail.setToexamineTime(System.currentTimeMillis());
                mail.setStatus(2);
                this.dao.update(mail);
                sendMail(mail);
            }

        }
    }

    @Override
    public List<Record> getMail(String mailId) {
        List<Record> mails =this.dao.query("gm_config.gm_mail_data",Cnd.where("id","=",mailId));
        for (Record r:mails ) {
            Map<String,Object> map = JSONObject.parseObject(r.get("email").toString(),Map.class);
            r.put("title",map.get("title"));
            r.put("context",map.get("context"));
            r.put("enclosure",map.get("funscJson"));
        }

        return mails;
    }

    @Override
    public String delMail(String mailId) throws GmException {
        GmMail mail = new GmMail();
        mail.setId(Integer.parseInt(mailId));
        int delete = this.dao.delete(mail);
        if (delete <= 0){
            throw new GmException("删除的数据不存在");
        }

        return "删除成功";
    }

    @Override
    public void toExamineMailStatus(Integer id, Integer type,UserForService user) throws GmException {
        List<GmMail> mails = this.dao.query(GmMail.class,Cnd.where("id","=",id));
        if (mails == null || mails.size()==0){
            throw new GmException("邮件不存在");
        }
        for (GmMail mail: mails) {
            mail.setToexamineBy(user.getName());
            mail.setToexamineTime(System.currentTimeMillis());
            mail.setStatus(type);
            int i = this.dao.update(mail);
            if (i == 0){
                throw new GmException("审核操作失败");
            }
            if (type == 2){
                sendMail(mail);
            }
        }
    }

    public void sendMail(GmMail mail) throws GmException {

        Map<String ,Object> email = JSONObject.parseObject(mail.getEmail(),Map.class);
        String mailStr = null;
        if (mail.getUsername().equals("all")){
            mailStr="mail?&playerId="+mail.getUsername()+"&title="+email.get("title")+"&context="+email.get("context")+"&funcsJson="+email.get("funscJson");
        }else {
            mailStr = "mail?&playerName="+mail.getUsername()+"&title="+email.get("title")+"&context="+email.get("context")+"&funcsJson="+email.get("funscJson");
        }
        List<GameServer> games = new ArrayList<>();
        if (mail.getSid().equals("all")){
            games = this.dao.query(GameServer.class,null);
       }else {
            games = this.dao.query(GameServer.class,Cnd.where("serverID","=",mail.getSid()));
        }
        int i = 0;
        for (GameServer gm:games) {
            String url = "http://" + gm.getServerIP()+ ":" +  games.get(i++).getHttpPort() + "/game/mail";
            SendUtil.sendHttpMsg(url,mailStr);
        }
    }
}
