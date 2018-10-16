package com.web

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.cache.ServiceCache
import com.constant.SendType
import com.gmdesign.bean.other.GmHashMap
import com.gmdesign.exception.GmException
import com.gmdesign.util.URLTool
import com.service.code.CodeServiceIF
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * Created by DJL on 2018/1/11.
 * @ClassName PrizeCodeVerifyController
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = ["/code"],method = [(RequestMethod.POST)])
class PrizeCodeVerifyController(@Qualifier("codeService") private val codeService:CodeServiceIF) {

    @RequestMapping(value = ["/update"])
    fun updateCode(){
        synchronized(ServiceCache.ATTR) {
            ServiceCache.ATTR.clear()
        }
    }

    @RequestMapping(value = ["/verify"])
    fun verifyCode(msg:String):String{
        System.out.println("msg:$msg")
        val msg =JSON.parseObject(URLTool.Dncode(msg), MutableMap::class.java)
        val code = msg["code"]
        val sid = msg["sid"]
        val pid = msg["pid"]
        val platform = msg["platform"]
        var res= mapOf("code" to SendType.SUCCESS)
        try {
            val attr = codeService.queryAid(code as String)
            System.out.println("attr:$attr")
            synchronized(Objects.requireNonNull(attr)) {
                isBaseMessage(attr, code, sid as String, pid as String, platform as String)
                codeService.isUseVerify(code,pid)
            }
            codeService.sendMail(attr, sid as String, pid as String)

        } catch (e: GmException) {
            e.printStackTrace()
            res= mapOf("code" to e.code)
        }
        return JSON.toJSONString(res)
    }

    @Throws(GmException::class)
    private fun isBaseMessage(attr: GmHashMap, code: String, sid: String, pid: String, platform: String) {
        if (isScopeOfApplication(sid, attr["serverId"].toString()) && isScopeOfApplication(platform, attr["platform"].toString())) {
            if (isOutTime(attr["time"] as Long)) {
                isUse(attr, code, sid, pid)
            } else {
                throw GmException("PRIZE_CODE_OUT_TIME", SendType.PRIZE_CODE_OUT_TIME)
            }
        } else {
            throw GmException("PRIZE_CODE_UNAVAILABLE", SendType.PRIZE_CODE_UNAVAILABLE)
        }
    }


    private fun isScopeOfApplication(prams: String, config: String): Boolean {
        return config == "all" || Arrays.asList(*config.split(",".toRegex()).dropLastWhile { it.isEmpty()}.toTypedArray()).contains(prams)
    }

    private fun isOutTime(end: Long): Boolean {
        return end > System.currentTimeMillis()
    }

    @Throws(GmException::class)
    private fun isUse(attr: GmHashMap, code: String, sid: String, pid: String) {
        val type = attr["type"] as Int
        var flag = false
        when(type){
             1 -> flag =codeService.isUseForNormal(code)
            -1 -> flag =codeService.isUseForPlayer(code, pid)
            -2 -> flag =codeService.isUseForServer(code, sid)
        }
        if (flag) {
            throw GmException("PRIZE_CODE_USE", SendType.PRIZE_CODE_USE)
        } else {
            isAchiveId(attr, pid)
        }
    }

    @Throws(GmException::class)
    private fun judgeBatchId(aid: Int, pid: String) {
        val flag = codeService.isJudgeAchiveId(aid, pid)
        if (flag) {
            throw GmException("PRIZE_CODE_CANNT_USE_ACHIVE ", SendType.PRIZE_CODE_CANNT_USE_ACHIVE)
        }
    }

    @Throws(GmException::class)
    private fun judgeBindingBatchId(aid: Int, pid: String) {
        val flag = codeService.isBindingCode(aid, pid)
        if (!flag) {
            throw GmException("PRIZE_CODE_CANNT_USE_BINDING", SendType.PRIZE_CODE_CANNT_USE_BINDING)
        }
    }

    @Throws(GmException::class)
    private fun isAchiveId(attr: GmHashMap, pid: String) {
        val special = attr["specialType"] as Int
        when(special){
            1 -> judgeBatchId(attr["id"] as Int, pid)
            2 -> judgeBindingBatchId(attr["id"] as Int, pid)
        }
    }

}