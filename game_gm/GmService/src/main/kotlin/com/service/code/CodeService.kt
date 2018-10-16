package com.service.code

import com.cache.ServiceCache
import com.constant.SendType
import com.gmdesign.bean.other.GmHashMap
import com.gmdesign.exception.GmException
import com.gmdesign.util.DateUtil
import com.gmdesign.util.SendUtil
import com.service.config.ConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

/**
 * Created by DJL on 2018/1/12.
 * @ClassName gm
 * @Description
 */
interface CodeServiceIF{
     fun queryAid(code:String):GmHashMap
     fun isJudgeAchiveId(aid: Int, pid: String): Boolean
     fun isUseForNormal(code: String): Boolean
     fun isUseForPlayer(code: String, pid: String): Boolean
     fun isUseForServer(code: String, sid: String): Boolean
     fun isBindingCode(aid: Int, pid: String): Boolean
     fun sendMail(attr: GmHashMap,sid:String, pid: String)
     fun isUseVerify(code: String, pid: String)

    val queryAid get() = "select aid from gm_config.prize_code where prizeCode=?;"
    val QUERY_ALL_ATTR get() = "select * from gm_config.prize_code_attr;"
    val ADD_LOG get() = "insert into analysis_log.prize_code_use(id,prizeCode,pid,serverId,platform,modifyTime,achiveId) values(null,?,?,?,?,?,?)"
    val AID_NUM get() = "select count(id)num from analysis_log.prize_code_use where pid=? and achiveId=?;"
    val NORMAL get() = "select count(id)num from analysis_log.prize_code_use where prizeCode=?;"
    val FOR_PLAYER get() = "select count(id)num from analysis_log.prize_code_use where prizeCode=? and pid=?"
    val FOR_SERVER get() = "select count(id)num from analysis_log.prize_code_use where prizeCode=? and serverId=?;"
    val QUERY_BINDING_ONE get() = "select bid code from gm_config.prize_binding where aid=?;"
    val QUERY_BINDING_TWO get() = "select aid code from gm_config.prize_binding where bid=?;"
    val PLAYER_USE_AID get() = "select achiveId from analysis_log.prize_code_use where pid=? group by achiveId;"
    val QUERY_CODE_USE_BY_CODE get() = "select * from gm_config.prize_code_use where code=? and pid=?"
    val ADD_CODE_USE get() = "insert into gm_config.prize_code_use(code,aid,pid) values (?,?,?)"
    val QUERY_CODE_INFO_BY_CODE get() = "select pca.id,pca.serverId,pca.type,pca.specialType from gm_config.prize_code_attr pca left join gm_config.prize_code pc on pca.id = pc.aid where pc.prizeCode=?"
    val QUERY_CODE_USE_BY_AID get() = "select * from gm_config.prize_code_use where aid=? and pid=?"

}
@Service("codeService")
class CodeService(private @Autowired val jdbcTemplate: JdbcTemplate, @Autowired private val configService: ConfigService):CodeServiceIF{

    private fun initCode(){
        val list=jdbcTemplate.queryForList(QUERY_ALL_ATTR)
        for (map in list){
            map.put("time", DateUtil.toDateTime(map["endTime"].toString()).time)
            val gmp = GmHashMap()
            gmp.putAll(map)
            ServiceCache.ATTR[map["id"] as Int]=gmp
        }
    }

    @Throws(GmException::class)
    override fun queryAid(code: String): GmHashMap {
        val aid=jdbcTemplate.queryForList(queryAid,code)
        if (aid == null || aid.isEmpty()) {
            throw GmException("PRIZE_CODE_ERROR", SendType.PRIZE_CODE_ERROR)
        }
        if(ServiceCache.ATTR.isEmpty()){
            initCode()
        }
        val id :Int= aid[0]["aid"] as Int
        return ServiceCache.ATTR[id]!!
    }

    override fun isJudgeAchiveId(aid: Int, pid: String): Boolean {
        val num = jdbcTemplate.queryForObject(AID_NUM, Int::class.java, pid, aid)
        return num > 0
    }

