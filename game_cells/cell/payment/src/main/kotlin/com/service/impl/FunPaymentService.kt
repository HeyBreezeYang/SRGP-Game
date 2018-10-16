package com.service.impl

import com.config.PaymentConfig
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.service.PaymentServiceIF
import com.util.DeliveryEncryption
import com.util.GameExchangeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by DJL on 2018/3/5.
 * @ClassName base
 * @Description
 */
@Service("funPaymentService")
class FunPaymentService (@Autowired private val jdbcTemplate: JdbcTemplate):PaymentServiceIF{

    private val SAVE_ORDER get() = "insert into details_fun(id,basicId,orderId,money,currency,orderMsg,createTime) values (null,?,?,?,?,?,?);"

    private val ORDER_PARAM = ConcurrentHashMap<String, MutableMap<String,Any?>>()

    override fun saveTrade(base: MutableMap<*,*>, attach: MutableMap<*, *>): String {
        val data = mapOf("accountId" to base[SdkKey.ACCOUNT_ID],
                "rmb" to 0,
                "app" to base[SdkKey.APP_ID].toString()+base[SdkKey.OPEN_ID],
                "payType" to PaymentConfig.FUN_TYPE).toMutableMap()
        val id = saveOrder(data, jdbcTemplate)
        val uuid = data[SdkKey.PAY_UUID].toString()
        val b= mapOf<String,Any?>().toMutableMap()
        base.forEach {
            b[it.key.toString()] = it.value
        }
        base.clear()
        b["dbId"]=id
        b[SdkKey.PAY_UUID]=uuid
        ORDER_PARAM[uuid]=b
        return uuid
    }

    private fun funPaymentCurrency(cy: String): Int {
        when (cy) {
            "USD" -> return USD
            else -> return CNY
        }
    }

    override fun payVerification(msg: MutableMap<*, *>) {
        val player = ORDER_PARAM[msg["comments"].toString()] ?: throw PlatformException("order not create")
        val money = GameExchangeUtil.getPrice(msg["currency_amount"], funPaymentCurrency(msg["currency"].toString()))
        val dbId = player["dbId"] as Int
        updateBasicMsg(jdbcTemplate, dbId, NO_PAY, money)
        player.put(SdkKey.MONEY, money)
        val res = setDeliver(player, msg["orderid"].toString(), PaymentConfig.FUN_TYPE)
        jdbcTemplate.update(SAVE_ORDER,dbId,msg["orderid"],money,msg["currency"],msg.toString(),msg["createtime"])
        val r = DeliveryEncryption.activeNotification(DeliveryEncryption.result(res))
        if (r != "0") {
            throw PlatformException("游戏服处理异常")
        }
    }

    override fun updateSuccessOrder(uuid: String) {
        updateBasicMsg(jdbcTemplate, uuid, USE)
        ORDER_PARAM.remove(uuid)
    }

    override fun insideAccountPay(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queryGoodsByMoney(money: String, appid: String): MutableMap<*, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}