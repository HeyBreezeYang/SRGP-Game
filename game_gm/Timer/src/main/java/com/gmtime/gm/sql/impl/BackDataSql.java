package com.gmtime.gm.sql.impl;

import com.gmtime.gm.sql.SqlIF;

/**
 * Created by DJL on 2018/4/18.
 *
 * @ClassName gm
 * @Description
 */
public enum BackDataSql implements SqlIF {
    UPDATE_P_MSG_PAY(){
        @Override
        public String querySql() {
            return "update analysis_log.player set payment=?,lastPayTime=? where pid=? ";
        }
    },
    EXIST_P_MSG(){
        @Override
        public String querySql() {
            return "select id,payment,lastPayTime from analysis_log.player where pid=?;";
        }
    },
    SAVE_NOON_BOSS(){
        @Override
        public String querySql() {
            return "insert into analysis_log.boss_noon (id,sid,logTime,nop,num) values (null,?,?,?,?)";
        }
    },
    SAVE_NIGHT_BOSS(){
        @Override
        public String querySql() {
            return "insert into analysis_log.boss_night (id,sid,logTime,channel,fre,num) values (null,?,?,?,?,?)";
        }
    },
    SAVE_SHOP(){
        @Override
        public String querySql() {
            return "insert into analysis_log.shop (id,sid,channel,logTime,goods,shopType,num,fre,payNum) values (null,?,?,?,?,?,?,?,?)";
        }
    },
    SAVE_CPT(){
        @Override
        public String querySql() {
            return "insert into analysis_log.check (id,sid,logTime,channel,task,passNum,passFre) values (null,?,?,?,?,?,?)";
        }
    },
    SAVE_MONETARY(){
        @Override
        public String querySql() {
            return "insert into analysis_log.monetary_statistics (id,sid,channel,logTime,typeC,typeU,numC,numP) values (null,?,?,?,?,?,?,?)";
        }
    },
    SID_PLATFORM_UID(){
        @Override
        public String querySql() {
            return "select B.sid,B.platform,B.uid from (select A.sid,A.platform,A.uid,A.createTime from (select sid,platform,uid,createTime from analysis_log.player ORDER BY createTime) A GROUP BY A.uid)B where B.createTime>=? AND B.createTime<=?;";
        }
    },
    GET_PAY(){
        @Override
        public String querySql() {
            return "select sid,channel,COUNT(DISTINCT pid)num,SUM(price)price from analysis_log.pay where logTime<=? group by sid,channel";
        }
    },
    GET_PAY_EVERYONE(){
        @Override
        public String querySql() {
            return "select sid,channel,pid,SUM(price)price from analysis_log.pay where logTime>=? and logTime<=? group by pid ";
        }
    },
    QUERY_OTHER_COMPREHENSIVE(){
        @Override
        public String querySql() {
            return "select " +
                    "(select count(pid) from analysis_log.player where platform=? and sid=? and createTime<=?)player," +
                    "(select count(pid) from analysis_log.login where platform=? and loginTime=? and sid=? )login";
        }
    },
    GET_CREATE(){
        @Override
        public String querySql() {
            return "select pid,DATE_FORMAT(createTime,'%Y-%m-%d')date from analysis_log.player where createTime>=? and createTime<=? and sid=? and platform=?;";
        }
    },
    SAVE_COMPREHENSIVE(){
        @Override
        public String querySql() {
            return "insert into analysis_log.synthetical(id,sid,platform,logTime,newPlayer,player,register,login,pay,dayPay,newPay,payNum,dayPayNum,newPayNum) values (null,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        }
    },
    SAVE_BASE(){
        @Override
        public String querySql() {
            return "insert into analysis_log.base_data(id,sid,platform,logTime,newPlayer) values (null,?,?,?,?);";
        }
    },
    GET_BASE_ID(){
        @Override
        public String querySql() {
            return "select id form analysis_log.base_data where sid=? and platform=? and logTime=? ";
        }
    },
    SAVE_LOSS(){
        @Override
        public String querySql() {
            return "insert into analysis_log.loss_rate (id,baseId,tDay,lossNum) values (null,?,?,?);";
        }
    },
    GET_LOGIN(){
        @Override
        public String querySql() {
            return "select pid from analysis_log.login where loginTime=? and sid=? and platform=?;";
        }
    },
    ADD_OL(){
        @Override
        public String querySql() {
            return "insert into analysis_log.online_num (id,logTime,sid,hour,maxNum,minNum) values (null,?,?,?,?,?);";
        }
    },
    Add_PLAYER(){
        @Override
        public String querySql() {
            return "insert into analysis_log.player (pid,name,uid,createTime,platform,sid) values (?,?,?,?,?,?)";
        }
    },
    UPDATE_P_MSG(){
        @Override
        public String querySql() {
            return "update analysis_log.player set treasure=?,lv=?,vip=?,money=?,fight=?,lastLoginTime=? where pid=?;";
        }
    }
}
