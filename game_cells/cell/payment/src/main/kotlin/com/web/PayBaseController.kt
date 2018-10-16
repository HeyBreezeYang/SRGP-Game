package com.web

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.config.PaymentConfig
import com.design.context.PlatformCode
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.design.util.StringUtil
import com.design.util.URLTool
import com.service.PaymentServiceIF
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.annotation.Resource

/**
 * Created by DJL on 2018/3/5.
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/pay", method = arrayOf(RequestMethod.POST))
class PayBaseController (@Qualifier( "funPaymentService") private val funPay:PaymentServiceIF,
                         @Qualifier( "yhPaymentService") private val yingHunPay:PaymentServiceIF){
    private val log = LoggerFactory.getLogger(PayBaseController::class.java)

    private val changePayMethod = HashMap<Int, PaymentServiceIF>()
    init {

        changePayMethod[PaymentConfig.FUN_TYPE]=funPay
        changePayMethod[PaymentConfig.YING_HUN_TYPE]=yingHunPay

    }

    @RequestMapping(value = "/order")
    fun createOrder(userMsg:String):String{
        log.info("创建订单userMsg:$userMsg")

        val msg =JSON.parseObject(URLTool.Dncode(userMsg), MutableMap::class.java)
        val res = mapOf<String,Any>().toMutableMap()
        val base =JSON.parseObject(msg["baseMsg"].toString(), MutableMap::class.java)
        try {
            StringUtil.judgeMap(msg)

            val pay = changePayMethod[base[SdkKey.PAY_TYPE]]
            pay?.saveTrade(
                    JSON.parseObject(msg[SdkKey.BASE_MSG].toString(), MutableMap::class.java),
                    JSON.parseObject(msg[SdkKey.ATTACH].toString(), MutableMap::class.java))?.let {
                res.put(SdkKey.PENETRATION, it)

            }
            val callbackInfo = mapOf(
                    "sid" to base["sid"],
                    "goodsId" to base["goodsId"],
                    "uid" to base["uid"],
                    "pid" to base["roleId"]
            )
            res["callbackInfo"] =callbackInfo
            res[SdkKey.RETURN_STATE] = PlatformCode.SUCCESS

            System.out.println("callbackInfo的json值：$res")

            val callbackInfoUrlCode = URLTool.Encode(JSONObject.toJSONString(callbackInfo))

            when(base[SdkKey.PAY_TYPE]){
                PaymentConfig.FUN_TYPE -> res["callback"]="192.168.1.46:8012/funPay/payment"
                PaymentConfig.YING_HUN_TYPE -> res["callback"]="192.168.1.46:8012/yhPay/payment?callbackInfo1=$callbackInfoUrlCode"

            }

            System.out.println("rest的值：$res")
        } catch (e: PlatformException) {
            log.error(e.message)
            res[SdkKey.RETURN_STATE] = e.code
        }
        return URLTool.Encode(JSON.toJSONString(res))
    }

    @RequestMapping(value = "/finish")
    fun finishOrder(uuid:String,type:Int){
        val pay = changePayMethod[type]
        pay?.updateSuccessOrder(uuid)
    }
}