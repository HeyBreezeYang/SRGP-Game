package com.web;

import com.alibaba.fastjson.JSON
import com.design.util.URLTool
import com.service.InterfaceManagerServiceIF
import com.service.QueryOrderServiceIF
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/3/1.
 *
 * @ClassName OperateController
 * @Description
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/operate", method = arrayOf(RequestMethod.POST))
class OperateController(private @Qualifier("queryOrderService") val queryOrderService:QueryOrderServiceIF,
                        private @Qualifier("interfaceManagerService") val interfaceManagerService: InterfaceManagerServiceIF) {

    @RequestMapping("/firstPay")
    fun queryFirstPay(param:String):String{
        val msg= JSON.parseObject(param, MutableMap::class.java)
        return queryOrderService.queryFirstPay(msg)
    }

    @RequestMapping("/payMsg")
    fun queryPayMsg(app: String, time: Long): String {
        return JSON.toJSONString(queryOrderService.queryPayMsgEveryServer(app, time))
    }

    @RequestMapping("/addInterface")
    fun addInterface(prams: String): String {
        val map = JSON.parseObject(URLTool.Dncode(prams), MutableMap::class.java)
        interfaceManagerService.addInterface(map)
        return "0"
    }

    @RequestMapping("/delInterface")
    fun delInterface(app: String, sid: String): String {
        interfaceManagerService.delInterface(app, sid)
        return "0"
    }

}
