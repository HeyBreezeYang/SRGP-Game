package com.service

import com.design.context.PlatformCode
import com.design.context.PlatformKey
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.design.util.StringUtil
import com.design.util.URLTool
import com.util.GameExchangeUtil
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Connection
import java.sql.PreparedStatement

/**
 * Created by DJL on 2018/3/5.
 * @ClassName base
 * @Description
 */
interface PaymentServiceIF {

    @Throws(PlatformException::class)
    fun saveTrade(base: MutableMap<*,*>, attach: MutableMap<*,*>): String

    @Throws(PlatformException::class)
    fun payVerification(msg:MutableMap<*,*>)

    fun updateSuccessOrder(uuid:String)

    @Throws(PlatformException::class)
    fun insideAccountPay(msg:String)

    fun queryGoodsByMoney(money:String,appid:String):MutableMap<*,*>

    val TWD get() = 1
    val CNY get() =  0
    val HDK get() =  2
    val USD get() =  3

    val PAY get() = 2
    val NO_PAY get() =  1
    val PAY_FALSE get()=3
    val DELIVER get()=4
    val DELIVER_SUCCESS get()=5
    val DELIVER_FALSE get()=6
    val USE get() =  3

    val ADD_BASIC get()= "insert INTO platform_pay.order_basic (id,uid,orderUuid,money,state,app,createTime,payType) VALUES (null,?,?,?,?,?,?,?);"
    val UPDATE_BASIC get()= "UPDATE platform_pay.order_basic SET state=? WHERE orderUuid=?;"
    val UPDATE_BASIC_2 get()= "UPDATE platform_pay.order_basic SET state=?,money=? WHERE id=?;"
    val GET_BASIC get()= "SELECT id FROM platform_pay.order_basic WHERE orderUuid=?;"
    val GET_STATE get()= "SELECT state,orderUuid FROM platform_pay.order_basic WHERE id=?;"

    @Throws(PlatformException::class)
    fun saveOrder(saveMsg: MutableMap<String, Any?>, template: JdbcTemplate): Int {
        val judge = accountVerification(saveMsg[SdkKey.ACCOUNT_ID].toString())
        if (judge != "0") {
            throw PlatformException("accountId is EXISTENCE", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        saveMsg[SdkKey.PAY_UUID]=StringUtil.createUUID()
        return saveBasicMsg(template, saveMsg)
    }

    @Throws(PlatformException::class)
    fun saveOrder(msg:  MutableMap<String,Any>, priceType: Int, payType: Int, template: JdbcTemplate): Int {
        val judge = accountVerification(msg[SdkKey.ACCOUNT_ID].toString())
        if (judge != "0") {
            throw PlatformException("accountId is EXISTENCE", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        val uuid = StringUtil.createUUID()
        val m = GameExchangeUtil.getPrice(msg.get(SdkKey.MONEY), priceType) ?: throw PlatformException("price is error ", PlatformCode.PRICE_ERROR)
        val appId=msg[SdkKey.APP_ID].toString()+msg[SdkKey.OPEN_ID]
        val data = mapOf("accountId" to msg[SdkKey.ACCOUNT_ID],"uuid" to uuid,"payType" to payType,"rmb" to m,"app" to appId)
        val id = saveBasicMsg(template, data)
        msg[SdkKey.PAY_UUID]= uuid
        msg[SdkKey.MONEY]=m
        return id
    }

    /**
     * 保存基本充值信息
     * @param template jdbc
     * *
     * @param data 订单信息
     * *
     * @return 主键
     */
    fun saveBasicMsg(template: JdbcTemplate, data: Map<String,Any?>): Int {
        val keyHolder = GeneratedKeyHolder()
        template.update({ con: Connection ->
            val ps = con.prepareStatement(ADD_BASIC, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setObject(1, data["accountId"])
            ps.setObject(2, data["uuid"])
            ps.setObject(3, data["rmb"])
            ps.setObject(4, PAY)
            ps.setObject(5, data["app"])
            ps.setObject(6, System.currentTimeMillis())
            ps.setObject(7, data["payType"])
            ps
        }, keyHolder)
        return keyHolder.key.toInt()
    }

    fun updateBasicMsg(template: JdbcTemplate, uuid: String, t: Int): Int {
        return template.update(UPDATE_BASIC, t, uuid)
    }

    fun updateBasicMsg(template: JdbcTemplate, id: Int, t: Int, money: Int): Int {
        return template.update(UPDATE_BASIC_2, t, money, id)
    }

    fun getIndex(template: JdbcTemplate, uuid: String): Int {
        return template.queryForObject(GET_BASIC, Int::class.javaPrimitiveType, uuid)
    }

    fun getState(template: JdbcTemplate, id: Int): Map<String, Any> {
        return template.queryForList(GET_STATE, id)[0]
    }

    @Throws(PlatformException::class)
    fun accountVerification(accountId: String): String {
        val p = URLTool.Encode(accountId)
        if (p != null) {
            return URLTool.sendMsg("http://192.168.1.46:9080/certified/isAccount?id=" + p)
        } else {
            return PlatformCode.ACCOUNT_NOT_EXISTENCE.toString()
        }
    }

    fun setDeliver(player: MutableMap<*,*>, order: String, payType: Int): MutableMap<String, Any?> {
        return mapOf(
                PlatformKey.PAY_TYPE to payType,
                PlatformKey.ORDER to order,
                PlatformKey.ORDER_UUID to player[SdkKey.PAY_UUID],
                PlatformKey.GOODS to player[SdkKey.GOODS],
                PlatformKey.SERVER to player[SdkKey.SERVER],
                PlatformKey.PID to player[SdkKey.PLAYER],
                PlatformKey.MONEY to player[SdkKey.MONEY],
                PlatformKey.CHANNEL to player[SdkKey.PLATFORM],
                PlatformKey.APP to player[SdkKey.APP_ID].toString()+player[SdkKey.OPEN_ID]
        ).toMutableMap()
    }

}