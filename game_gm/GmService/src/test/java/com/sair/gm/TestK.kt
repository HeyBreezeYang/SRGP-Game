package com.sair.gm

/**
 * Created by DJL on 2018/1/16.
 * @ClassName gm
 * @Description
 */
class TestK{
   override fun toString():String{
        return "K"+super.toString()
    }
}
fun Any?.toStringIsNull():String{
    if(this==null)
        return "null"
    return toString()+"L"
}
fun main(args:Array<String>){
    val k:TestK=TestK()
    print(k.toStringIsNull())
}
