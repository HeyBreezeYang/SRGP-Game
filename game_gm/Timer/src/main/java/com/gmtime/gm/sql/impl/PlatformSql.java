package com.gmtime.gm.sql.impl;

import com.gmdesign.context.GmConfiger;
import com.gmtime.gm.sql.SqlIF;

/**
 * Created by DJL on 2017/7/24.
 *
 * @ClassName GM
 * @Description
 */
public enum PlatformSql implements SqlIF{

    GET_LOGIN_FIRST(){
        @Override
        public String querySql() {
            return "select platform,accountId from platform_login.first_login where appId='"+ GmConfiger.APP+"' and logTime>=? and logTime<=? ;";
        }
    },
    GET_PAY_GOODS(){
        @Override
        public String querySql() {
            return "select serverId,channel,logTime,pid,price,goodsId from platform_deliver.deliver_goods where app='"+GmConfiger.APP+"' and logTime>=? and logTime<=? ;";
        }
    },
    GET_PAY_PLAYER_COUNT(){
        @Override
        public String querySql() {
            return "select A.pid,sum(A.price)price,A.logTime from (select pid,price,logTime from platform_deliver.deliver_goods where app='"+GmConfiger.APP+"' order by logTime desc)A group by A.pid; ";
        }
    },
    GET_PAY_PLAYER_MSG(){
        @Override
        public String querySql() {
            return "select pid,price,logTime from platform_deliver.deliver_goods where app='"+GmConfiger.APP+"' and logTime>? order by logTime desc; ";
        }
    },
    QUERY_PAY_DATA_1(){
        @Override
        public String querySql() {
            return "select pid,sendState,orderNumber,goodsId,serverId,price,FROM_UNIXTIME(logTime/1000,'%Y-%m-%d %h:%i:%s')logTime from platform_deliver.deliver_goods where app='"+GmConfiger.APP+"' and serverId=? and logTime>=? and logTime<=?;";
        }
    },
    QUERY_PAY_DATA_2(){
        @Override
        public String querySql() {
            return "select  pid,sendState,orderNumber,goodsId,serverId,price,FROM_UNIXTIME(logTime/1000,'%Y-%m-%d %h:%i:%s')logTime from platform_deliver.deliver_goods where app='"+GmConfiger.APP+"' and  logTime>=? and logTime<=?;";
        }
    },
    QUERY_PAY_DATA_3(){
        @Override
        public String querySql() {
            return "select sendState,orderNumber,goodsId,serverId,price,FROM_UNIXTIME(logTime/1000,'%Y-%m-%d %h:%i:%s')logTime from platform_deliver.deliver_goods where app='"+GmConfiger.APP+"' and pid=? and  logTime>=? and logTime<=?;";
        }
    }

}
