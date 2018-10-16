package com.web.pay

import com.design.exception.PlatformException
import com.service.PaymentServiceIF
import com.util.VerificationOrder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/3/5.
 * @ClassName base
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/funPay", method = arrayOf(RequestMethod.POST))
class FunPayController(@Qualifier("funPaymentService") private val funPay: PaymentServiceIF){

    private val log = LoggerFactory.getLogger(FunPayController::class.java)

    @RequestMapping(value = "/payment")
    fun funPay(order: String, server_id: String, character_id: String, purchase_token: String): String {
        log.info("MSG: $order---$server_id---$character_id---$purchase_token")
        try {
            val msg = VerificationOrder.verificationForFun(order, purchase_token)
            if (msg["status"].toString() != "1") {
                throw PlatformException("status not 1")
            }
            if (msg["server_id"].toString()!=server_id) {
                throw PlatformException("server_id error")
            }
            if (msg["character_id"].toString()!=character_id) {
                throw PlatformException("character_id error")
            }
            funPay.payVerification(msg)
        } catch (e: PlatformException) {
            e.printStackTrace()
            return "fail"
        }
        return "success"
    }
}