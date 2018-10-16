package com.web

import com.alibaba.fastjson.JSON
import com.design.context.PlatformCode
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.service.PlatformManageServiceIF
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/3/6.
 *
 * @ClassName base
 * @Description 认证接口
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = ["/operation"], method = [(RequestMethod.POST)])
class OperationController (@Qualifier("platformManageService") private val platformManage: PlatformManageServiceIF){
    private val log = LoggerFactory.getLogger(OperationController::class.java)

    @RequestMapping(value = ["/addApp"])
    fun setAppId(AID: String): String {
        val msg = JSON.parseObject(AID, Map::class.java)
        val result = mapOf<String,Any>().toMutableMap()
        try {
            if (!msg.containsKey("openID")) {
                throw PlatformException("参数格式错误:no openID", PlatformCode.PARAMETER_FORMAT_ERROR)
            }
            if (!msg.containsKey("appID")) {
                throw PlatformException("参数格式错误:no appID", PlatformCode.PARAMETER_FORMAT_ERROR)
            }
            platformManage.setAppID(msg["appID"].toString(), msg["openID"].toString())
            result.put(SdkKey.RETURN_STATE, PlatformCode.SUCCESS)
        } catch (e: PlatformException) {
            e.printStackTrace()
            result.put(SdkKey.RETURN_STATE, e.code)
            log.error(e.message)
        }

        return JSON.toJSONString(result)
    }
}
