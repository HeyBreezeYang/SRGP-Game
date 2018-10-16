package com.cellsgame.game.module.guild.bo.impl;

import java.nio.charset.Charset;
import java.util.*;
import javax.annotation.Resource;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cache.CacheWord;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.SYSCons;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.goods.bo.GoodsBO;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.guild.cache.CacheDisGuild;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.cons.*;
import com.cellsgame.game.module.guild.csv.*;
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.guild.msg.MsgFactoryGuild;
import com.cellsgame.game.module.guild.vo.*;
import com.cellsgame.game.module.mail.MailFactory;
import com.cellsgame.game.module.mail.MailType;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.mail.cons.MailConstant;
import com.cellsgame.game.module.mail.cons.MailFuncType;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.cons.PlayerInfoVOUpdateType;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.msg.MsgFactoryPlayer;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.rank.bo.RankBO;
import com.cellsgame.game.module.sys.SystemBO;
import com.cellsgame.game.util.StringVerifyUtil;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on  2016-08-15.
 */
public class GuildBOImpl implements GuildBO{

    private static final Charset GBK = Charset.forName("GBK");

    private static Logger Logger = LoggerFactory.getLogger(GuildBOImpl.class);
    @Resource
    private BaseDAO<GuildVO> guildDAO;
    @Resource
    private BaseDAO<GuildMemberVO> guildMemberDAO;
    @Resource
    private BaseDAO<PlayerVO> playerDAO;
    @Resource
    private BaseDAO<GuildReqVO> guildReqDAO;

    private MailBO mailBO;
    private RankBO rankBO;
    private SystemBO systemBO;
    private ActivityBO activityBO;
    private GoodsBO goodsBO;
    private DepotBO depotBO;
    
    private MsgFactoryGuild msgFactoryGuild = MsgFactoryGuild.instance();


    public static int getMaxMemberSize(GuildVO guildVO) {
        return CacheDisGuild.MaxMemberSize.getData()[guildVO.getLevel()];
    }

    private void checkName(String name) throws LogicException {
        CodeGuild.NotUseEmptyName.throwIfTrue(NumberUtils.isDigits(name));
        CodeGuild.NameFormationError.throwIfTrue(!StringVerifyUtil.isPATTERN_ABC_1(name));
        checkLen(name, 2, 12);
        CodeGuild.SameName.throwIfTrue(CacheGuild.hashName(name));
    }

    private void checkLen(String name, int min, int max) throws LogicException {
        CodeGeneral.General_Param_Error.throwIfTrue(StringUtil.isEmpty(name));
        byte[] gbks = name.getBytes(GBK);
        CodeGuild.NameTooShort.throwIfTrue(gbks.length < min);
        CodeGuild.NameTooLong.throwIfTrue(gbks.length > max);
        CodeGuild.HaveKeyWords.throwIfTrue(CacheWord.haveKeyWord(name));
        CodeGuild.HaveKeyWords.throwIfTrue(CacheWord.haveDirtyWord(name));
    }

    @Override
    public GuildVO getGuildVOAndCheckRight(PlayerVO pvo, GuildRight right) throws LogicException {
        GuildMemberVO memberVO = getGuildMemberVO(pvo);
        CodeGuild.NotJoinGuild.throwIfTrue(null == memberVO);
        GuildVO guildVO = CacheGuild.getGuildByID(memberVO.getGuildID());
        CodeGuild.NotJoinGuild.throwIfTrue(null == guildVO);

//        if (right != GuildRight.DISSOLUTION_GUILD && right != GuildRight.OutGuild) {
//            checkGuildStatus(guildVO);
//        }

        if (right != null) {
            Integer memberRight = guildVO.getMemberRights().get(pvo.getId());
            RightLevel rightLevel = Enums.get(RightLevel.class, memberRight);
            CodeGuild.RightMinus.throwIfTrue(null == rightLevel || !rightLevel.haveRight(right));
        }
        return guildVO;
    }

    private GuildMemberVO getGuildMemberVO(PlayerVO pvo) {
        return getGuildMemberVO(pvo.getId());
    }

    private GuildMemberVO getGuildMemberVO(int pid) {
        return CacheGuild.getGuildMemberVO(pid);
    }

    private void checkGuildStatus(GuildVO vo) throws LogicException {
        CodeGuild.AlreadyDissolution.throwIfTrue(GuildVO.statusUpdater.get(vo) != GuildVO.NORMAL);
    }

    /**
     * 初始化数据
     * 将db加载到内存
     */
    @Override
    public void init() {
        List<DBObj> all = guildDAO.getAll();
        if (all != null) {
            for (DBObj dbObj : all) {
                GuildVO vo = new GuildVO();
                vo.readFromDBObj(dbObj);
                CacheGuild.addCache(vo);
                // 解散状态的工会添加到缓存
                if (vo.getStatus() == GuildVO.IN_DISSOLUTION) {
                    CacheGuild.inDissolutionGuild.add(vo);
                }
                refNoNeedReqGuilds(vo);
            }
        }
        List<DBObj> all1 = guildReqDAO.getAll();
        if (all1 != null) {
            for (DBObj dbObj : all1) {
                GuildReqVO vo = new GuildReqVO();
                vo.readFromDBObj(dbObj);
                CacheGuild.cacheReq(vo);
            }

        }
        List<DBObj> all2 = guildMemberDAO.getAll();
        if (all2 != null) {
            for (DBObj dbObj : all2) {
                GuildMemberVO vo = new GuildMemberVO();
                vo.readFromDBObj(dbObj);
                CacheGuild.cacheMember(vo);
                resetGuildMember(vo);
            }
        }


        // 检查并且刷新系统数据
        systemResetGuildData();
    }

    private void refNoNeedReqGuilds(GuildVO guildVO) {
        if(guildVO.isEnterNeedReq()) return;
        if(isMaxSize(guildVO)) {
            CacheGuild.NoReqGuild.remove(guildVO);
            return;
        };
        if(CacheGuild.NoReqGuild.contains(guildVO)) return;
        CacheGuild.NoReqGuild.add(guildVO);
    }


