package com.master.gm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJL on 2017/7/24.
 *
 * @ClassName GM
 * @Description
 */
public enum GmSql {
    QUERY_CPT(){
        @Override
        public String getSql() {
            return "select logTime,task,sum(passNum)passNum,sum(passFre)passFre from analysis_log.checkpoint where sid=@server and logTime>=@start and logTime<=@end group by logTime,task;";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("logTime");
            column.add("task");
            column.add("passNum");
            column.add("passFre");
            return column;
        }
        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("start");
            params.add("end");
            return params;
        }
    },
    QUERY_LEAGUE_RANK(){
        @Override
        public String getSql() {
            return "select leagueName,power,num from analysis_log.league_rank where sid=@server order by power desc;";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("leagueName");
            column.add("power");
            column.add("num");
            return column;
        }
        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            return params;
        }
    },
    QUERY_BOSS_NOON(){
        @Override
        public String getSql() {
            return "select logTime,nop,sum(num)num from analysis_log.boss_noon where sid=@server and logTime>=@start and logTime<=@end group by logTime,nop";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("logTime");
            column.add("nop");
            column.add("num");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("start");
            params.add("end");
            return params;
        }
    },
    QUERY_VIP_NUM(){
        @Override
        public String getSql() {
            return "select lv,count(pid) num from analysis_log.vip_rank where sid=@server and channel=@channel group by lv ;";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("lv");
            column.add("num");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("channel");
            return params;
        }
    },
    QUERY_VIP_NUM_ALL(){
        @Override
        public String getSql() {
            return "select sid,count(pid) num from analysis_log.vip_rank where lv>0 and channel=@channel group by sid ;";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("sid");
            column.add("num");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("channel");
            return params;
        }
    },
    QUERY_VIP_RANK(){
        @Override
        public String getSql() {
//            return "select `name`,vip,payment from analysis_log.player where sid=@server and platform=@channel ORDER BY vip DESC LIMIT 200;";
            return "select lv,pname,exp from analysis_log.vip_rank where sid=@server and channel=@channel ORDER BY lv DESC,exp DESC LIMIT 200;";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("pname");
            column.add("lv");
            column.add("exp");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("channel");
            return params;
        }
    },
    QUERY_BOSS_NIGHT(){
        @Override
        public String getSql() {
            return "select logTime,sum(fre) frequency,sum(num) num from analysis_log.boss_night where sid=@server and logTime>=@time1 and logTime<=@time2 group by logTime";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("logTime");
            column.add("frequency");
            column.add("num");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("time1");
            params.add("time2");
            return params;
        }
    },
    QUERY_MONETARY(){
        @Override
        public String getSql() {
            return "select logTime date,typeU,sum(numC) `change`,sum(numP) `num` from analysis_log.monetary_statistics where sid=@server and logTime>=@start and logTime<=@end and typeC=@type group by logTime,typeU;";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("date");
            column.add("useType");
            column.add("change");
            column.add("num");
            return column;
        }
        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("start");
            params.add("end");
            params.add("type");
            return params;
        }
    },
    QUERY_GOODS(){
        @Override
        public String getSql() {
            return "select logTime date,goods,sum(num) num,sum(fre) frequency,sum(payNum)payNum,shopType from analysis_log.shop where sid=@server and logTime>=@start and logTime<=@end group by logTime,goods,shopType";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("date");
            column.add("goods");
            column.add("num");
            column.add("frequency");
            column.add("payNum");
            column.add("shopType");
            return column;
        }
        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("server");
            params.add("start");
            params.add("end");
            return params;
        }
    },
    QUERY_7_LOSS(){
        @Override
        public String getSql() {
            return "SELECT M.logTime,N.createNum,M.tDay,(M.lossNum/N.createNum) loss  FROM (select A.logTime,SUM(lossNum) lossNum,tDay from  " +
                    "(select id,sid,platform,logTime from analysis_log.base_data where logTime>=@timeOne and logTime<=@timeTwo ) A " +
                    "RIGHT JOIN analysis_log.loss_rate ON A.id=baseId GROUP BY A.logTime,tDay ORDER BY A.logTime)M " +
                    "left JOIN (select logTime,SUM(createNum)createNum from analysis_log.base_data where logTime>=@timeOne and logTime<=@timeTwo GROUP BY logTime)N " +
                    "ON M.logTime=N.logTime";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("logTime");
            column.add("createNum");
            column.add("tDay");
            column.add("loss");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("timeOne");
            params.add("timeTwo");
            return params;
        }
    }
    ,
    GET_CU(){
        @Override
        public String getSql() {
            return "select logTime,MAX(maxNum)pcu,AVG(maxNum)acu from analysis_log.online_num where sid=@sid and logTime>=@start and logTime<=@end GROUP BY logTime;";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("logTime");
            column.add("pcu");
            column.add("acu");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("sid");
            params.add("start");
            params.add("end");
            return params;
        }
    },
    PLAYER_MSG(){
        @Override
        public String getSql() {
            return "select A.`name`,B.treasure,B.payment,B.money,B.fight,B.lv,B.lastPayTime,B.lastLoginTime from (select pid,`name` from gm_back.player " +
                    "where createTime>=@start and createTime<=@end and sid=@sid )A LEFT JOIN analysis_log.player_msg B ON A.pid=B.pid;";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("name");
            column.add("treasure");
            column.add("payment");
            column.add("money");
            column.add("fight");
            column.add("lv");
            column.add("lastPayTime");
            column.add("lastLoginTime");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("start");
            params.add("end");
            params.add("sid");
            return params;
        }
    },
    DUP_COUNT(){
        @Override
        public String getSql() {
            return "select dup,sum(firstPass) firstPass,sum(passNum) passNum from analysis_log.dup_count where sid=@sid and createTime>=@createA and createTime<=@createB and logTime>=@logA and logTime<=@logB group by dup ; ";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("dup");
            column.add("firstPass");
            column.add("passNum");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("sid");
            params.add("createA");
            params.add("createB");
            params.add("logA");
            params.add("logB");
            return params;
        }
    },
    GET_MAC_COUNT(){
        @Override
        public String getSql() {
            return "select X.midNum,X.imiNum,X.platform,X.date,Y.aidNum from  " +
                    " (select K.midNum,L.imiNum,L.platform,L.date from (select COUNT(B.mid)midNum,B.platform,B.date from " +
                    "(select mid,platform,FROM_UNIXTIME(logTime/1000,'%Y-%m-%d')date from analysis_log.mark where logTime>=@oneTimeA and logTime<=@twoTimeA )B GROUP BY B.platform,B.date)K " +
                    "RIGHT JOIN (select COUNT(DISTINCT A.mid)imiNum,A.platform,A.date from (select mid,platform,FROM_UNIXTIME(logTime/1000,'%Y-%m-%d')date from analysis_log.registration_mark " +
                    "where logTime>=@oneTimeB and logTime<=@twoTimeB )A GROUP BY A.platform,A.date)L ON K.date=L.date and K.platform=L.platform)X left join " +
                    "(select COUNT(W.actId)aidNum,W.platform,W.date " +
                    "from (select actId,platform,logTime,FROM_UNIXTIME(logTime/1000,'%Y-%m-%d')date from analysis_log.registration_mark GROUP BY actId)W " +
                    " where W.logTime>=@oneTimeC and W.logTime<=@twoTimeC GROUP BY W.platform,W.date)Y " +
                    "on X.platform=Y.platform AND X.date=Y.date;";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("midNum");
            column.add("imiNum");
            column.add("aidNum");
            column.add("platform");
            column.add("date");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("oneTimeA");
            params.add("twoTimeA");
            params.add("oneTimeB");
            params.add("twoTimeB");
            params.add("oneTimeC");
            params.add("twoTimeC");
            return params;
        }
    },
    GET_COMPREHENSIVE(){
        @Override
        public String getSql() {
            return "select A.logTime,A.player,A.newPlayer,A.register,A.login,A.pay,A.dayPay,A.payNum,A.dayPayNum,(dayPay/dayPayNum)'arppu',(pay/player)'arpu',(dayPayNum/login)'permeate',(newPayNum/newPlayer)'convert'" +
                    " from (select logTime,SUM(player)player,SUM(register)register,SUM(login)login,SUM(pay)pay,SUM(payNum)payNum,SUM(dayPay)dayPay,SUM(dayPayNum)dayPayNum,SUM(newPayNum)newPayNum,SUM(newPlayer)newPlayer " +
                    "from analysis_log.synthetical where logTime>=@oneTime and logTime<=@twoTime GROUP BY logTime)A ORDER BY A.logTime;";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("logTime");
            column.add("player");
            column.add("newPlayer");
            column.add("register");
            column.add("login");
            column.add("pay");
            column.add("dayPay");
            column.add("payNum");
            column.add("dayPayNum");
            column.add("arppu");
            column.add("arpu");
            column.add("permeate");
            column.add("convert");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("oneTime");
            params.add("twoTime");
            return params;
        }
    };
    public abstract String  getSql();
    public abstract List<String> getColumn();
    public abstract List<String> getParams();
}
