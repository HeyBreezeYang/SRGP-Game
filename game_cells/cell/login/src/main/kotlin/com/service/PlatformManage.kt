package com.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by DJL on 2018/3/6.
 * @Description
 */
interface PlatformManageServiceIF {
    fun setAppID(appID: String, openID: String)

    val ADD_APP_ID get() = "insert into app_id(id,appId,openId,privateKey) values(null,?,?,?);"
}

@Service("platformManageService")
 class PlatformManageService(@Autowired private val jdbcTemplate: JdbcTemplate): PlatformManageServiceIF{
    override fun setAppID(appID: String, openID: String) {
        val key = UUID.randomUUID().toString().replace("-".toRegex(), "")
        jdbcTemplate.update(ADD_APP_ID, appID, openID, key)
    }
}