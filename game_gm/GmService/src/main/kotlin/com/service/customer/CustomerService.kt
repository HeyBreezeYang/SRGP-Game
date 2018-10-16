package com.service.customer

import com.gmdesign.bean.other.GmHashMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Service

/**
 * Created by DJL on 2018/1/11.
 * @ClassName gm
 * @Description
 */
interface CustomerServiceIF {
    fun queryQuestion(sid:String,pid:String):GmHashMap
    fun saveQuestion(sid:String,pid:String,title:String,name:String,context:String):GmHashMap
}
@Service("customerService")
class CustomerService(@Autowired private val redisTemplate: RedisTemplate<String,String>):CustomerServiceIF{
    init {
        val stringSerializer=StringRedisSerializer()
        redisTemplate.keySerializer = stringSerializer
        redisTemplate.valueSerializer = stringSerializer
        redisTemplate.hashKeySerializer = stringSerializer
        redisTemplate.hashValueSerializer = stringSerializer
    }
    override fun saveQuestion(sid: String, pid: String, title: String, name: String, context: String): GmHashMap {
        val key ="userQuestion:$sid:$pid"
        val map = mapOf("name" to name).toMutableMap()
        redisTemplate.opsForHash<String,String>().putAll(key,map)
        val keyQ="$key:question"
        map.clear()
        map[System.currentTimeMillis().toString()]="$title&&&$context"
        redisTemplate.opsForHash<String,String>().putAll(keyQ,map)
        return queryQuestion(sid,pid)
    }
    override fun queryQuestion(sid: String, pid: String): GmHashMap {
        val result=GmHashMap()
        val questionKey= "userQuestion:$sid:$pid:question"
        val answerKey="userQuestion:$sid:$pid:answer"
        val question=redisTemplate.opsForHash<String,String>().entries(questionKey)
        val ks=question.keys
        var msg:List<String>
        for(keyQ in ks){
            val answer = redisTemplate.opsForHash<String,String>().entries(answerKey + keyQ)
            msg= question[keyQ]?.split("&&&")!!
            result.put(keyQ, mapOf("title" to msg[0],"text" to msg[1],"answer" to answer))
        }
        return result
    }
}