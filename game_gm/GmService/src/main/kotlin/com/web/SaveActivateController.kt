package com.web

import com.service.config.ConfigServiceIF
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * Created by DJL on 2018/4/28.
 * @ClassName SaveActivateController
 * @Description 登录前打点记录
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = ["/log"],method = [(RequestMethod.POST)])
class SaveActivateController(@Qualifier("configService") private val configService: ConfigServiceIF){

    @RequestMapping(value = ["/create"])
    fun createCode():String{
        configService.addNum()
        return UUID.randomUUID().toString()
    }

    @RequestMapping(value = ["/activate"])
    fun activate(code:String){
        configService.saveOne(code)
    }

    @RequestMapping(value = ["/account"])
    fun account(code:String,aid:String){
        configService.saveTwo(code,aid)
    }

    @RequestMapping(value = ["/role"])
    fun role(code:String,pid:String){
        configService.saveTwo(code,pid)
    }

}