package com.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

/**
 * Created by DJL on 2018/3/1.
 * @Description  查询业务
 */
interface QueryOrderServiceIF{
    fun queryFirstPay(msg:MutableMap<*,*>):String
    fun queryPayMsgEveryServer(app:String,time:Long):List<Map<String,Any>>

    val FIRST_TIME get() = "select logTime from deliver_goods where app=? and serverId=? and pid=? order by logTime limit 1;"
    val PAY_MSG get() = "select serverId,COUNT(DISTINCT pid)num,SUM(price)money from deliver_goods where sendState=0 and app=? and logTime>=? GROUP BY serverId;"
}
@Service("queryOrderService")
class QueryOrderService(private @Autowired val jdbcTemplate: JdbcTemplate):QueryOrderServiceIF{
    override fun queryPayMsgEveryServer(app: String, time: Long): List<Map<String, Any>> {
       return jdbcTemplate.queryForList(PAY_MSG, app, time)
    }

    override fun queryFirstPay(msg: MutableMap<*,*>): String {
        val list = jdbcTemplate.queryForList(FIRST_TIME, msg["app"], msg["sid"], msg["pid"])
        if(list==null||list.isEmpty()){
            return "0"
        }else{
            return list[0]["logTime"].toString()
        }
    }

}