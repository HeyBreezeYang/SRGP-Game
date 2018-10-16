package com.web

import com.alibaba.fastjson.JSON
import com.design.context.PlatformCode
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.design.util.StringUtil
import com.design.util.URLTool
import com.service.AccountServiceIF
import com.service.LoginServiceIF
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/3/6.
 * @Description 注册 改密码
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/account", method = arrayOf(RequestMethod.POST))
class AccountController(@Qualifier("accountService") private val account: AccountServiceIF,
                        @Qualifier("loginService") private val login : LoginServiceIF){
    private val log = LoggerFactory.getLogger(AccountController::class.java)
    /**
     * 注册
     */
    @RequestMapping(value = "/register")
    fun register(playerMsg: String): String {
        val msg = JSON.parseObject(URLTool.Dncode(playerMsg), Map::class.java)
        val     result = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.ACCOUNT_ID,SdkKey.ACCOUNT_NAME,SdkKey.PASSWORD)
            val uid = account.register(msg)
            result[SdkKey.RETURN_STATE]=PlatformCode.SUCCESS
            result[SdkKey.RETURN_UID]= uid
        } catch (e: PlatformException) {
            e.printStackTrace()
            result[SdkKey.RETURN_STATE]=e.code
            log.error(e.message)
        }
        return JSON.toJSONString(result);
    }

    /**
     *
     */
    @RequestMapping(value = "/verification")
    fun accountVerification(playerMsg: String): String {
        val msg = JSON.parseObject(URLTool.Dncode(playerMsg), Map::class.java)
        val result = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.ACCOUNT_TYPE,SdkKey.ACCOUNT_NAME)
            this.account.accountVerification(msg)
            result[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
        } catch (e: PlatformException) {
            e.printStackTrace()
            result[SdkKey.RETURN_STATE]= e.code
            log.error(e.message)
        }
        return JSON.toJSONString(result)
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/resetPassword")
    fun accountResetPassword(playerMsg: String): String {
        val msg = JSON.parseObject(URLTool.Dncode(playerMsg), Map::class.java)
        val result = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.ACCOUNT_TYPE,SdkKey.ACCOUNT_ID,SdkKey.PASSWORD,SdkKey.PASSWORD_2,SdkKey.ATTACH_TOKEN)
            val msgService = mapOf<String,Any?>().toMutableMap()
            msg.forEach({
                msgService[it.key.toString()]=it.value
            })
            this.login.isLogin(msgService)
            this.account.restPassword(msgService)
            result[SdkKey.RETURN_STATE]=PlatformCode.SUCCESS
        } catch (e: PlatformException) {
            e.printStackTrace()
            result[SdkKey.RETURN_STATE]=e.code
            log.error(e.message)
        }
        return JSON.toJSONString(result)
    }

    /**
     *检索密码
     */
    @RequestMapping(value = "/retrievePassword")
    fun accountRetrievePassword(playerMsg: String): String {
        val msg = JSON.parseObject(URLTool.Dncode(playerMsg), Map::class.java)
        val result =  mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.ACCOUNT_TYPE,SdkKey.ACCOUNT_NAME,SdkKey.PASSWORD_2,SdkKey.ATTACH_SIGN,SdkKey.V_CODE)
            this.account.retrievePassword(msg)
            result[SdkKey.RETURN_STATE]=PlatformCode.SUCCESS
        } catch (e: PlatformException) {
            e.printStackTrace()
            result[SdkKey.RETURN_STATE]= e.code
            log.error(e.message)
        }
        return JSON.toJSONString(result)
    }
}