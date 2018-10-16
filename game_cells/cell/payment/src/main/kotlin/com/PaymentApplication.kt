package com

import com.alibaba.fastjson.JSONObject
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

/**
 * Created by DJL on 2018/3/1.
 * @ClassName base
 * @Description
 */
@SpringBootApplication
@ComponentScan
class PaymentApplication
fun main(args:Array<String>){
    SpringApplication.run(PaymentApplication::class.java,*args)
}