package com

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
class DeliverApplication
fun main(args:Array<String>){
    SpringApplication.run(DeliverApplication::class.java,*args)
}