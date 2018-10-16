package com.service

import com.alibaba.fastjson.JSON
import com.config.LoginConfig
import com.design.context.PlatformCode
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.design.util.DESEncrypt
import com.design.util.DateUtil
import com.design.util.StringUtil
import com.design.util.URLTool
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by DJL on 2018/3/6.
 * @ClassName base
 * @Description
 */
interface LoginServiceIF {
    @Throws(PlatformException::class)
    fun loginMode(map: MutableMap<String,Any>): MutableMap<String,Any>

    @Throws(PlatformException::class)
    fun login(msg: Map<*, *>): Map<String, String>

    @Throws(PlatformException::class)
    fun verify(map: Map<*, *>): Map<String,Any?>

    @Throws(PlatformException::class)
    fun isLogin(msg: MutableMap<String, Any?>)

    @Throws(PlatformException::class)
    fun loginverificationCode(msg: Map<*, *>): Map<String, String>
}
val GET_ACCOUNT_ID get() = "select uid from account_data where id=?;"
val GET_FREEZE get() = "select id from account_freeze where account=?;"
val GET_KEY get() = "select privateKey from app_id where appId=? and openId=?;"
val GET_FIRST get() = "select id,logTime,platform from first_login where accountId=? and appId=? "
val ADD_FIRST get() = "insert into first_login (id,accountId,logTime,appId,platform) values (null,?,?,?,?);"
val IS_ADMIN get() = "select id from account_admin where adminAccount=?;"
val ADD_LOG get() = "insert into login_log (id,uid,loginTime,app) values (null,?,?,?);"
val ADD_DAY_LOG get() = "insert into login_day_log (id,uid,loginTime,appId) values (null,?,?,?);"
val GET_DAY_LOG get()="select id,uid,loginTime,appId from login_day_log where uid=? and loginTime>=? and appId=?"

