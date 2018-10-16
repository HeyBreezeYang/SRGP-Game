package com.web

import com.alibaba.fastjson.JSON
import com.design.context.PlatformCode
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.design.util.StringUtil
import com.service.DeliverOrderServiceIF
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/3/1.
 * @ClassName base
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/config", method = arrayOf(RequestMethod.POST))
class ConfigController(private @Qualifier("deliverOrderService") val deliverOrderService:DeliverOrderServiceIF){
    private val log = LoggerFactory.getLogger(ConfigController::class.java)

    @RequestMapping("/inspect")
    fun queryOrder(parameter: String): String {
        val msg = JSON.parseObject(parameter, MutableMap::class.java)
        val res = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.SERVER,SdkKey.OPEN_ID,SdkKey.APP_ID)
            val r = deliverOrderService.queryNotShipped(msg[SdkKey.APP_ID].toString()+ msg[SdkKey.OPEN_ID].toString(), msg[SdkKey.SERVER].toString())
            res[SdkKey.RETURN_STATE]=PlatformCode.SUCCESS
            res["res"]=JSON.toJSONString(r)
        } catch (e: PlatformException) {
            log.error(e.message)
            res[SdkKey.RETURN_STATE]=e.code
        }
        return JSON.toJSONString(res)
    }

    @RequestMapping("/fulfil")
    fun orderFulfil(orderMsg: String): String {
        val msg = JSON.parseObject(orderMsg, MutableMap::class.java)
        val res = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.OPEN_ID,SdkKey.APP_ID,SdkKey.ATTACH)
            val app = msg[SdkKey.APP_ID].toString()+ msg[SdkKey.OPEN_ID].toString()
            val fail = deliverOrderService.UpdateOrderSuccess(app, msg[SdkKey.ATTACH] as List<*>)
            if (fail.isEmpty()) {
                res[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
            } else {
                res[SdkKey.RETURN_STATE]= PlatformCode.PAYMENT_ERROR
                res[SdkKey.ATTACH]= fail
            }
        } catch (e: PlatformException) {
            log.error(e.message)
            res[SdkKey.RETURN_STATE]=e.code
        }
        return JSON.toJSONString(res)
    }

    @RequestMapping("/fail")
    fun orderFail(orderMsg: String): String {
        val msg = JSON.parseObject(orderMsg,MutableMap::class.java)
        val res = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.OPEN_ID,SdkKey.APP_ID,SdkKey.ATTACH)
            val app = msg[SdkKey.APP_ID].toString()+msg[SdkKey.OPEN_ID].toString()
            val fail = deliverOrderService.UpdateOrderFail(app, msg[SdkKey.ATTACH] as Map<*, *>)
            if (fail.isEmpty()) {
                res[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
            } else {
                res[SdkKey.RETURN_STATE]= PlatformCode.PAYMENT_ERROR
                res[SdkKey.ATTACH]= fail
            }
        } catch (e: PlatformException) {
            log.error(e.message)
            res[SdkKey.RETURN_STATE]=e.code
        }
        return JSON.toJSONString(res)
    }
}