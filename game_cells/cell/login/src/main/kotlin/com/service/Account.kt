package com.service

import com.alibaba.fastjson.JSONObject
import com.config.LoginConfig
import com.design.context.PlatformCode
import com.design.context.PlatformKey
import com.design.context.SdkKey
import com.design.exception.PlatformException
import com.design.util.DESEncrypt
import com.design.util.ResultUtil
import com.design.util.StringUtil
import com.design.util.URLTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by DJL on 2018/3/6.
 * @Description
 */
interface AccountServiceIF {

    @Throws(PlatformException::class)
    fun getUid(account: String): String

    @Throws(PlatformException::class)
    fun loginRegister(map: MutableMap<String,Any>)

    @Throws(PlatformException::class)
    fun isAccountId(id: String)

    @Throws(PlatformException::class)
    fun register(msg: Map<*, *>): String

    @Throws(PlatformException::class)
    fun restPassword(msg: Map<*, *>)

    @Throws(PlatformException::class)
    fun retrievePassword(msg: Map<*, *>)

    @Throws(PlatformException::class)
    fun accountCertified(account: String)

    @Throws(PlatformException::class)
    fun accountVerification(msg: Map<*, *>): MutableList<MutableMap<String, Any>>?

    fun getAllIpWhiteList():MutableList<MutableMap<String, Any>>?

    fun addIpWhiteList(ip: String, remark: String): MutableMap<String, Any>?

    fun delIpWhiteList(id: String): MutableMap<String, Any>?

}
val GET_UID get() = "select uid from account_data where id=(select aId from account_other where account=?); "
val GET_ACCOUNT_FOR_NAME get() = "select aId,password from account_other where account=? and type=?;"
val GET_AID get() = "select id from account_data where uid=?;"
val GET_CERTIFIED get() =  "select id from not_certified where account=?;"
val DEL_CERTIFIED get() = "delete from not_certified where account=?;"
val RESET_PASSWORD get() = "update account_other set password=?,restTime=? where account=?;"
val GET_OTHER_FOR_ID get() = "select * from account_other where aId=?;"
val ADD_ACCOUNT_OTHER get() = "insert into account_other(id,aId,type,account,password,bindingTime) values (null,?,?,?,?,?);"
val ADD_NOT_CERTIFIED get() = "insert into not_certified(id,account) values (null,?);"
val ADD_ACCOUNT get() = "insert into account_data(id,uid,createTime,accountType) values (null,?,?,'0');"
val ADD_ACCOUNT_PH get() = "insert into account_data(id,uid,createTime,accountType) values (null,?,?,'');"
val GET_IP_WHITE_LIST get() = "select id,ip,remark from ip_white_list"
val ADD_IP_WHITE_LIST get() = "insert into ip_white_list(ip,remark) values (?,?)"
val DEL_IP_WHITE_LIST get() = "delete from ip_white_list where id=?"

@Service("accountService")
class AccountService(@Autowired private val jdbcTemplate: JdbcTemplate):AccountServiceIF{
    override fun delIpWhiteList(id: String): MutableMap<String, Any> {
        System.out.println("ip:$id")
        val result = mapOf<String,Any>().toMutableMap()
        try {
            val num = jdbcTemplate.update(DEL_IP_WHITE_LIST,id)

            if (num ==1){
                val ipWhiteLists = jdbcTemplate.queryForList(GET_IP_WHITE_LIST)

                result["data"] = ipWhiteLists
            }
            result["code"] = num
            return result
        }catch (e:PlatformException){
            e.printStackTrace()
            result["code"] = 0
            result["data"] = ""
            return result
        }
    }

    override fun addIpWhiteList(ip: String, remark: String): MutableMap<String, Any> {
        System.out.println("ip:$ip"+"remark:$remark")
        val result = mapOf<String,Any>().toMutableMap()
        try {
            val num = jdbcTemplate.update(ADD_IP_WHITE_LIST,ip,remark)
            if (num ==1){
                val ipWhiteLists = jdbcTemplate.queryForList(GET_IP_WHITE_LIST)
                result["data"] = ipWhiteLists
            }
            result["code"] = num
            return result
        }catch (e:PlatformException){
            e.printStackTrace()
            result["code"] = 0
            result["data"] = ""
            return result
        }
    }

    override fun getAllIpWhiteList(): MutableList<MutableMap<String, Any>>? {
        return this.jdbcTemplate.queryForList(GET_IP_WHITE_LIST)
    }