    override fun isUseForNormal(code: String): Boolean {
        val num = jdbcTemplate.queryForObject(NORMAL, Int::class.java, code)
        return num > 0
    }

    override fun isUseForPlayer(code: String, pid: String): Boolean {
        val num = jdbcTemplate.queryForObject(FOR_PLAYER, Int::class.java, code, pid)
        return num > 0
    }

    override fun isUseForServer(code: String, sid: String): Boolean {
        val num = jdbcTemplate.queryForObject(FOR_SERVER, Int::class.java, code, sid)
        return num > 0
    }

    override fun isBindingCode(aid: Int, pid: String): Boolean {
        if (isJudgeAchiveId(aid, pid)) {
            return false
        }
        var aids = jdbcTemplate.queryForList(QUERY_BINDING_ONE,aid)
        if (aids == null || aids.isEmpty()) {
            val aids2 = jdbcTemplate.queryForList(QUERY_BINDING_TWO, aid)
            if (aids2 == null || aids2.isEmpty()) {
                return true
            } else {
                aids = aids2
            }
        }
        val uAids = jdbcTemplate.queryForList(PLAYER_USE_AID, pid)
        if (uAids == null || uAids.isEmpty()) {
            return true
        }
        for (a in aids) {
            uAids.filter { a["code"].toString() == it["achiveId"].toString()}.forEach { return false }
        }
        return true
    }

    override fun sendMail(attr: GmHashMap,sid: String, pid: String) {
        val mailStr = "mail?&playerId="+pid+"&title=${attr["mailTitle"]}&context=${attr["mailText"]}&funcsJson=${attr["func"]}"
        System.out.println("发送邮件attr:$attr=sid:$sid=pid:$pid")
        val map = configService.queryGameConfig(sid)
        val url = "http://" + map["serverIP"]+ ":" +  map["httpPort"] + "/game/mail"
        SendUtil.sendHttpMsg(url,mailStr)

    }

    @Throws(GmException::class)
    override fun isUseVerify(code: String, pid: String) {

        val codeStatus = jdbcTemplate.queryForList(QUERY_CODE_INFO_BY_CODE,code)
        if (codeStatus != null && codeStatus.size>0){
            if (codeStatus[0]["type"] ==1){
                if (codeStatus[0]["specialType"] == 0){
                    val codeUse = jdbcTemplate.queryForList(QUERY_CODE_USE_BY_AID,codeStatus[0]["id"],pid)
                    if (codeUse != null && codeUse.size>0){
                        throw GmException("批次兑换码一个用户只能使用其中一个",SendType.PRIZE_CODE_USE)
                    }else{
                        jdbcTemplate.update(ADD_CODE_USE,code,codeStatus[0]["id"],pid)
                    }
                }
                if (codeStatus[0]["specialType"] == 1){
                    val codeUse = jdbcTemplate.queryForList(QUERY_CODE_USE_BY_CODE,code,pid)
                    if (codeUse != null && codeUse.size>0){
                        throw GmException("该用户已使用过此兑换码",SendType.PRIZE_CODE_USE)
                    }else{
                        jdbcTemplate.update(ADD_CODE_USE,code,codeStatus[0]["id"],pid)
                    }
                }
            }
            if (codeStatus[0]["type"] ==-1){
                if (codeStatus[0]["specialType"] == 0){
                    val codeUse = jdbcTemplate.queryForList(QUERY_CODE_USE_BY_CODE,code,pid)
                    if (codeUse != null && codeUse.size>0){
                        throw GmException("该用户已使用过此兑换码",SendType.PRIZE_CODE_USE)
                    }else{
                        jdbcTemplate.update(ADD_CODE_USE,code,codeStatus[0]["id"],pid)
                    }
                }
            }
            if (codeStatus[0]["type"] ==2){

            }
        }else{
            throw GmException("PRIZE_CODE NOT EXIST")
        }

    }

}
