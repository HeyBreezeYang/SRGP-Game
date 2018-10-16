package com.master.gm.service.manage.impl;

import java.util.*;

import com.gmdesign.exception.GmException;
import com.master.gm.bean.UserQuestion;
import com.master.gm.BaseService;
import com.master.gm.service.manage.CustomerServiceIF;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/7/27.
 *
 * @ClassName CustomerService
 * @Description
 */
@SuppressWarnings("unchecked")
@IocBean
public class CustomerService extends BaseService implements CustomerServiceIF{

    @Inject
    private RedisService redisService;

    @Override
    public UserQuestion getUserQuestion(String sid, String pid, long time) throws GmException {
        String timeS=String.valueOf(time);
        UserQuestion question=new UserQuestion();
        question.setTime(time);
        question.setPid(pid);
        question.setSid(sid);
        String keyMsg="userQuestion:".concat(sid.concat(":")).concat(pid);
        Map<String,String> msg=this.redisService.hgetAll(keyMsg);
        question.setName(msg.get("name"));
        Map<String,String> quesMsg=this.redisService.hgetAll(keyMsg.concat(":question"));
        String[] ques=quesMsg.get(timeS).split("&&&");
        question.setTitle(ques[0]);
        question.setContext(ques[1]);
        Map<String,String> type=this.redisService.hgetAll(keyMsg.concat(":type"));
        if(type!=null&&!type.isEmpty()){
            String t=type.get(timeS);
            if(t!=null){
                question.setType(Integer.parseInt(t));
                Map<String,String> answer=this.redisService.hgetAll(keyMsg.concat(":answer:").concat(timeS));
                for(String k:answer.keySet()){
                    question.setAnswers(Long.parseLong(k),answer.get(k));
                }
            }
        }
        return question;
    }

    @Override
    public List<UserQuestion> queryAllQuestion() throws GmException {
        List<UserQuestion> questions =new ArrayList<>();
        Set<String> allKey=this.redisService.keys("userQuestion:*:question");
        for(String key:allKey){
            String[] k=key.split(":");
            Map<String,String> quesMap=this.redisService.hgetAll(key);
            for(String qKey:quesMap.keySet()){
                UserQuestion question=new UserQuestion();
                question.setSid(k[1]);
                question.setPid(k[2]);
                Map<String,String> a=this.redisService.hgetAll(k[0]+":"+k[1]+":"+k[2]);
                question.setName(a.get("name"));
                question.setTime(Long.parseLong(qKey));
                String msg=quesMap.get(qKey);
                if(msg.length()<=3||msg.indexOf("&&&")<1){
                    continue;
                }
                question.setTitle(msg.split("&&&")[0]);
                Map<String,String> tKey=this.redisService.hgetAll(k[0]+":"+k[1]+":"+k[2]+":type");
                if(tKey!=null&&!tKey.isEmpty()){
                    String t=tKey.get(qKey);
                    if(t!=null){
                        question.setType(Integer.parseInt(t));
                    }
                }
                questions.add(question);
            }
        }
        return questions;
    }

    @Override
    public void saveAnswer(String sid, String pid, long time, int type, String context) throws GmException {
        String timeS=String.valueOf(time);
        String keyMsg="userQuestion:".concat(sid.concat(":")).concat(pid);
        if(type!=0){
            Map<String,String> t=new HashMap<>();
            t.put(timeS,String.valueOf(type));
            this.redisService.hmset(keyMsg.concat(":type"),t);
        }
        Map<String,String> a=new HashMap<>();
        a.put(String.valueOf(System.currentTimeMillis()),context);
        this.redisService.hmset(keyMsg.concat(":answer:").concat(timeS),a);
    }
}
