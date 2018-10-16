package com.service.config

import com.gmdesign.bean.other.GmHashMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

/**
 * Created by DJL on 2018/1/11.
 * @ClassName gm
 * @Description
 */
interface ConfigServiceIF{
    fun queryGameList(): GmHashMap
    fun queryGameConfig(other:String):Map<String, Any>
    fun saveOne(code:String)
    fun saveTwo(code:String,aid:String)
    fun saveThree(code:String,pid:String)
    fun addNum()
    val listGame get() = "select serverID,serverName,state,recommend,openTime,resourceURL,other from gm_config.game_server order by logicID;"
    val rescUrl get() ="select id,resource from gm_config.resource_url;"
    val game get() = "select logicID,state,serverIP,serverPort,httpPort,extranetIP,deliverPort,openTime from gm_config.game_server where serverID=?;"
    val activate get() = "insert into analysis_log.mac (id) values (?);"
    val activateUpdate1 get() = "update analysis_log.mac set aid=? where id=?;"
    val activateUpdate2 get() = "update analysis_log.mac set pid=? where id=?;"

}
@Service("configService")
class ConfigService(@Autowired private val jdbcTemplate: JdbcTemplate,
                    @Autowired private val redisTemplate: RedisTemplate<String, String>):ConfigServiceIF {
    override fun addNum() {
       val num= redisTemplate.opsForValue().get("gameOpenNum")
       val n:Int=num.toInt()+1
        println("now:$n")
        redisTemplate.opsForValue().set("gameOpenNum",n.toString())
    }

    init {
        val stringSerializer= StringRedisSerializer()
        redisTemplate.keySerializer = stringSerializer
        redisTemplate.valueSerializer = stringSerializer
        redisTemplate.hashKeySerializer = stringSerializer
        redisTemplate.hashValueSerializer = stringSerializer
    }


    override fun saveTwo(code: String, aid: String) {
        jdbcTemplate.queryForList(activateUpdate1, code,aid)
    }

    override fun saveThree(code: String, pid: String) {
        jdbcTemplate.queryForList(activateUpdate2, code,pid)
    }

    override fun saveOne(code: String) {
        jdbcTemplate.queryForList(activate, code)
    }

    override fun queryGameConfig(other: String): Map<String, Any> {
        val res =jdbcTemplate.queryForList(game, other)
        return res[0]
    }
    override fun queryGameList(): GmHashMap {
        val res=GmHashMap()
        res.put("game",jdbcTemplate.queryForList(listGame))
        res.put("rs",jdbcTemplate.queryForList(rescUrl))
        return res
    }


}