@Service("loginService")
class LoginService(@Autowired private val jdbcTemplate: JdbcTemplate,
                   @Autowired private val redisTemplate: StringRedisTemplate):LoginServiceIF{
    private val tokenLong = AtomicLong(System.currentTimeMillis() * 1000L)

    @Throws(PlatformException::class)
    private fun FunLogin(token: String): String {
        val res = URLTool.sendMsg("http://mainaland.auth.raink.com.cn/verify?access_token=$token", null, "GET")
        //        String res=URLTool.sendMsg("http://auth-beta.funcell123.com/verify?access_token=".concat(token),null,"GET");
        val r = JSON.parseObject(res, MutableMap::class.java)
        if (r.containsKey("error_code")) {
            throw PlatformException("error_code " + r["error_code"], PlatformCode.ACCOUNT_TOKEN_ERROR)
        }
        return r["account"].toString()
    }
    override fun loginMode(map: MutableMap<String, Any>): MutableMap<String, Any> {
        val type = map[SdkKey.ACCOUNT_TYPE] as Int
        when (type) {
            LoginConfig.LOGIN_FUN123 -> {
                val attach = JSON.parseObject(map[SdkKey.ATTACH].toString(), MutableMap::class.java)
                val token = attach["token"].toString()
                val aid = map[SdkKey.ACCOUNT_ID]
                if (FunLogin(token) != aid) {
                    throw PlatformException("accountId error``", PlatformCode.PARAMETER_ERROR)
                }
                map.put(SdkKey.ACCOUNT_NAME, aid)
                map.put(SdkKey.PASSWORD, "-")
            }
            /*LoginConfig.LOGIN_AI_YINGHUN -> {
                val attach = JSON.parseObject(map[SdkKey.ATTACH].toString(), MutableMap::class.java).toMutableMap()

                val gameId = attach["gameId"]
                val channelId = attach["channelId"]
                val appId = attach["appId"]
                val sid = attach["sid"]
                val userId = attach["userId"]
                val appSecret = LoginConfig.APP_SECRET

                val sign = DigestUtils.md5Hex("""appId=${appId}channelId=${channelId}gameId=${gameId}sid=${sid}userId=${userId}${appSecret}""")
                val attachObj = "gameId=$gameId&channelId=$channelId&appId=$appId&userId=$userId&sid=$sid&sign=$sign"
                val urlRes = URLTool.sendMsg("http://token.aiyinghun.com/user/token",attachObj)
                val urlMap = JSON.parseObject(urlRes, MutableMap::class.java).toMutableMap()
                if (urlMap["ret"]==0){
                    map[SdkKey.META]=urlRes
                    val urlMapContent = JSON.parseObject(urlMap["content"].toString(), MutableMap::class.java).toMutableMap()
                    val urlMapData = JSON.parseObject(urlMapContent["data"].toString(), MutableMap::class.java).toMutableMap()
                    map[SdkKey.ACCOUNT_ID] =urlMapData["userId"].toString()
                    val userId = urlMapData["userId"]
                    map[SdkKey.ACCOUNT_NAME] =urlMapData["userId"].toString()
                    map[SdkKey.PASSWORD] = "-"
                }else{
                    map[SdkKey.META]=urlRes
                }

            }*/
        }

        map.remove(SdkKey.ATTACH)
        return map
    }

    @Throws(PlatformException::class)
    private fun passwordAuthentication(name: String, pwd: String, type: Int): String {
        val list = this.jdbcTemplate.queryForList(GET_ACCOUNT_FOR_NAME, name, type)
        if (list == null || list.isEmpty()) {
            throw PlatformException("account not existence!~", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        val password = StringUtil.changePassword(pwd)
        if (password != list[0]["password"].toString()) {
            throw PlatformException("password is error!~", PlatformCode.ACCOUNT_PASSWORD_ERROR)
        }
        return jdbcTemplate.queryForObject(GET_ACCOUNT_ID, String::class.java, Integer.parseInt(list[0]["aId"].toString()))
    }



    override fun login(msg: Map<*, *>): Map<String, String> {
        var uid = msg[SdkKey.ACCOUNT_ID].toString()
        if (StringUtil.isEmpty(uid)) {
            val name = msg[SdkKey.ACCOUNT_NAME].toString()
            val pwd = msg[SdkKey.PASSWORD].toString()
            if (StringUtil.isEmpty(name) || StringUtil.isEmpty(pwd)) {
                throw PlatformException("no password or name~", PlatformCode.ACCOUNT_NOT_NAME_OR_PASSWORD)
            } else {
                uid = passwordAuthentication(name, pwd, msg[SdkKey.ACCOUNT_TYPE] as Int)
            }
        } else {
               val list = jdbcTemplate.queryForList(GET_AID, uid)
            if (list.size != 1) {
                throw PlatformException("accountData not existence!~", PlatformCode.ACCOUNT_NOT_EXISTENCE)
            }
        }
        val index = jdbcTemplate.queryForList(GET_FREEZE, uid)
        if (index != null && !index.isEmpty()) {
            throw PlatformException("account is freeze!~", PlatformCode.ACCOUNT_FREEZE)
        }
        val loginDayLog = jdbcTemplate.queryForList(GET_DAY_LOG,uid,DateUtil.getToDayMorning(),msg[SdkKey.APP_ID])
        if (loginDayLog == null || loginDayLog.size==0){
            jdbcTemplate.update(ADD_DAY_LOG,uid,System.currentTimeMillis(),msg[SdkKey.APP_ID])
        }
        return returnLoginMsg(uid, msg[SdkKey.APP_ID].toString(), msg[SdkKey.OPEN_ID].toString(), msg[SdkKey.PLATFORM].toString())
    }


    override fun loginverificationCode(msg: Map<*, *>): Map<String, String> {
        var uid = msg[SdkKey.ACCOUNT_ID].toString()
        if(uid!=null&&!"".equals(uid)){
            val list = jdbcTemplate.queryForList(GET_AID, uid)
            if (list.size != 1) {
                //账号不存在-----注册登录
                var accountId = uid;
                if (accountId.length != 11) {
                    throw PlatformException("phonenumber is error!~", 3000)
                }
                val time = System.currentTimeMillis()
                this.jdbcTemplate.update(ADD_ACCOUNT, accountId, time)

            }else{//账号存在
                val index = jdbcTemplate.queryForList(GET_FREEZE, uid)
                if (index != null && !index.isEmpty()) {
                    throw PlatformException("account is freeze!~", PlatformCode.ACCOUNT_FREEZE)
                }
                val loginDayLog = jdbcTemplate.queryForList(GET_DAY_LOG,uid,DateUtil.getToDayMorning(),msg[SdkKey.APP_ID])
                if (loginDayLog == null || loginDayLog.size==0){
                    jdbcTemplate.update(ADD_DAY_LOG,uid,System.currentTimeMillis(),msg[SdkKey.APP_ID])
                }
            }
        }else{
            throw PlatformException("request data accountData not existence!", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        return returnLoginMsg(uid, msg[SdkKey.APP_ID].toString(), msg[SdkKey.OPEN_ID].toString(), msg[SdkKey.PLATFORM].toString())
    }


    private fun newToken(uid: String): String {
        var token = redisTemplate.opsForValue().get(uid)
        if (!StringUtil.isEmpty(token)) {
            this.redisTemplate.delete(uid)
        }
        token = java.lang.Long.toString(tokenLong.incrementAndGet(), 32)
        redisTemplate.opsForValue().set(uid, token, 2, TimeUnit.DAYS)
        return token
    }

    @Throws(PlatformException::class)
    private fun getPrivateKey(appId: String, openId: String): String {
        if (!StringUtil.isNotEmptyForSimultaneously(appId, openId)) {
            throw PlatformException("no appId or openId~", PlatformCode.ACCOUNT_NOT_APP_ID)
        }
        return this.jdbcTemplate.queryForObject(GET_KEY, String::class.java, appId, openId)
    }
    @Throws(PlatformException::class)
    private fun returnLoginMsg(uid: String, appID: String, OpenID: String, platform: String): Map<String, String> {
        val token = newToken(uid)
        val text = "$token&$uid"
        val key = getPrivateKey(appID, OpenID)
        val sign = DESEncrypt.encoderByDES(key, text)
        val res = mapOf(SdkKey.ATTACH_TOKEN to token,SdkKey.ATTACH_SIGN to sign,SdkKey.RETURN_UID to uid)
        saveFirstLogin(uid, platform, appID + OpenID)
        return res
    }

    @Throws(PlatformException::class)
    private fun saveFirstLogin(accountId: String, platform: String, appId: String) {
        val list = this.jdbcTemplate.queryForList(GET_FIRST, accountId, appId)
        if (list == null || list.isEmpty()) {
            this.jdbcTemplate.update(ADD_FIRST, accountId, System.currentTimeMillis(), appId, platform)
        }
    }

    override fun verify(map: Map<*, *>): Map<String, Any?> {
        val key = getPrivateKey(map[SdkKey.APP_ID].toString(), map[SdkKey.OPEN_ID].toString())
        println("key:"+key);
        println("sign=====:"+map[SdkKey.ATTACH_SIGN].toString());
        val data = DESEncrypt.decoderByDES(key, map[SdkKey.ATTACH_SIGN].toString())
        println("data=====:"+data);
        val signData = data.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (signData.size != 2)
            throw PlatformException("sign error!~", PlatformCode.ACCOUNT_SIGN_ERROR)
        val accountId = signData[1]
        //val accountId = map[SdkKey.ACCOUNT_ID].toString()
        val l = jdbcTemplate.queryForList(GET_AID, accountId)
        if (l.size != 1) {
            throw PlatformException("account not existence!~", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        val token = signData[0]
        val useToken = getToken(accountId)
        val flag = token == useToken
        if (flag) {
        //if(true){
            saveLoginLog(accountId, map[SdkKey.APP_ID].toString() + map[SdkKey.OPEN_ID].toString())
        } else {
            throw PlatformException("token error !~", PlatformCode.ACCOUNT_TOKEN_ERROR)
        }
        val list = this.jdbcTemplate.queryForList(IS_ADMIN, accountId)
        var admin = 0
        if (list != null && !list.isEmpty()) {
            admin = 1
        }
        //return mapOf<String,Any>(SdkKey.ATTACH_TOKEN to "",SdkKey.ACCOUNT_ID to accountId,SdkKey.ADMIN to admin)

        return mapOf<String,Any>(SdkKey.ATTACH_TOKEN to token,SdkKey.ACCOUNT_ID to accountId,SdkKey.ADMIN to admin)
    }

    @Throws(PlatformException::class)
    private fun saveLoginLog(accountId: String, appId: String) {
        val a = this.jdbcTemplate.update(ADD_LOG, accountId, System.currentTimeMillis(), appId)
        if (a != 1) {
            throw PlatformException("data add error~!", PlatformCode.DATA_ADD_ERROR)
        }
    }

    private fun getToken(uid: String): String {
        return redisTemplate.opsForValue().get(uid)
    }

    override fun isLogin(msg: MutableMap<String, Any?>) {
        val uid = msg[SdkKey.ACCOUNT_ID].toString()
       // val token = getToken(uid)
        //if (token != msg[SdkKey.ATTACH_TOKEN]) {
          //  throw PlatformException("token error !~", PlatformCode.ACCOUNT_TOKEN_ERROR)
       // }
        val index = jdbcTemplate.queryForObject(GET_AID, Int::class.java, uid)
        val list = this.jdbcTemplate.queryForList(GET_OTHER_FOR_ID, index)
        msg[SdkKey.ACCOUNT_NAME]= list[0]["account"]
    }
}
