package com.master.gm.bean;

import java.util.ArrayList;
import java.util.List;

import com.gmdesign.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by DJL on 2017/3/28.
 *
 * @ClassName gm
 * @Description
 */
@Getter
@Setter
@ToString
public class UserQuestion {

    private String sid;
    private String name;
    private String pid;
    private String title;
    private String context;
    private String timeName;
    private long time;
    private int type=0;
    private List<Answer> answers=new ArrayList<>();

    public String getTypeName() {
        if(this.type==0){
            return "未定义";
        }else if(this.type==1){
            return "充值";
        }else if(this.type==2){
            return "游戏BUG";
        }else if(this.type==3){
            return "体验";
        }else if(this.type==4){
            return "建议";
        }else if(this.type==5){
            return "普通咨询";
        }else if(this.type==6){
            return "无效问题";
        }else if(this.type==7){
            return "其他";
        }else if(this.type==8){
            return "活动BUG";
        }else if(this.type==9){
            return "闪退";
        }else if(this.type==10){
            return "卡死";
        }else if(this.type==11){
            return "无法充值";
        }else if(this.type==12){
            return "充值未到账";
        }else if(this.type==13){
            return "活动咨询";
        }else if(this.type==14){
            return "功能BUG";
        }else{
            return "未知";
        }
    }

    public void setTime(long time) {
        this.timeName= DateUtil.formatDateTime(time);
        this.time = time;
    }

    public String getStatues(){
        return type==0?"未回复":"已回复";
    }

    public void setAnswers(long time,String context) {
        Answer a=new Answer();
        a.setTime(time);
        a.setContext(context);
        this.answers.add(a);
    }

    @Getter
    @Setter
    @ToString
     public class Answer{
        private String time;
        private String context;
        public void setTime(long time) {
            this.time = DateUtil.formatDateTime(time);
        }
    }
}