    @Override
    public Map<?, ?> createGuild(PlayerVO pvo, CMD cmd, String cliName, String qq, String vx, String notice, boolean needReq) throws LogicException {
        String name = cliName.trim();
        GuildConfig config = GuildConfig.getConfig();
//        int plv = pvo.getLevel();
        if (null == qq) qq = "";
        if (null == vx) vx = "";
        if (null == notice) notice = "";
        CodeGeneral.General_Param_Error.throwIfTrue(StringUtil.isEmpty(name));
//        CodeGuild.PlayerLevelMinus.throwIfTrue(plv < config.getCreateLimitPlayerLV());

        GuildMemberVO memberVO = getGuildMemberVO(pvo);
        if (null != memberVO) {
            GuildVO guildVO = CacheGuild.getGuildByID(memberVO.getGuildID());
            CodeGuild.AlreadyInGuild.throwIfTrue(null != guildVO);
        }
        // checkName
        checkName(name);

        // 处理屏蔽词
        qq = CacheWord.replaceDirtyWords(qq);
        vx = CacheWord.replaceDirtyWords(vx);
        notice = CacheWord.replaceDirtyWords(notice);

        Map<String, Object> result = GameUtil.createSimpleMap();
        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
        exec.addSyncFunc(SyncFuncType.ChangeCur, CurrencyType.TRE.getType(), -Math.abs(CacheDisGuild.CreateGuildCostTre.getData()[0]));
        exec.exec(result, pvo);

        GuildVO vo = new GuildVO();
        vo.setLevel(1);
        vo.setCreator(pvo.getId());
        vo.setOwner(pvo.getId());
        vo.setQq(qq);
        vo.setVx(vx);
        vo.setNotice(notice);
        vo.setName(name);
        vo.setEnterNeedReq(needReq);
        guildDAO.save(vo);
        directJoinGuild(result, pvo.getId(), vo, RightLevel.Level0, cmd);
        CacheGuild.addCache(vo);
        refNoNeedReqGuilds(vo);
        rankBO.build(vo);
        EvtTypeGuild.Create.happen(result, cmd, vo, EvtParamType.PLAYER.val(pvo));
        return MsgUtil.brmAll(msgFactoryGuild.toFullInfo(result, vo, getGuildMemberVO(pvo.getId())), cmd.getCmd());
    }

