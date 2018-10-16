package com.util

import com.alibaba.fastjson.JSON
import com.config.PaymentConfig
import com.design.context.PlatformCode
import com.design.exception.PlatformException
import com.design.util.URLTool
import org.slf4j.LoggerFactory

/**
 * Created by DJL on 2018/3/5.
 * @ClassName base
 * @Description
 */
object VerificationOrder {
    private val log = LoggerFactory.getLogger(VerificationOrder::class.java)

    private val FUN_GAME_TYPE = "195"
    @Throws(PlatformException::class)
    fun verificationForFun(order: String, token: String): MutableMap<*,*> {
        val prams = "game=$FUN_GAME_TYPE&order=$order&purchase_token=$token"
        val res = JSON.parseObject(URLTool.sendMsg(PaymentConfig.FUN_PAY_URL, prams), MutableMap::class.java)
        log.info("fun order :" + res)
        if (res.containsKey("error_code")) {
            throw PlatformException("msg is error:" + res["error_code"], PlatformCode.PARAMETER_ERROR)
        }
        return res
    }
}