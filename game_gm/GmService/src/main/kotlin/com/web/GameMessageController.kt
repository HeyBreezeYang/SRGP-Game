package com.web

import com.alibaba.fastjson.JSON
import com.service.config.ConfigServiceIF
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/1/11.
 * @ClassName GameMessageController
 * @Description 游戏服和客户端获取服务器列表
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = ["/game"],method = [(RequestMethod.POST)])
class GameMessageController(@Qualifier("configService") private val configService:ConfigServiceIF){

    /**
     * 游戏服
     */
    @RequestMapping("/gameState")
    fun queryGameState(serverId:String):String {
        return JSON.toJSONString(configService.queryGameConfig(serverId))
    }
    /**
     * 客服端
     */
    @RequestMapping("/serverList")
    fun queryGameList():String {
        return JSON.toJSONString(configService.queryGameList())
    }
}