    @Override
    public Map enterGuild(PlayerVO pvo, CMD cmd) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, null);
        Map<String, Object> ret = GameUtil.createSimpleMap();
    	EvtTypeGuild.LV_UP.happen(ret, cmd, pvo, EvtParamType.AFTER.val(guildVO.getLevel()+0L));
        return MsgUtil.brmAll(msgFactoryGuild.toFullInfo(ret, guildVO, getGuildMemberVO(pvo)), cmd.getCmd());
    }

    @Override
    public Map<String, Object> queryMemberList(PlayerVO pvo, CMD message) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, null);
        Map<String, Object> msg = msgFactoryGuild.getAllMemberInfo(guildVO);
        return MsgUtil.brmAll(msg, message.getCmd());
    }

    @Override
    public Map<String, Object> queryGuild(CMD message, PlayerVO ovo, String name) throws LogicException {
        boolean isNum = NumberUtils.isDigits(name);
        Collection<GuildVO> vos;
        if (isNum) {
            // 通过ID查找
            GuildVO guildVO = CacheGuild.getGuildByID(Integer.parseInt(name));
            if (null != guildVO)
                vos = Collections.singletonList(guildVO);
            else vos = Collections.emptyList();
        } else {
            if (null != name && name.length() > 0) {
                vos = CacheGuild.fuzzyQueryByName(name);
            } else {
                // 匿名推荐
                // 先返回请求过的
                int needSize = CacheGuild.maxSearchGuildSize;
                Map<Integer, GuildReqVO> req = CacheGuild.getReqByPid(ovo.getId());
                vos = new HashSet<>();
                if (req != null) {
                    for (GuildReqVO reqVO : req.values()) {
                        vos.add(CacheGuild.getGuildByID(reqVO.getGuildID()));
                    }
                    needSize -= req.size();
                }
                if (needSize > 0)
                    for (GuildVO vo : CacheGuild.recommendGuild) {
                        if (vos.contains(vo)) continue;
                        vos.add(vo);
                        needSize--;
                        if (needSize <= 0)
                            break;
                    }
            }
        }
        MsgFactoryGuild msg = msgFactoryGuild;
        Map<String, Object> parent = GameUtil.createSimpleMap();
        Map<String, Object> info = msg.gocInfoMap(parent);
        List<Map> list = msg.gocLstIn(info, MsgFactoryGuild.QUERY_GUILD);
        for (GuildVO vo : vos) {
            Map<String, Object> map = msg.toBaseInfoMap(GameUtil.createSimpleMap(), vo);
            if (null != map) {
                GuildReqVO req = CacheGuild.getReq(ovo.getId(), vo.getId());
                map.put(MsgFactoryGuild.GUILD_REQ_ED, req != null);
                list.add(map);
            }
        }
        return MsgUtil.brmAll(parent, message.getCmd());
    }

    @Override
    public Map<?, ?> joinGuild(CMD cmd, PlayerVO pvo, int guildID) throws LogicException {
        GuildVO guildVO;
        if(guildID > 0) {
            guildVO = CacheGuild.getGuildByID(guildID);
            CodeGuild.NotFindGuild.throwIfTrue(null == guildVO);
        }else{
            int len = CacheGuild.NoReqGuild.size();
            CodeGuild.NotMatchGuild.throwIfTrue(len <= 0);
            int index = GameUtil.r.nextInt(len);
            guildVO = CacheGuild.NoReqGuild.get(index);
        }
        GuildMemberVO memberVO = getGuildMemberVO(pvo);
        GuildConfig guildConfig = GuildConfig.getConfig();
        if (null != memberVO) {
            CodeGuild.AlredyInGuild.throwIfTrue(CacheGuild.getGuildByID(memberVO.getGuildID()) != null);
            CodeGuild.EnterGuildCD.throwIfTrue(memberVO.getNextEnterGuildTime() > System.currentTimeMillis());
        }
        checkGuildStatus(guildVO);


        // 判断是否成员达到了上限
        CodeGuild.MemberFull.throwIfTrue(isMaxSize(guildVO));

        Map msg = null;
        // 如果需要申请
        if (guildVO.isEnterNeedReq()) {
            GuildLVCfg cfg = GuildLVCfg.getByLV(guildVO.getLevel());
            CodeGuild.MaxReqSize.throwIfTrue(CacheGuild.getGuildReqedSize(guildVO.getId()) >= cfg.getMaxCanReqSize());
            GuildReqVO req = new GuildReqVO();
            req.setPid(pvo.getId());
            req.setGuildID(guildID);
            req.setTime(System.currentTimeMillis());
            CacheGuild.cacheReq(req);
            guildReqDAO.save(req);
        } else {
            msg = directJoinGuild(GameUtil.createSimpleMap(), pvo.getId(), guildVO, RightLevel.Level3, cmd);
            refNoNeedReqGuilds(guildVO);
        }
        return MsgUtil.brmAll(msg, cmd.getCmd());
    }

    /**
     * 实际加入家族 r如果玩家已经存在于家族中 则不处理
     * 会清空请求列表
     */
    private Map directJoinGuild(Map ret, int pid, GuildVO guildVO, RightLevel rightLevel, CMD cmd) {
        GuildMemberVO memberVO = getGuildMemberVO(pid);
        // 不检查公会人数
//        if (isMaxSize(guildVO)) {
//            return ret;
//        }
        if (memberVO == null) {
            memberVO = new GuildMemberVO();
            memberVO.setPid(pid);
            memberVO.setJoinGuildTime(System.currentTimeMillis());
            memberVO.setLastRestTime(System.currentTimeMillis());
            memberVO.setGuildID(guildVO.getId());
            CacheGuild.cacheMember(memberVO);
            guildMemberDAO.save(memberVO);
        } else {
            // 如果已经存在
            if (CacheGuild.getGuildByID(memberVO.getGuildID()) != null) {
                return ret;
            }
        }
        memberVO.setGuildID(guildVO.getId());
        PlayerVO pvo = CachePlayer.getPlayerByPid(pid);
        if (null != pvo) {
            pvo.setGuildID(guildVO.getId());
            playerDAO.save(pvo);
        }

        // 成功加入 家族
        // 1.清空请求列表
        Collection<GuildReqVO> reqVOMap = CacheGuild.removeReq(pid);
        guildReqDAO.delete(reqVOMap);

        // 设置加入数据
        chgRight(guildVO, pid, rightLevel);
        memberVO.setGuildID(guildVO.getId());
        memberVO.setJoinGuildTime(System.currentTimeMillis());
        guildMemberDAO.save(memberVO);
        CachePlayerBase.updateBasInfo(pvo, PlayerInfoVOUpdateType.GuildInfo);

        if (isMaxSize(guildVO)) {
            // 成员已经满了
            CacheGuild.recommendGuild.remove(guildVO);
            // 清空邀请列表
            guildVO.getInviteList().clear();
            // 家族的申请列表 由时间控制自动删除
        }
        // LOG
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
        if (null != baseInfo)
            guildLog(guildVO, GuildLogType.JoinGuild, baseInfo.getName());
        guildDAO.save(guildVO);
        ret = EvtTypeGuild.Join.happen(ret, cmd, pvo, EvtParamType.Right_Level.val(rightLevel), EvtParamType.PlayerInfoVO.val(baseInfo));
        ret = msgFactoryGuild.memberAddMsg(ret, guildVO, getGuildMemberVO(pvo.getId()));
        return ret;
    }

    private void guildLog(GuildVO guildVO, GuildLogType logType, String... param) {
        List<GuildLogVO> logs = guildVO.getLogs();
        int size = CacheDisGuild.GuildLogLen.first();

        GuildLogVO logVO = new GuildLogVO();
        logVO.setLogID(logType.getId());
        logVO.setParam(param);

        add2List(logVO, logs, size);
        guildDAO.save(guildVO);
    }

    private <T> T add2List(T logVO, List<T> logs, int size) {
        if (size > 0) {
            // 剩下N-1条
            for (int i = logs.size() - size; i >= 0; i--) {
                logs.remove(logs.size() - 1);
            }
            logs.add(0, logVO);
            return logVO;
        } else {
            logs.clear();
        }
        return null;
    }

    /**
     * 批准玩家加入家族
     */
    @Override
    public Map<?, ?> acceptPlayerJoinGuild(CMD cmd, PlayerVO pvo, int pid, boolean accept) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.APPROVAL_JOIN_GUILD);
        GuildReqVO req = CacheGuild.getReq(pid, guildVO.getId());
        CodeGuild.ReqTimeout.throwIfTrue(null == req);
        Map result = GameUtil.createSimpleMap();
        if (accept) {
            CodeGuild.MemberFull.throwIfTrue(isMaxSize(guildVO));
            GuildMemberVO vo = getGuildMemberVO(pid);
            // 没有加入
            boolean notInGuild = null == vo || vo.getGuildID() <= 0;
            if (notInGuild && GuildReqVO.statusUpdater.compareAndSet(req, GuildReqVO.STATUS_NORMAL, GuildReqVO.STATUS_ACCEPT)) {
                CodeGuild.MemberFull.throwIfTrue(isMaxSize(guildVO));
                PlayerVO tgtVO = CachePlayer.getPlayerByPid(pid);
                result = directJoinGuild(result, pid, guildVO, RightLevel.Level3, cmd);
                GuildMemberVO memberVO = getGuildMemberVO(pid);
                if (tgtVO.isOnline()) {
                    Map msg = msgFactoryGuild.toFullInfo(null, guildVO, memberVO);
                    msg = msgFactoryGuild.guildJoinStateMsg(msg, guildVO, true);
                    Push.multiThreadSend(tgtVO.getMessageController(), MsgUtil.brmAll(msg, Command.Guild_Join));
                }
            } else {
                // 被其他加入了 或者状态错误 直接删除错误的请求
                guildReqDAO.delete(req);
                CacheGuild.removeReq(req);
                PlayerVO tgtVO = CachePlayer.getPlayerByPid(pid);
                if (tgtVO.isOnline()) {
                    Map msg = msgFactoryGuild.guildJoinStateMsg(null, guildVO, false);
                    Push.multiThreadSend(tgtVO.getMessageController(), MsgUtil.brmAll(msg, Command.Guild_Join));
                }
            }
        } else {
            dealGuildReqVO(guildVO, req);
            PlayerVO tgtVO = CachePlayer.getPlayerByPid(pid);
            if (tgtVO.isOnline()) {
                Map msg = msgFactoryGuild.guildJoinStateMsg(null, guildVO, false);
                Push.multiThreadSend(tgtVO.getMessageController(), MsgUtil.brmAll(msg, Command.Guild_Join));
            }
        }
        return MsgUtil.brmAll(result, cmd.getCmd());
    }

    private void dealGuildReqVO(GuildVO guildVO, GuildReqVO req){
        if (GuildReqVO.statusUpdater.compareAndSet(req, GuildReqVO.STATUS_NORMAL, GuildReqVO.STATUS_REFUSE)) {
            guildReqDAO.delete(req);
            CacheGuild.removeReq(req);
//            GuildConfig config = GuildConfig.getConfig();
//            String content = config.getGuildRejectJoinContent();
//            content = content.replace("{guildName}", guildVO.getName());
//            mailBO.sendMailByID(MailConstant.MAIL_SENDER_NAME_SYS, req.getPid(), MailType.Guild, config.getGuildRejectJoinTitle(), content);
        }
    }

    @Override
    public Map notAllJoinReq(CMD cmd, PlayerVO pvo) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.APPROVAL_JOIN_GUILD);
        Map<Integer, GuildReqVO> all = CacheGuild.getReqByGuildId(guildVO.getId());
        if(!all.isEmpty()){
            for (GuildReqVO req : all.values()) {
                dealGuildReqVO(guildVO, req);
            }
        }
        return MsgUtil.brmAll(cmd.getCmd());
    }

    private boolean isMaxSize(GuildVO guildVO) {
        return guildVO.getMemberSize() >= getMaxMemberSize(guildVO);
    }

    /**
     * 修改公告
     */
    @Override
    public Map<String, Object> modifyNotice(CMD cmd, PlayerVO pvo, String content) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.MODIFY_GUILD_NOTICE);
        checkLen(content, 0, GuildConfig.getConfig().getMaxNoticeLen());
        guildVO.setNotice(content);
        guildDAO.save(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(null, guildVO), cmd.getCmd());
    }

    /**
     * 修改 宣言 描述
     */
    @Override
    public Map<String, Object> modifyDesc(CMD cmd, PlayerVO pvo, String content) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.MODIFY_GUILD_DESC);
        checkLen(content, 0, GuildConfig.getConfig().getMaxDescLen());
        guildVO.setDesc(content);
        guildDAO.save(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(null, guildVO), cmd.getCmd());
    }

    @Override
    public Map<String, Object> modifyQQ(CMD message, PlayerVO pvo, String qq) throws LogicException {
        CodeGeneral.General_Param_Error.throwIfTrue(StringUtil.isEmpty(qq));
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.MODIFY_GUILD_QQ);
        guildVO.setQq(qq);
        guildDAO.save(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(GameUtil.createSimpleMap(), guildVO), message.getCmd());
    }

    @Override
    public Map<String, Object> modifyVX(CMD message, PlayerVO pvo, String vx) throws LogicException {
        CodeGeneral.General_Param_Error.throwIfTrue(StringUtil.isEmpty(vx));
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.MODIFY_GUILD_VX);
        guildVO.setVx(vx);
        guildDAO.save(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(GameUtil.createSimpleMap(), guildVO), message.getCmd());
    }

    /**
     * 修改家族名字
     */
    @Override
    public Map<String, Object> modifyName(CMD message, PlayerVO pvo, String name) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.MODIFY_GUILD_NAME);
        checkName(name);
        Map result = GameUtil.createSimpleMap();
        int cost = CacheDisGuild.ChangeGuildNameCost.first();
        SyncFuncType.ChangeCur.getFunc().checkAndExec(result,message,pvo,new FuncParam(CurrencyType.TRE.getType(), -cost));
        String oldName = guildVO.getName();
        guildVO.setName(name);
        // 重新添加到缓存 更新名字缓存    老名字将不能使用
        CacheGuild.resetNameCache(oldName, guildVO);
        guildDAO.save(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(result, guildVO), message.getCmd());

    }

    @Override
    public Map<String, Object> modifyRight(CMD message, PlayerVO pvo, int pid, int tgtRight) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.MODIFY_RIGHT_LEVEL);
        Map<Integer, Integer> rights = guildVO.getMemberRights();
        Integer tgtRightObj = rights.get(pid);
        CodePlayer.Player_NotFindPlayer.throwIfTrue(null == tgtRightObj);
        // 不可操作比自己权限大的人
        Integer myRight = rights.get(pvo.getId());
        CodeGuild.RightMinus.throwIfTrue(tgtRight >= myRight);
        tgtRight = Math.max(RightLevel.Level3.getId(), tgtRight);
        RightLevel tgtRightLV = Enums.get(RightLevel.class, tgtRight);
        CodeGuild.RightMinus.throwIfTrue(null == tgtRightLV);
        int size = -1;
        switch (tgtRightLV) {
            case Level2:
                int[] data = CacheDisGuild.RightLevel2MemberSize.getData();
                size = data[Math.min(data.length, guildVO.getLevel()) - 1];
                break;
            case Level1:
                data = CacheDisGuild.RightLevel1MemberSize.getData();
                size = data[Math.min(data.length, guildVO.getLevel()) - 1];
                break;
        }
        if (size > 0) {
            int rightlvNum = 0;
            for (Integer right : rights.values()) {
                if (tgtRight == right) {
                    rightlvNum++;
                }
            }
            CodeGuild.RightLevelMemberFull.throwIfTrue(rightlvNum >= size);
        }
        rights.put(pid, tgtRight);
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
        if (null != baseInfo) {
            guildLog(guildVO, GuildLogType.ChangeRight, pvo.getName(), baseInfo.getName(), String.valueOf(tgtRight), String.valueOf(tgtRight));
        }
        notifyRightChg(pid, tgtRight);
        guildDAO.save(guildVO);
        Map result = GameUtil.createSimpleMap();

        return MsgUtil.brmAll((Map<String, Object>) null, message.getCmd());
    }

    private void notifyRightChg(int pid, Integer tgtRight) {
        PlayerVO tgtPvo = CachePlayer.getPlayerByPid(pid);
        if (null != tgtPvo && tgtPvo.isOnline()) {
            Push.multiThreadSend(tgtPvo.getMessageController(), MsgUtil.brmAll(msgFactoryGuild.getGuildRightChgMsg(tgtRight), Command.Guild_ModifyRight));
        }
        GuildConfig config = GuildConfig.getConfig();
        mailBO.sendMailByID(MailConstant.MAIL_SENDER_NAME_SYS, pid, MailType.Guild,
                config.getGuildRightChgMailTitle(), config.getGuildRightChgMailContent());
    }

    /**
     * 离开家族
     */
    @Override
    public Map<String, Object> outGuild(CMD cmd, PlayerVO pvo, int pid) throws LogicException {
        GuildVO guildVO;
        boolean slfOut = pvo.getId() == pid;
        if (slfOut) {
            guildVO = getGuildVOAndCheckRight(pvo, GuildRight.OUT_GUILD);
        } else {
            guildVO = getGuildVOAndCheckRight(pvo, GuildRight.DISMISS_GUILD_MEMBER);
        }
        GuildMemberVO memberVO = getGuildMemberVO(pvo);

        GuildMemberVO opTgt;
        //  会增加时间惩罚
        GuildConfig guildConfig = GuildConfig.getConfig();
        long nextEnterTime = System.currentTimeMillis() + guildConfig.getEnterGuildCD() * DateUtil.HOUR_MILLIS;

        if (slfOut) {
            opTgt = memberVO;
        } else {
            Integer tgtRight = guildVO.getMemberRights().get(pid);
            CodePlayer.Player_NotFindPlayer.throwIfTrue(null == tgtRight);
            // 不能踢 比自己权限大的
            CodeGuild.RightMinus.throwIfTrue(tgtRight >= guildVO.getMemberRights().get(pvo.getId()));
            opTgt = getGuildMemberVO(pid);
        }

        // 移除
        guildVO.getMemberRights().remove(pid);
        guildDAO.save(guildVO);

        if (null != opTgt) {
            opTgt.setNextEnterGuildTime(nextEnterTime);
            opTgt.setGuildID(-1);

            guildMemberDAO.save(opTgt);
            PlayerVO tgtPvo = CachePlayer.getPlayerByPid(opTgt.getPid());
            if (null != tgtPvo) {
                tgtPvo.setGuildID(-1);
                playerDAO.save(tgtPvo);
            }
        }
        String outPlayerName = null;
        // 特殊处理 --> 可能对方没有在线
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
        if (baseInfo != null) {
            baseInfo.setGuildId(-1);
            baseInfo.setGuildName("");
            outPlayerName = baseInfo.getName();
        }

        Map ret = null;
        if (!slfOut) {
            PlayerVO tgtPVO = CachePlayer.getPlayerByPid(pid);
            if (null != tgtPVO) {
                if (tgtPVO.isOnline()) {
                    Map<String, Object> rt = GameUtil.createSimpleMap();
                    MsgFactoryPlayer.instance().getGuildIDNameUpdateMsg(rt, -1, "");
                    Push.multiThreadSend(tgtPVO.getMessageController(), MsgUtil.brmAll(rt, cmd.getCmd()));
                }
                EvtTypePlayer.LeaveGuild.happen(GameUtil.createSimpleMap(), cmd, tgtPVO);
            }
//            String content = guildConfig.getGuildTOutContent().replaceAll("\\{guildName}", guildVO.getName());
            MailFactory.newMail(MailType.System, pid, "out guild title", "out guild")
                    .send();
        } else {
            ret = EvtTypePlayer.LeaveGuild.happen(GameUtil.createSimpleMap(), cmd, pvo);
            //自愿退出公会，扣除公会贡献一半
            long curr = depotBO.getCurByType(pvo, CurrencyType.GUILD_COIN);
            if(curr > 1) {
                long changeVal = curr / 2;
                depotBO.changeCurByType(ret, pvo, CurrencyType.GUILD_COIN, -changeVal, true, true, cmd, false);
            }
        }
        if (StringUtil.isNotEmpty(outPlayerName)) {
            if(slfOut) {
                guildLog(guildVO, GuildLogType.MyOutGuild, outPlayerName);
            }else{
                guildLog(guildVO, GuildLogType.OutGuild, pvo.getName(), outPlayerName);
            }
        }
        if (slfOut)
            MsgFactoryPlayer.instance().getGuildIDNameUpdateMsg(ret, -1, "");
        refNoNeedReqGuilds(guildVO);
        return MsgUtil.brmAll(ret, cmd.getCmd());
    }

    /**
     * 转让家族
     */
    @Override
    public Map<String, Object> chgOwner(CMD message, PlayerVO pvo, int tgtPid) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.CHG_OWNER);
        Integer tgtRight = guildVO.getMemberRights().get(tgtPid);
        CodePlayer.Player_NotFindPlayer.throwIfTrue(null == tgtRight);
        chgRight(guildVO, tgtPid, RightLevel.Level0);
        chgRight(guildVO, pvo.getId(), RightLevel.Level1);
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(tgtPid);
        if (null != baseInfo)
            guildLog(guildVO, GuildLogType.ChangeRight, pvo.getName(), baseInfo.getName(), String.valueOf(tgtRight));
        guildVO.setOwner(tgtPid);
        notifyRightChg(tgtPid, RightLevel.Level0.getId());
        guildDAO.save(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.memberUpdateMsg(GameUtil.createSimpleMap(), guildVO, CacheGuild.getGuildMemberVO(pvo.getId())), message.getCmd());
    }

    /**
     * 解散家族
     */
    @Override
    public Map<String, Object> dissolutionGuild(PlayerVO pvo, CMD message) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.DISSOLUTION_GUILD);
        if (GuildVO.statusUpdater.compareAndSet(guildVO, GuildVO.NORMAL, GuildVO.IN_DISSOLUTION)) {
            guildVO.setDissolutionTime(System.currentTimeMillis() + GuildConfig.getConfig().getDissolutionCD() * DateUtil.HOUR_MILLIS);
            // 将即将解散的家族添加到 缓存
            CacheGuild.inDissolutionGuild.add(guildVO);
            guildDAO.save(guildVO);
        }
        Map result = msgFactoryGuild.guildUpdateMsg(null, guildVO);
        return MsgUtil.brmAll(result, message.getCmd());
    }

    /**
     * 取消解散家族
     */
    @Override
    public Map<String, Object> cancelDissolutionGuild(PlayerVO pvo, CMD message) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.DISSOLUTION_GUILD);
        if (GuildVO.statusUpdater.compareAndSet(guildVO, GuildVO.IN_DISSOLUTION, GuildVO.NORMAL)) {
            guildVO.setDissolutionTime(-1);
            CacheGuild.inDissolutionGuild.remove(guildVO);
            guildDAO.save(guildVO);
        }
        Map result = msgFactoryGuild.guildUpdateMsg(null, guildVO);
        return MsgUtil.brmAll(result, message.getCmd());
    }

    @Override
    public void innerDissolutionGuild(List<GuildVO> guildVOS) {
        if (null == guildVOS || guildVOS.size() == 0)
            return;
        Map<String, Object> ret = MsgUtil.brmAll(MsgFactoryPlayer.instance().getGuildIDNameUpdateMsg(null, -1, ""), Command.Guild_Out);
        for (GuildVO guildVO : guildVOS) {
            if (GuildVO.statusUpdater.compareAndSet(guildVO, GuildVO.IN_DISSOLUTION, GuildVO.DISSOLUTION_END)) {
                Collection<Integer> pids = guildVO.getMemberRights().keySet();
                for (int pid : pids) {
                    GuildMemberVO memberVO = getGuildMemberVO(pid);
                    if (null != memberVO) {
                        memberVO.setGuildID(-1);
                        guildMemberDAO.save(memberVO);
                    }
                    PlayerVO pvo = CachePlayer.getPlayerByPid(pid);
                    // 其他不在线的情况 等玩家登陆的时候再处理
                    if (null != pvo) {
                        pvo.setGuildID(-1);
                        playerDAO.save(pvo);
                        if (pvo.isOnline()) {
                            Push.multiThreadSend(pvo.getMessageController(), ret);
                        }
                    }
                }
            }
            CacheGuild.inDissolutionGuild.remove(guildVO);
            CacheGuild.removeCache(guildVO);
            EvtTypeGuild.Dissolution.happen(GameUtil.createSimpleMap(), CMD.system, guildVO);
            guildDAO.delete(guildVO);
        }
    }

    private void chgRight(GuildVO vo, int pid, RightLevel rightLevel) {
        Map<Integer, Integer> rights = vo.getMemberRights();
        rights.put(pid, rightLevel.getId());
    }

    @Override
    public Map<String, Object> donate(CMD cmd, PlayerVO pvo, int donateCid) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.DONATE);
        DonateConfig config = CacheConfig.getCfg(DonateConfig.class, donateCid);
        CodeGuild.NotFindDonateConfig.throwIfTrue(config == null);
        GuildMemberVO memberVO = getGuildMemberVO(pvo);
        CodeGuild.AlreadyDonate.throwIfTrue(memberVO.getDonateCid() > 0);
        Map result = GameUtil.createSimpleMap();
        Map prizeMap = GameUtil.createSimpleMap();
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
        executor.addSyncFunc(config.getCost());
        executor.addSyncFunc(config.getPrize());
        executor.exec(result, prizeMap, pvo);
        guildVO.setMny(guildVO.getMny() + config.getGuildMoneyPrize());
        addGuildExp(cmd, result, guildVO, pvo, config.getGuildExpPrize());
        memberVO.setDonateCid(donateCid);
        guildMemberDAO.save(memberVO);
        //
        result = EvtTypeGuild.Donate.happen(result, cmd, pvo,
                EvtParamType.GUILD_ID.val(guildVO.getId()),
                EvtParamType.GUILD_NAME.val(guildVO.getName()),
                EvtParamType.CID.val(donateCid),
                EvtParamType.AFTER.val(guildVO.getRankVal()));
        guildDAO.save(guildVO);
        msgFactoryGuild.getPrizeMsg(result, prizeMap);
        msgFactoryGuild.memberUpdateMsg(result, memberVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(result, guildVO), cmd.getCmd());
    }

    private void checkGuildLevelUp(CMD cmd, Map parent, PlayerVO self, GuildVO guildVO) {
        GuildLVCfg nextCfg = GuildLVCfg.getByLV(guildVO.getLevel() + 1);
        CodeGuild.LevelLimit.throwIfTrue(nextCfg == null);
        if(guildVO.getExp() >= nextCfg.getReqExp()) {
            guildVO.setExp(guildVO.getExp() - nextCfg.getReqExp());
            guildVO.setLevel(nextCfg.getLv());
            guildDAO.save(guildVO);
            guildLog(guildVO, GuildLogType.UpLevel, String.valueOf(guildVO.getLevel()));
        	EvtTypeGuild.LV_UP.happen(parent, cmd, self, EvtParamType.AFTER.val(guildVO.getLevel()+0L));
        }
    }

    /**
     * 邀请玩家加入公会
     */
    @Override
    public Map<String, Object> invitePlayerJoin(CMD message, PlayerVO pvo, int pid) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.APPROVAL_JOIN_GUILD);
        String pname = CachePlayerBase.getPnameByPid(pid);
        CodePlayer.Player_NotFindPlayer.throwIfTrue(null == pname);
        guildVO.getInviteList().add(pid);
        GuildConfig config = GuildConfig.getConfig();
        String content = config.getGuildInviteMailContent();
        content = content.replace("{name}", pvo.getName());
        content = content.replace("{guildName}", guildVO.getName());
        mailBO.sendMailByID(MailConstant.MAIL_SENDER_NAME_SYS, pid,
                MailType.Guild, config.getGuildInviteMailTitle(), content,
                MailFuncType.AddGuild, new String[]{String.valueOf(guildVO.getId())});
        return MsgUtil.brmAll(message.getCmd());
    }

    @Override
    public Map<String, Object> showReqList(CMD message, PlayerVO pvo) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, null);
        Map<Integer, GuildReqVO> req = CacheGuild.getReqByGuildId(guildVO.getId());
        Map<Integer, GuildReqVO> tmp = new HashMap<>(req);
        return MsgUtil.brmAll(msgFactoryGuild.getReqList(tmp), message.getCmd());
    }

    @Override
    public Map<String, Object> modifyNeedReqStatus(CMD message, PlayerVO pvo, boolean need) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, GuildRight.APPROVAL_JOIN_GUILD);
        guildVO.setEnterNeedReq(need);
        if(guildVO.isEnterNeedReq()) CacheGuild.NoReqGuild.remove(guildVO);
        return MsgUtil.brmAll(msgFactoryGuild.guildUpdateMsg(GameUtil.createSimpleMap(), guildVO), message.getCmd());
    }

    @Override
    public Map<String, Object> getGuildLOG(CMD message, PlayerVO pvo) throws LogicException {
        GuildVO guildVO = getGuildVOAndCheckRight(pvo, null);
        List<GuildLogVO> logs = guildVO.getLogs();
        return MsgUtil.brmAll(msgFactoryGuild.getGuildLog(logs), message.getCmd());
    }

    private void broadcastMsg2GuildMember(GuildVO guildVO, Map<String, Object> retMsg) {
        for (int pid : guildVO.getMemberRights().keySet()) {
            PlayerVO pvo = CachePlayer.getPlayerByPid(pid);
            if (null != pvo && pvo.isOnline()) {
                Push.multiThreadSend(pvo, retMsg);
            }
        }
    }



    @Override
    public Map addGuildExp(CMD cmd, Map parent, GuildVO guildVO, PlayerVO playerVO, int exp) {
        guildVO.setExp(guildVO.getExp() + exp);
        checkGuildLevelUp(cmd,parent,playerVO,guildVO);
        EvtTypeGuild.AddExp.happen(parent, cmd, playerVO, EvtParamType.GUILD.val(guildVO), EvtParamType.NUM.val(exp));
        return parent;
    }

    @Override
    public void clearGuildReq() {
        ArrayList<GuildReqVO> allReqs = CacheGuild.getAllReqs();
        long flushTime = System.currentTimeMillis() + DateUtil.HOUR_MILLIS;
        for (GuildReqVO req : allReqs) {
            Date createDate = req.getCreateDate();
            // 不处理 同意  可能玩家不在内存 只处理了请求数据
            if (GuildReqVO.statusUpdater.get(req) != GuildReqVO.STATUS_ACCEPT && createDate == null || createDate.getTime() > flushTime) {
                CacheGuild.removeReq(req);
                guildReqDAO.delete(req);
            }
        }
    }

    @Override
    public void checkInDissolutionGuild() {
        if (CacheGuild.inDissolutionGuild.size() > 0) {
            long disTime = System.currentTimeMillis();
            Set<GuildVO> copy = new HashSet<>(CacheGuild.inDissolutionGuild);
            List<GuildVO> collect = null;
            for (GuildVO guildVO : copy) {
                if (GuildVO.statusUpdater.get(guildVO) == GuildVO.IN_DISSOLUTION && guildVO.getDissolutionTime() <= disTime) {
                    if (collect == null) collect = GameUtil.createList();
                    collect.add(guildVO);
                }
            }
            if (null != collect && collect.size() > 0) {
                innerDissolutionGuild(collect);
            }
        }
    }

    @Override
    public void systemReset(Map<String, Object> ret, PlayerVO playerVO) {
        GuildMemberVO memberVO = getGuildMemberVO(playerVO);
        if (null == memberVO) return;
        if (SYSCons.notSameDate(System.currentTimeMillis(), memberVO.getLastRestTime())) {
            memberVO.setLastRestTime(System.currentTimeMillis());
            memberVO.setDonateCid(0);
            guildMemberDAO.save(memberVO);
            if (null != ret) {
                GuildVO guild = playerVO.getGuild();
                if (guild != null)
                    msgFactoryGuild.memberBaseInfo(ret, memberVO, guild.getMemberRights().get(playerVO.getId()));
            }
        }
    }

    @Override
    public void cmdReset(Map<String, Object> ret, PlayerVO playerVO) {
        GuildMemberVO memberVO = getGuildMemberVO(playerVO);
        if (null == memberVO) return;
        memberVO.setLastRestTime(System.currentTimeMillis());
        memberVO.setDonateCid(0);
        guildMemberDAO.save(memberVO);
        if (null != ret) {
            GuildVO guild = playerVO.getGuild();
            if (guild != null)
                msgFactoryGuild.memberUpdateMsg(ret, memberVO);
        }
    }

    @Override
    public void systemResetGuildData() {
        long cur = System.currentTimeMillis();
        if (SYSCons.notSameDate(systemBO.getRecordTime(GuildSysCons.GUILD_REF_DATA_TIME), cur)) {
            systemBO.recordTime(GuildSysCons.GUILD_REF_DATA_TIME, cur);
        }
        for (GuildVO guildVO : CacheGuild.getAllGuild()) {
            for (Integer playerId : guildVO.getMemberRights().keySet()) {
                GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(playerId);
                if(memberVO != null){
                    resetGuildMember(memberVO);
                }
            }
        }

    }

    @Override
    public void updateGuildFightForce() {
        Collection<GuildVO> allGuild = CacheGuild.getAllGuild();
        for (GuildVO guildVO : allGuild) {
            long ff = 0;
            for (int pid : guildVO.getMemberRights().keySet()) {
                PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
                if (null != baseInfo) {
                    ff += baseInfo.getFightForce();
                }
            }
            if (guildVO.getFightForce() != ff) {
                guildVO.setFightForce(ff);
                guildDAO.save(guildVO);
            }
        }
    }

    public void setMailBO(MailBO mailBO) {
        this.mailBO = mailBO;
    }
  

    public void setSystemBO(SystemBO systemBO) {
        this.systemBO = systemBO;
    }


    @Override
    public void changeGuildMny(CMD cmd, Map parent, PlayerVO player, GuildVO guildVO, long delta) {
        long before = guildVO.getMny();
        innerChgGuildMny(guildVO, delta);
        long after = guildVO.getMny();
        guildDAO.save(guildVO);
        EvtTypeGuild.MoneyChange.happen(GameUtil.createSimpleMap(), cmd, guildVO,
                EvtParamType.BEFORE.val(before),
                EvtParamType.AFTER.val(after),
                EvtParamType.CHANGE.val(delta));
        MsgFactoryGuild.instance().getGuildMoneyUpdateMsg(parent, guildVO);
    }

    private void innerChgGuildMny(GuildVO guildVO, long delta) {
        guildVO.setMny(guildVO.getMny() + delta);
    }




    @Override
    public void replaceGuildLeader() {
        for (GuildVO guildVO : CacheGuild.getAllGuild()) {
            int owner = guildVO.getOwner();
            PlayerInfoVO playerInfoVO = CachePlayerBase.getBaseInfo(owner);
            PlayerVO playerVO = CachePlayer.getPlayerByPid(owner);
            if(playerVO != null && playerVO.isOnline()) continue;
            //没有找到军团拥有者信息，则直接转让军团
            if(playerInfoVO != null &&
                    DateUtil.getDay(new Date(playerInfoVO.getLastLoginTime())) < CacheDisGuild.LeaderOfflineReplace.first()){
                continue;
            }
            GuildMemberVO memberVO = getReplaceGuildLeaderMember(guildVO);
            if(memberVO == null) continue;
            int tgtRight = guildVO.getMemberRights().get(memberVO.getPid());
            chgRight(guildVO, memberVO.getPid(), RightLevel.Level0);
            chgRight(guildVO, owner, RightLevel.Level1);
            PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(memberVO.getPid());
            if (null != baseInfo)
                guildLog(guildVO, GuildLogType.ChangeRight, playerInfoVO.getName(), baseInfo.getName(), String.valueOf(tgtRight));
            guildVO.setOwner(memberVO.getPid());
            notifyRightChg(memberVO.getPid(), RightLevel.Level0.getId());
            guildDAO.save(guildVO);
        }


    }

    private GuildMemberVO getReplaceGuildLeaderMember(GuildVO guildVO) {
        GuildMemberVO memberVO = null;
        for (Map.Entry<Integer, Integer> entry : guildVO.getMemberRights().entrySet()) {
            int pid = entry.getKey();
            GuildMemberVO guildMemberVO = CacheGuild.getGuildMemberVO(pid);
            if(guildMemberVO == null) continue;
            PlayerInfoVO infoVO = CachePlayerBase.getBaseInfo(pid);
            if(infoVO == null) continue;
            if(DateUtil.getDay(new Date(infoVO.getLastLoginTime())) >= CacheDisGuild.LeaderOfflineReplace.first()) continue;
            if(memberVO == null) {
                memberVO = guildMemberVO;
                continue;
            }
            if(memberVO.getHistoryGuildCoin() < guildMemberVO.getHistoryGuildCoin()){
                memberVO = guildMemberVO;
            }
        }
        return memberVO;
    }


    public void setActivityBO(ActivityBO activityBO) {
        this.activityBO = activityBO;
    }

    
    /**
     * 玩家登陆 加载数据
     */
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(player.getId());
        if (memberVO == null || memberVO.getGuildID() <= 0) {
            // 如果不在公会
            // 处理请求信息
            Map<Integer, GuildReqVO> req = CacheGuild.getReqByPid(player.getId());
            if (req != null && req.size() > 0) {
                for (GuildReqVO vo : req.values()) {
                    // 处理玩家离线的时候 被批准加入家族的情况  正常逻辑下这里只有一个
                    GuildVO guildVO = CacheGuild.getGuildByID(vo.getGuildID());
                    if (isMaxSize(guildVO)) {
                        continue;
                    }
                    if (GuildReqVO.statusUpdater.compareAndSet(vo, GuildReqVO.STATUS_ACCEPT, GuildReqVO.STATUS_DONE)) {
                        if (null != guildVO && guildVO.getStatus() != GuildVO.DISSOLUTION_END && !isMaxSize(guildVO)) {
                            directJoinGuild(GameUtil.createSimpleMap(), player.getId(), guildVO, RightLevel.Level3, cmd);
                            break;
                        }
                    }
                }
            }
        } else {
            // 如果在公会
            Collection<GuildReqVO> guildReqVOS = CacheGuild.removeReq(player.getId());
            if (guildReqVOS.size() > 0) guildReqDAO.delete(guildReqVOS);

            // 处理其他情况玩家所在家族被解散情况
            GuildVO guild = CacheGuild.getGuildByID(memberVO.getGuildID());
            // 家族解散了
            if (null == guild
                    || GuildVO.statusUpdater.get(guild) == GuildVO.DISSOLUTION_END
                    // 被踢了
                    || !guild.getMemberRights().containsKey(player.getId())) {
                memberVO.setGuildID(-1);
            }
            guildMemberDAO.save(memberVO);
        }

        memberVO = CacheGuild.getGuildMemberVO(player.getId());
        if (null == memberVO) {
            player.setGuildID(-1);
        } else {
            //重新校验处理玩家身上的GuildID
            player.setGuildID(memberVO.getGuildID());
        }
        playerDAO.save(player);

    }

    private void resetGuildMember(GuildMemberVO vo) {
        long ms = System.currentTimeMillis();
        if(SYSCons.notSameDate(ms, vo.getLastRestTime())) {
            vo.setLastRestTime(System.currentTimeMillis());
            vo.getUsedWorker().clear();
            vo.setDonateCid(0);
            guildMemberDAO.save(vo);
        }
    }
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	public RankBO getRankBO() {
		return rankBO;
	}

	public void setRankBO(RankBO rankBO) {
		this.rankBO = rankBO;
	}

    public void setGoodsBO(GoodsBO goodsBO) {
        this.goodsBO = goodsBO;
    }

    public void setDepotBO(DepotBO depotBO) {
        this.depotBO = depotBO;
    }
}
