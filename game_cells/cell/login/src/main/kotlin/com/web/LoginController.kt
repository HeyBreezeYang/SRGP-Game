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
 * @ClassName base
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = ["/login"], method = [(RequestMethod.POST)])
class LoginController (@Qualifier("accountService") private val account: AccountServiceIF,
                       @Qualifier("loginService") private val login: LoginServiceIF){
    private val log = LoggerFactory.getLogger(LoginController::class.java)

    /**登录验证*/
    @RequestMapping(value = ["/verify"])
    fun loginVerify(verifyMsg: String): String {
        val msg = JSON.parseObject(URLTool.Dncode(verifyMsg), MutableMap::class.java)
        val result = mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg, SdkKey.APP_ID,SdkKey.OPEN_ID,SdkKey.ATTACH_SIGN)
            result[SdkKey.ATTACH]=login.verify(msg)
            result[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
        } catch (e: PlatformException) {
            result[SdkKey.RETURN_STATE] = e.code
            log.error(e.message)
        }
        return JSON.toJSONString(result)
    }

    @RequestMapping("/inspect")
    fun playerLogin(loginMsg: String): String {

        System.out.println("参数loginMsg：$loginMsg")
        val msg = JSON.parseObject(URLTool.Dncode(loginMsg), MutableMap::class.java)
        val result =  mapOf<String,Any>().toMutableMap()
        try {
            StringUtil.judgeMap(msg,SdkKey.ACCOUNT_TYPE,SdkKey.APP_ID,SdkKey.OPEN_ID)
            var msgService=mapOf<String,Any>().toMutableMap()
            msg.forEach({
                msgService[it.key.toString()]=it.value!!
            })
            msgService = login.loginMode(msgService)
            StringUtil.judgeMap(msg,SdkKey.ACCOUNT_NAME,SdkKey.ACCOUNT_ID,SdkKey.PASSWORD,SdkKey.PLATFORM)
           if(msg[SdkKey.ACCOUNT_TYPE]==6){
               account.loginRegister(msgService)
               if (msgService[SdkKey.ACCOUNT_ID]!=null && msgService[SdkKey.ACCOUNT_ID]!=""){
                   var msg1=mapOf<String,Any>().toMutableMap()
                   msg.forEach({
                       if (it.key == "accountId"){
                           msg1[it.key.toString()]=msgService[SdkKey.ACCOUNT_ID].toString()
                       }else{
                           msg1[it.key.toString()]=it.value!!
                       }
                   })
                   result[SdkKey.ATTACH]= login.login(msg1)
                   result[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
                   result[SdkKey.META]=msgService[SdkKey.META]!!
               }else{
                   result[SdkKey.RETURN_STATE]= PlatformCode.FAIL
               }
           }else if(msg[SdkKey.ACCOUNT_TYPE]==1){//1是手机登录
               result[SdkKey.ATTACH]= login.loginverificationCode(msgService)
               result[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
           }else {
               account.loginRegister(msgService)
               result[SdkKey.ATTACH]= login.login(msgService)
               result[SdkKey.RETURN_STATE]= PlatformCode.SUCCESS
           }

        } catch (e: PlatformException) {
            result[SdkKey.RETURN_STATE]= e.code
            result[SdkKey.RETURN_FLAG]= false
            log.error(e.message)
        }
        return JSON.toJSONString(result)
    }
}