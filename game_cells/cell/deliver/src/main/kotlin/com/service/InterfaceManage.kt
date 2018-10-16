package com.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

/**
 * Created by DJL on 2018/3/1.
 * @Description 游戏发货接口管理
 */
interface InterfaceManagerServiceIF{
    fun addInterface(map: Map<*,*>)
    fun delInterface(app: String, sid: String)

    val ADD_INTERFACE get() = "insert into send_interface (id,app,sid,sendUrl) values (null,?,?,?);"
    val DEL_INTERFACE get() = "delete from send_interface where app=? and sid=?;"
}
@Service("interfaceManagerService")
class InterfaceManagerService(private @Autowired val jdbcTemplate: JdbcTemplate):InterfaceManagerServiceIF{
    override fun addInterface(map: Map<*,*>) {
        jdbcTemplate.update(ADD_INTERFACE, map["app"], map["sid"], map["sendUrl"])
    }

    override fun delInterface(app: String, sid: String) {
        jdbcTemplate.update(DEL_INTERFACE, app, sid)
    }
}