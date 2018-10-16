package com.web

import com.service.customer.CustomerServiceIF
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by DJL on 2018/1/11.
 * @ClassName CustomerController
 * @Description 客服后台回复功能
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = ["/customer"],method = [(RequestMethod.POST)])
class CustomerController(@Qualifier("customerService") private val customerService: CustomerServiceIF){
    @RequestMapping(value = ["/queryQuestion"])
    fun resultQuestionForPlayer():String{
        return ""
    }

    @RequestMapping(value = ["/putQuestion"])
    fun putQuestion():String{
        return ""
    }

}