    override fun getUid(account: String): String {
        val t = this.jdbcTemplate.queryForList(GET_UID, account)
        if (t == null || t.isEmpty()) {
            throw PlatformException("ACCOUNT_NOT_EXISTENCE : $account", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        return t[0]["uid"].toString()
    }

    fun accountExistenceForFun(msg: MutableMap<String, Any>){
        val aid = msg[SdkKey.ACCOUNT_ID].toString()+"FUN"
        val list = jdbcTemplate.queryForList(GET_AID, aid)
        if (list.size!=1) {
            msg["otherUid"]= aid
            msg[SdkKey.ACCOUNT_ID]=""
            msg[SdkKey.ACCOUNT_ID]= register(msg)
        } else {
            msg.put(SdkKey.ACCOUNT_ID, aid)
        }
    }

    fun accountExistenceForYingHun(msg: MutableMap<String, Any>){
        val aid = msg[SdkKey.ACCOUNT_ID].toString()
        val aType = msg[SdkKey.ACCOUNT_TYPE].toString()
        /*val list1 = jdbcTemplate.queryForList(GET_ACCOUNT_FOR_NAME, aid,aType)
        System.out.println("list1:$list1")*/
        val list = jdbcTemplate.queryForList(GET_UID, aid)
        if (list.size!=1) {
            msg["otherUid"]= aid
            msg[SdkKey.ACCOUNT_ID]=""
            msg[SdkKey.ACCOUNT_ID]= register(msg)
        } else {
            msg[SdkKey.ACCOUNT_ID] = list[0]["uid"].toString()
        }
    }

    fun defaultDoing(msg: MutableMap<String, Any>){
        if (StringUtil.isEmptyForSimultaneously(
                msg[SdkKey.ACCOUNT_ID].toString(),
                msg[SdkKey.ACCOUNT_NAME].toString(),
                msg[SdkKey.PASSWORD].toString())) {
            msg.put(SdkKey.ACCOUNT_ID, register(msg))
        }
    }
    //手机账号登录
    fun dephfaultDoing(msg: MutableMap<String, Any>){
        if (msg[SdkKey.ACCOUNT_ID].toString()!=null&&!"".equals(msg[SdkKey.ACCOUNT_ID].toString())) {
            msg.put(SdkKey.ACCOUNT_ID, register(msg))
        }
    }

    override fun loginRegister(map: MutableMap<String, Any>) {
       val type = map[SdkKey.ACCOUNT_TYPE] as Int
        when (type){
            LoginConfig.LOGIN_FUN123 -> {
                accountExistenceForFun(map)
            }
            LoginConfig.LOGIN_AI_YINGHUN ->{
                accountExistenceForYingHun(map)
            }
            LoginConfig.LOGIN_PHONE ->{
                dephfaultDoing(map)
            }
            else -> defaultDoing(map)

        }
    }

    override fun isAccountId(id: String) {
        val list = jdbcTemplate.queryForList(GET_AID, id)
        if (list.size!=1) {
            throw PlatformException("account not existence!~", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
    }

    private fun createAccountId(type: Int, msg: Map<*, *>): String {
        if (type == LoginConfig.LOGIN_FUN123) {
            return msg["otherUid"].toString()
        }/*else if (type == LoginConfig.LOGIN_AI_YINGHUN){
            return msg["otherUid"].toString()
        } */else {
            return UUID.randomUUID().toString()
        }
    }

    override fun register(msg: Map<*, *>): String {
        var accountId = msg[SdkKey.ACCOUNT_ID].toString()
        var binding = true
        val type = msg[SdkKey.ACCOUNT_TYPE] as Int
        if (accountId.length < 2) {
            accountId = createAccountId(type, msg)
            binding = false
        }
        val accountName = if (msg[SdkKey.ACCOUNT_NAME].toString().length < 2) null else msg[SdkKey.ACCOUNT_NAME].toString()
        var password: String? = null
        if (accountName != null) {
            password = StringUtil.changePassword(msg[SdkKey.PASSWORD].toString())
        }
        saveAccount(binding, accountId, accountName, password, type)
        return accountId
    }

    @Throws(PlatformException::class)
    private fun saveAccount(binding: Boolean, id: String, name: String?, pwd: String?, type: Int?) {
        var flag: Int
        try {
            if (binding) {
                val index = jdbcTemplate.queryForObject(GET_AID, Int::class.java, id)
                if (index == null || index <= 0) {
                    throw PlatformException("account no ,binding error!~", PlatformCode.ACCOUNT_NOT_EXISTENCE)
                }
                typeJudge(type, name)
                println("index----"+index)
                val list = this.jdbcTemplate.queryForList(GET_OTHER_FOR_ID, index)
                if (list != null && !list.isEmpty()) {
                    throw PlatformException("account is binding", PlatformCode.ACCOUNT_IS_BINDING)
                }
                val t = this.jdbcTemplate.queryForList(GET_ACCOUNT_FOR_NAME, name, type)
                if (t != null && !t.isEmpty()) {
                    throw PlatformException("account have!~", PlatformCode.ACCOUNT_IS_BINDING)
                }
                flag = this.jdbcTemplate.update(ADD_ACCOUNT_OTHER, index, type, name, pwd, System.currentTimeMillis())
                if (type == 2) {
                    this.jdbcTemplate.update(ADD_NOT_CERTIFIED, name)
                }
            } else {
                val time = System.currentTimeMillis()
                val f = name != null
                if (f) {
                    val t = this.jdbcTemplate.queryForList(GET_ACCOUNT_FOR_NAME, name, type)
                    if (t != null && !t.isEmpty()) {
                        throw PlatformException("account have!~", PlatformCode.ACCOUNT_IS_BINDING)
                    }
                }
                flag = this.jdbcTemplate.update(ADD_ACCOUNT, id, time)
                if (f && flag > 0) {
                    val index = jdbcTemplate.queryForObject(GET_AID, Int::class.java, id)
                    typeJudge(type, name)
                    flag = this.jdbcTemplate.update(ADD_ACCOUNT_OTHER, index, type, name, pwd, time)
                    if (type == 2) {
                        this.jdbcTemplate.update(ADD_NOT_CERTIFIED, name)
                    }
                }
            }
        } catch (de: DataAccessException) {
            de.printStackTrace()
            throw PlatformException("data add error!~" + de.message, PlatformCode.DATA_ADD_ERROR)
        }

        if (flag < 0) {
            throw PlatformException("data add error!~", PlatformCode.DATA_ADD_ERROR)
        }
    }

    @Throws(PlatformException::class)
    private fun typeJudge(type: Int?, account: String?) {
        if (type == null) {
            throw PlatformException("account type no ,binding error!~", PlatformCode.PARAMETER_NULL)
        } else if (type == 2) {
            if (!StringUtil.isMail(account)) {
                throw PlatformException("mail is error~", PlatformCode.ACCOUNT_IS_NOT_MAIL)
            }
        }
    }

    override fun restPassword(msg: Map<*, *>) {
        val account = accountVerification(msg)
        val oldPassword = account?.get(0)?.get("password")?.toString()
        if (oldPassword != StringUtil.changePassword(msg[SdkKey.PASSWORD].toString())) {
            throw PlatformException("psd is error !~", PlatformCode.ACCOUNT_PASSWORD_ERROR)
        }
        val newPassword = StringUtil.changePassword(msg[SdkKey.PASSWORD_2].toString())
        if (oldPassword == newPassword) {
            throw PlatformException("newPassword=oldPassword !~", PlatformCode.ACCOUNT_PASSWORD_ALIKE)
        }
        this.jdbcTemplate.update(RESET_PASSWORD, newPassword, System.currentTimeMillis(), msg[SdkKey.ACCOUNT_NAME])
    }

    override fun retrievePassword(msg: Map<*, *>) {
        codeAuthenticity(msg)
        accountVerification(msg)
        this.jdbcTemplate.update(RESET_PASSWORD,StringUtil.changePassword(msg[SdkKey.PASSWORD_2].toString()), System.currentTimeMillis(), msg[SdkKey.ACCOUNT_NAME])
    }

    override fun accountCertified(account: String) {
        val flag = jdbcTemplate.update(DEL_CERTIFIED, account)
        if (flag != 1) {
            throw PlatformException("该账户不存在或者已经认证~")
        }
    }

    override fun accountVerification(msg: Map<*, *>): MutableList<MutableMap<String, Any>>? {
        val type = msg[SdkKey.ACCOUNT_TYPE] as Int
        val account = msg[SdkKey.ACCOUNT_NAME].toString()
        if (type == 2) {
            val index = jdbcTemplate.queryForList(GET_CERTIFIED, account)
            if (index != null && !index.isEmpty()) {
                throw PlatformException("该账户未认证~", PlatformCode.ACCOUNT_NOT_CERTIFIED)
            }
        }
        return accountIsExistence(account, type)
    }

    private fun accountIsExistence(account : String,type:Int): MutableList<MutableMap<String, Any>>? {
        val t = this.jdbcTemplate.queryForList(GET_ACCOUNT_FOR_NAME, account, type)
        if (t == null || t.isEmpty()) {
            throw PlatformException("account not have!~", PlatformCode.ACCOUNT_NOT_EXISTENCE)
        }
        return t
    }

    @Throws(PlatformException::class)
    private fun codeAuthenticity(msg: Map<*, *>) {
        val sign = msg[SdkKey.ATTACH_SIGN].toString()
        val ct = DESEncrypt.decoderByDES(PlatformKey.SET_PASSWORD, sign).split("-")
        if (ct.size != 2) {
            throw PlatformException("set password sign is error !~", PlatformCode.ACCOUNT_SIGN_ERROR)
        }
        val time = java.lang.Long.parseLong(ct[1])
        if (System.currentTimeMillis() - time > 60000) {
            throw PlatformException("set password PARAMETER_OUT_TIME !~", PlatformCode.PARAMETER_OUT_TIME)
        }
        if (ct[0] != msg[SdkKey.V_CODE]) {
            throw PlatformException("set password code is error !~", PlatformCode.COD_ERROR)
        }
    }
}