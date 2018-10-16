package com.service

import com.design.context.PlatformCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by DJL on 2018/3/1.
 * @Description 订单管理
 */
interface DeliverOrderServiceIF {
     fun queryNotShipped(app: String, sid: String): List<Map<String, Any>>
     fun UpdateOrderSuccess(app: String, orders: List<*>): List<String>
     fun UpdateOrderFail(app: String, orders: Map<*, *>): List<String>

    val NOT_SHIPPED_ORDER get() = "select orderNumber,goodsId,pid,price from deliver_goods where app=? and serverId=? and sendState=1;"

    val SEND_STATE get() = "update deliver_goods set sendState=? where app=? and orderNumber=?;"
}
@Service("deliverOrderService")
class DeliverOrderService(private @Autowired val jdbcTemplate: JdbcTemplate):DeliverOrderServiceIF{
    override fun queryNotShipped(app: String, sid: String): List<Map<String, Any>> {
        return jdbcTemplate.queryForList(NOT_SHIPPED_ORDER, app, sid)
    }

    override fun UpdateOrderSuccess(app: String, orders: List<*>): List<String> {
        val define= ArrayList<String>()
        if(!orders.isEmpty()){
            orders.forEach {
                val k=jdbcTemplate.update(SEND_STATE, PlatformCode.SUCCESS, app, it)
                if (k!=1){
                    define.add(it.toString())
                }
            }
        }
        return define
    }

    override fun UpdateOrderFail(app: String, orders: Map<*, *>): List<String> {
        val define= ArrayList<String>()
        if (!orders.isEmpty()) {
            orders.forEach{
                val k=jdbcTemplate.update(SEND_STATE, it.value, app, it.key)
                if(k!=1){
                    define.add(it.key.toString())
                }
            }
        }
        return define
    }

}


