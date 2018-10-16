package com.web

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.design.context.PlatformCode
import com.design.context.PlatformKey
import com.design.exception.PlatformException
import com.design.util.DESEncrypt
import com.design.util.StringUtil
import com.design.util.URLTool
import com.service.AccountServiceIF
import com.sun.xml.internal.bind.v2.model.core.ID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/3/6.
 * @ClassName base
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/certified")
class CertifiedController(@Qualifier("accountService") private val account: AccountServiceIF){
    private val log = LoggerFactory.getLogger(CertifiedController::class.java)

    @RequestMapping(value = "/getUid", method = arrayOf(RequestMethod.POST))
    fun queryUid(account: String): String {
        try {
            return this.account.getUid(account)
        } catch (e: PlatformException) {
            log.error(e.message)
            return e.code.toString()
        }
    }

    @RequestMapping(value = "/isAccount", method = arrayOf(RequestMethod.POST))
    fun queryAccount(id: String): String {
        try {
            account.isAccountId(id)
            return PlatformCode.SUCCESS.toString()
        } catch (e: PlatformException) {
            log.error(e.message)
            return e.code.toString()
        }
    }

    @RequestMapping(value = "/mail", method = arrayOf(RequestMethod.GET))
    fun playerMail(sign: String?): String {
        var res: String
        try {
            if (sign == null || sign.isEmpty()) {
                throw PlatformException("参数解析错误")
            }
            val mail = DESEncrypt.decoderByDES(PlatformKey.REGISTER_MAIL, sign)
            if (!StringUtil.isMail(mail)) {
                throw PlatformException("不是邮箱~~~")
            }
            account.accountCertified(mail)
            res = "认证成功"
        } catch (e: PlatformException) {
            res = e.message!!
            log.error(res)
        }
        return res
    }

    @RequestMapping(value = "/getAllIpWhiteList", method = arrayOf(RequestMethod.POST))
    fun getAllIpWhiteList():String{
        return JSONObject.toJSONString(account.getAllIpWhiteList())
    }

    @RequestMapping(value = "/addIpWhiteList", method = arrayOf(RequestMethod.POST))
    fun addIpWhiteList(msg:String): String? {
        System.out.println("msg:$msg")
        val msg = JSON.parseObject(msg, MutableMap::class.java)
        return JSONObject.toJSONString(account.addIpWhiteList(msg["ip"].toString(),msg["remark"].toString()))
    }

    @RequestMapping(value = "/delIpWhiteList", method = arrayOf(RequestMethod.POST))
    fun delIpWhiteList(id:String):String{
        System.out.println("id:$id")
        return JSONObject.toJSONString(account.delIpWhiteList(id))
    }

}