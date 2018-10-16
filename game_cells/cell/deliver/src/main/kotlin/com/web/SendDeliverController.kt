package com.web

import com.alibaba.fastjson.JSON
import com.design.context.PlatformCode
import com.design.context.PlatformKey
import com.design.exception.PlatformException
import com.design.util.DESEncrypt
import com.design.util.URLTool
import com.service.DeliverManagerServiceIF
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by DJL on 2018/3/1.
 * @ClassName base
 * @Description 充值发货
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/send", method = arrayOf(RequestMethod.POST))
class SendDeliverController(private @Qualifier("deliverManagerService") val deliverManagerService:DeliverManagerServiceIF){
    private val lockOrder = ConcurrentHashMap<String, MutableMap<*,*>>()

    @RequestMapping("/deliver")
    fun send(deliverMsg: String): String {
        var r = "0"
        try {
            val json = DESEncrypt.decoderByDES(PlatformKey.DELIVERY_KEY, deliverMsg)
            val msg = JSON.parseObject(json, MutableMap::class.java)
            if (lockOrder[msg[PlatformKey.ORDER_UUID]] != null) {
                throw PlatformException("正在对该订单发货~", PlatformCode.DELIVER_ING)
            }
            lockOrder[msg[PlatformKey.ORDER_UUID].toString()]= msg
            deliverManagerService.saveOrder(msg)
            deliverManagerService.sendGoods(msg)
            lockOrder.remove(msg[PlatformKey.ORDER_UUID].toString())
            val uuid = msg[PlatformKey.ORDER_UUID]
            val payType = msg[PlatformKey.PAY_TYPE]
            URLTool.sendMsg("https://pay.cellsgame.com/pay/finish","uuid=$uuid&type=$payType")
        } catch (e: PlatformException) {
            e.printStackTrace()
            r = e.code.toString()
        }
        return r
    }
}