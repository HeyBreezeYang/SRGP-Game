package com.service

import com.alibaba.fastjson.JSON
import com.design.context.PlatformCode
import com.design.context.PlatformKey
import com.design.exception.PlatformException
import com.design.util.URLTool
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

/**
 * Created by DJL on 2018/3/1.
 * @Description 发货操作
 */
interface DeliverManagerServiceIF{
    @Throws(PlatformException::class)
    fun saveOrder(msg: MutableMap<*,*>)

    @Throws(PlatformException::class)
    fun sendGoods(msg: MutableMap<*,*>)

    val GOODS_PRICE get()= "select money from price_list where app=? and goodsId=? ;"
    val GET_ORDER get()= "select id,sendState from deliver_goods where orderNumber=? and orderUuid=? and app=? ;"
    val ADD_ORDER get()= "insert into deliver_goods (id,app,payType,sendState,orderNumber,orderUuid,goodsId,serverId,pid,price,channel,logTime) values(null,?,?,1,?,?,?,?,?,?,?,?); "
    val GET_URL get()= "select sendUrl from send_interface where app=? and sid=?;"
}
@Service
class DeliverManagerService(private @Autowired val jdbcTemplate: JdbcTemplate):DeliverManagerServiceIF{

    private fun priceMatch(app:String,goods:String ,money:Int):Boolean{
        val list = jdbcTemplate.queryForList(GOODS_PRICE, app, goods)
        if(list.isEmpty()){
            throw PlatformException("GOODS_NOT_EXISTENCE", PlatformCode.GOODS_NOT_EXISTENCE)
        }
        return money==list[0]["money"]
    }

    override fun saveOrder(msg: MutableMap<*, *>) {
        val order = msg[PlatformKey.ORDER]
        val app = msg[PlatformKey.APP].toString()
        val uuid = msg[PlatformKey.ORDER_UUID].toString()
        val payType = msg[PlatformKey.PAY_TYPE] as Int
        val goodsId = msg[PlatformKey.GOODS].toString()
        val money = msg[PlatformKey.MONEY] as Int
        if (!priceMatch(app, goodsId, money)) {
            throw PlatformException("price is error!~", PlatformCode.PRICE_ERROR)
        }
        val list = jdbcTemplate.queryForList(GET_ORDER, order, uuid, app)
        if (list != null &&!list.isEmpty()) {
            throw PlatformException("DELIVER_AUTOGRAPH!~", PlatformCode.DELIVER_AUTOGRAPH)
        }
        val i = jdbcTemplate.update(ADD_ORDER,app,payType,order,uuid,goodsId,msg[PlatformKey.SERVER],msg[PlatformKey.PID],money,msg[PlatformKey.CHANNEL],System.currentTimeMillis())
        if (i <= 0) {
            throw PlatformException("order save error~", PlatformCode.DATA_ADD_ERROR)
        }
    }

    override fun sendGoods(msg: MutableMap<*, *>) {
        val app = msg[PlatformKey.APP].toString()
        val sid = msg[PlatformKey.SERVER].toString()
        val list = jdbcTemplate.queryForList(GET_URL, String::class.java, app, sid)
        if (list == null || list.isEmpty()) {
            throw PlatformException("app is error!~", PlatformCode.PARAMETER_ERROR)
        }
        val res = mapOf("pid" to  msg[PlatformKey.PID],
                "orderNumber" to msg[PlatformKey.ORDER],
                "goodsId" to msg[PlatformKey.GOODS],
                "price" to msg[PlatformKey.MONEY]).toMutableMap()
        res["code"]=DigestUtils.md5Hex(res.toString()+PlatformKey.GAME_KEY)
        val full = list[0] + JSON.toJSONString(res)
        URLTool.sendMsg(full)
    }
}

