package com

import com.gmdesign.util.DateUtil
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

/**
 * Created by DJL on 2018/1/10.
 * @ClassName gm
 * @Description
 */
@SpringBootApplication
@ComponentScan
class TransitApplication
fun main(args:Array<String>){
    SpringApplication.run(TransitApplication::class.java,*args)
}