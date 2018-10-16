package com.cellsgame.game.module.player.bo.impl;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.common.util.collection.Filter;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.common.util.http.HttpUtil;
import com.cellsgame.game.cache.*;
import com.cellsgame.game.cons.*;
import com.cellsgame.game.context.DefaultPlayerConfig;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.CatchRunnable;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.core.socket.CacheMessageController;
import com.cellsgame.game.module.BuildPlayerProcess;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.LoadPlayerJobFactory;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.DailyResetable;
import com.cellsgame.game.module.card.bo.CardBO;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.bo.impl.DepotBOImpl;
import com.cellsgame.game.module.depot.bo.impl.EventDepotBOImpl;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.cons.DepotType;
import com.cellsgame.game.module.depot.msg.CodeDepot;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.func.vo.FixedDropVO;
import com.cellsgame.game.module.goods.ModuleInfo;
import com.cellsgame.game.module.goods.bo.GoodsBO;
import com.cellsgame.game.module.goods.cons.GoodsConstant;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.hero.bo.impl.HeroBOImpl;
import com.cellsgame.game.module.hero.csv.HeroCfg;
import com.cellsgame.game.module.player.PlayerUtil;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.*;
import com.cellsgame.game.module.player.cons.*;
import com.cellsgame.game.module.player.csv.CheckInPrizeConfig;
import com.cellsgame.game.module.player.csv.LoginPrizeConfig;
import com.cellsgame.game.module.player.csv.PlayerLevelConfig;
import com.cellsgame.game.module.player.csv.VipLevelConfig;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.msg.MsgFactoryPlayer;
import com.cellsgame.game.module.player.vo.*;
import com.cellsgame.game.module.shop.csv.ShopConfig;
import com.cellsgame.game.module.sys.msg.CodeSystem;
import com.cellsgame.game.util.LoginVerifyUtil;
import com.cellsgame.game.util.StringVerifyUtil;
import com.cellsgame.game.util.CmdTriFunction;
import com.cellsgame.game.util.CmdTriFunctionEx;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.ChainLoadFinisher;
import com.cellsgame.orm.DBObj;
import com.cellsgame.orm.LoadDBObjChainJob;
import com.cellsgame.orm.LoadVOJob;
import com.sun.org.apache.regexp.internal.RE;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.swing.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class PlayerBOImpl implements PlayerBO{
    private static final Logger log = LoggerFactory.getLogger(PlayerBOImpl.class);
    private static final Charset GBK = Charset.forName("GBK");
    @Resource
    private  HeroBOImpl heroimpl;
    @Resource
    private BaseDAO<PlayerVO> playerDAO;
//    @Resource
//    private BaseDAO<FixedDropVO> fixedDropDAO;
//    @Resource
//    private BaseDAO<CheckInVO> checkInDAO;
    @Resource
    private BuildPlayerProcess playerBuilder;
    private DefaultPlayerConfig defaultPlayerConfig;
    private GameConfig gameConfig;
    private CardBO cardBO;
    private GoodsBO goodsBO;
	private Collection<DailyResetable> systemResetables;


	 //TODO 测试对象
     private  GoodsVO gvo;

    public void setGoodsBO(GoodsBO goodsBO) {
        this.goodsBO = goodsBO;
    }



    public void setCardBO(CardBO cardBO) {
        this.cardBO = cardBO;
    }

    public void setDefaultPlayerConfig(DefaultPlayerConfig defaultPlayerConfig) {
        this.defaultPlayerConfig = defaultPlayerConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }


    @Override
    public void enterGame(MessageController controller, GameMessage message, CMD cmd) throws LogicException {
        String token = message.getToken();
        log.debug("enter game token = {}", token);
        PlayerVO player = CachePlayer.getPlayerByToken(token);
        if (player != null) {
            log.debug(" ##### reconnection enterGame:[{}] session id:{}", player.getName(), controller.getLastClientMessageId());
            reconnection(controller, message, player);
            return;
        }
        // 强制标记服务器ID
//        String serverId = "hx001";
        Integer serverId = controller.getAttribute(SessionAttributeKey.SERVER_ID);
        String accountId = controller.getAttribute(SessionAttributeKey.ACCOUNT_ID);
        CodeGeneral.General_NotLogin.throwIfTrue(serverId == null || serverId <= 0 || StringUtil.isEmpty(accountId));
        player = CachePlayer.getPlayerByAccount(serverId, accountId);
        if (player == null) {
            log.debug("# LOAD PLAYER : accountId = {}, serverId = {}", accountId, serverId);
            // 强制使用某玩家数据
//            String pid = CachePlayerBase.getPIDByPname("天行者伯特");
//            LoadPlayerJobFactory.loadByPlayerId(pid, data ->
//                    Dispatch.dispatchGameLogic(
//                            () -> loadSuc(controller, message, cmd, serverId, accountId, data)));
            //
            LoadPlayerJobFactory.loadByServerIdAndAccountId(accountId, serverId, new ChainLoadFinisher() {
                @Override
                public void finishLoad(Map<String, Object> data) {
                    Dispatch.dispatchGameLogic(
                            new Runnable() {
                                @Override
                                public void run() {
                                    loadSuc(controller, message, cmd, serverId, accountId, data);
                                }
                            });
                }
            });

        } else {
            this.enterGame(controller, message, player, cmd);
        }
    }

    private void enterGame(MessageController controller, GameMessage message, PlayerVO player, CMD cmd) throws LogicException {
        if (player.isOnline()) {
            log.debug(" ##### Player_ReplaceLogin enterGame:[{}] session id:{}", player.getName(), controller.getLastClientMessageId());
            MessageController oldController = player.getMessageController();
            Push.multiThreadSend(oldController, MsgUtil.brmAll(Command.Player_ReplaceLogin),
                    new Runnable() {
                        @Override
                        public void run() {
                            oldController.sendCloseMesage();
                            oldController.destroy();
                        }
                    });
            CachePlayer.offline(player);
        }
        if (PlayerState.FROZEN.check(player)) {
            Push.multiThreadSend(controller, MsgUtil.brmAll(cmd.getCmd(), CodePlayer.Player_Frozen));
            return;
        }
        log.debug(" player enterGame:[{}] session id:{}", player.getName(), controller.getLastClientMessageId());
        controller.setAttribute(SessionAttributeKey.PLAYER_ID, player.getId());
        player.setToken(message.getToken());
        player.setMessageController(controller);
        long lastLoginDate = player.getLoginDate();
        player.setLoginDate(System.currentTimeMillis());
        player.getLastChatTime().clear();
        CachePlayer.online(player);
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.ALL);
        // 玩家进入游戏事件
        EvtTypePlayer.EnterGame.happen(GameUtil.createSimpleMap(), cmd, player, EvtParamType.SAME_DATE.val(!SYSCons.notSameDate(lastLoginDate ,player.getLoginDate())));
        // 保存玩家数据
        playerDAO.save(player);
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.lastLoginTime);
        //向玩家推送角色本身的信息
        Map result = MsgUtil.brmAll(MsgFactoryPlayer.instance().getPlayerMessage(player), cmd);
        Push.multiThreadSend(controller, result);
    }

    /**
     * 系统重置玩家数据。
     * <p>
     * 调用情况：
     * <p>
     * 1、系统周期性调用，如每日固定时间调用。
     * <p>
     * 2、进入游戏时检测是否超过系统调用时间，是则调用。
     *
     * @param pvo 玩家数据
     */
    @Override
    public void dailyReset(CMD cmd, Map parent, PlayerVO pvo, long ms) {
	      //
//	      pvo.setActiveness(0);
//	      EvtTypePlayer.ACTIVENESS.happen(parent, cmd, pvo, EvtParamType.AFTER.val(pvo.getActiveness()+0L));
//          pvo.setWorship(false);
//	      //
//	      pvo.getActivenessPrized().clear();
//	      //
	      pvo.setResetTime(ms);
    }

    /**
     * @see com.cellsgame.game.module.DailyResetable#dailyResetable(com.cellsgame.game.module.player.vo.PlayerVO)
     */
    @Override
    public boolean dailyResetable(PlayerVO pvo) {
    	return true;
    }

	/**
	 * @see com.cellsgame.game.module.DailyResetable#lastDailyResetTime(com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public long lastDailyResetTime(PlayerVO pvo) {
		return pvo.getResetTime();
	}



    private int getCritMultiple(int[] rate, int[] critMultiple) {
        int random = GameUtil.r.nextInt(10000);
        int multiple = 1;
        for (int j = 0; j < rate.length; j++) {
            if (random >= rate[j])
                multiple = critMultiple[j];
        }
        return multiple;
    }

    @Override
    public Map checkIn(PlayerVO player, CMD cmd) throws LogicException {
        //
        CheckInVO checkInVO = player.getCheckInVO();
        // 如果今日已签到
        CodePlayer.Already_Check_In.throwIfTrue(checkInVO.isChecked());
        // 查看累计奖励
        CheckInPrizeConfig checkInPrizeConfig = CachePrizes.CHECK_IN_PRIZE_CONFIG.get(checkInVO.getCheckInDaysPerMonth() + 1);
        // 如果没有签到奖励
        CodePlayer.No_Check_In_Prize.throwIfTrue(checkInPrizeConfig == null);
        // 累计签到次数
        checkInVO.setCheckInDaysPerMonth(checkInVO.getCheckInDaysPerMonth() + 1);
        // 签到时间
        checkInVO.setLastCheckInTime(System.currentTimeMillis());
        //
//        checkInDAO.save(player.getCheckInVO());
        // 结果
        Map ret = GameUtil.createSimpleMap();
        // 奖励数据
        Map prizeMap = GameUtil.createSimpleMap();
        // 发放奖励
        FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(checkInPrizeConfig.getPrizes()).exec(ret, prizeMap, player);
        //
        ret = MsgFactoryPlayer.instance().getCheckInUpdateMsg(ret, player);
        //
        MsgFactoryPlayer.instance().addPrizeMsg(ret, prizeMap);
        EvtTypePlayer.CheckIn.happen(ret, cmd, player);
        //
        return MsgUtil.brmAll(ret, Command.Player_CHECK_IN);
    }

    @Override
    public Map getTotalCheckInPrize(PlayerVO player, CMD cmd, @CParam("ds") int days) throws LogicException {
        // 查看当天可以领取的奖励
        LoginPrizeConfig loginPrizeConfig = CachePrizes.LOGIN_PRIZE_CONFIG_MAP.get(days);
        // 如果没有, 或者不够次数
        CodePlayer.No_Login_Prize.throwIfTrue(loginPrizeConfig == null || days > player.getCheckInVO().getCheckInDaysPerMonth());
        // 查看是否已领取
        CodePlayer.Login_Prized.throwIfTrue(player.getCheckInVO().getTotalCheckInPrized().contains(days));
        // 结果
        Map ret = GameUtil.createSimpleMap();
        // 奖励数据
        Map prizeMap = GameUtil.createSimpleMap();
        // 发放奖励
        FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(loginPrizeConfig.getPrizes()).exec(ret, prizeMap, player);
        // 记录奖励历史
        player.getCheckInVO().getTotalCheckInPrized().add(days);
        //
        ret = MsgFactoryPlayer.instance().getCheckInUpdateMsg(ret, player);
        //
        MsgFactoryPlayer.instance().addPrizeMsg(ret, prizeMap);
        //
//        checkInDAO.save(player.getCheckInVO());
        // 返回
        return MsgUtil.brmAll(ret, Command.Player_GET_TOTAL_CHECK_IN_PRIZE);
    }

    public static Map<String, Object> PI_Message = MsgUtil.brmAll(Command.Player_PI);

    @Override
    public Map pi(PlayerVO player, CMD cmd) throws LogicException {
        CachePlayerPI.recordPI(player, cmd.getTime());
        return PI_Message;
    }


    @Override
    public Map serverTime(PlayerVO player, CMD cmd) throws LogicException {
        Map<String, Object> result = GameUtil.createSimpleMap();
        result.put(MsgFactory.TIME, System.currentTimeMillis());
        return MsgUtil.brmAll(result, cmd);
    }



    @Override
    public Map revFirstPayPrize(PlayerVO player, CMD cmd) throws LogicException {
        CodePlayer.Player_FirstPayNotFinish.throwIfTrue(FirstPayPrizeState.NotFinish.check(player));
        CodePlayer.Player_FirstPayAlreadyRev.throwIfTrue(FirstPayPrizeState.Prizeed.check(player));
        int prizeCid = CacheDisDataPlayer.FirstPayPrize.first();
        CodePlayer.Player_FirstPayNotFindConfig.throwIfTrue(prizeCid <= 0);
        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
        exec = goodsBO.addGoodsFuncs(exec, player, prizeCid);
        Map result = GameUtil.createSimpleMap();
        Map prizeMap = GameUtil.createSimpleMap();
        exec.exec(result, prizeMap, player);
        MsgFactoryPlayer.instance().getFirstPayPrizeMsg(result, prizeMap);
        FirstPayPrizeState.Prizeed.setState(player);
        playerDAO.save(player);
        MsgFactoryPlayer.instance().getFirstPayPrizeStateMsg(result, player);
        return MsgUtil.brmAll(result, cmd.getCmd());
    }

    @Override
    public Map upLevel(PlayerVO player, CMD cmd) throws LogicException {
        PlayerLevelConfig[] configs = PlayerLevelConfig.By_Level;
//        CodePlayer.Player_Level_Limit.throwIfTrue(player.getLevel() >= configs.length - 1);
//        PlayerLevelConfig config = configs[player.getLevel() + 1];
//        CodePlayer.Player_NotFindConfig.throwIfTrue(config == null);
//        int cost = config.getReqExp();
//        CodePlayer.Player_Exp_Minus.throwIfTrue(player.getExp() < cost);
        Map result = GameUtil.createSimpleMap();
//        changeExp(result, player, -cost, cmd);
//        player.setLevel(player.getLevel() + 1);
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
//        executor.addSyncFunc(config.getUpLevelPrize());
        Map prize = GameUtil.createSimpleMap();
        executor.exec(result, prize, player);
        playerDAO.save(player);
        MsgFactoryPlayer.instance().getPrizeMsg(result, prize);
        MsgFactoryPlayer.instance().getPlayerLevelUpdateMsg(result, player);
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.PLV);
//        EvtTypePlayer.LevelUp.happen(result, cmd, player, EvtParamType.AFTER.val(player.getLevel()+0L));
        return MsgUtil.brmAll(result, cmd.getCmd());
    }

    @Override
    public Map randomName(MessageController controller, CMD cmd, boolean male) throws LogicException {
        // 如果之前有随机结果
        String name = controller.getAndRemoveAttribute(SessionAttributeKey.Random_Name_Key);
        // 如果有
        if (StringUtils.isNotEmpty(name)){ CacheName.release(name);}
        // 重新随机
        name = CacheName.random(male);
        // 保存结果
        controller.setAttribute(SessionAttributeKey.Random_Name_Key, name);
        // 返回结果
        return MsgUtil.brmAll(MsgFactoryPlayer.instance().getRandomNameMsg(null, name), Command.Player_Random_Name);
    }




    @Override
    public void verifySign(MessageController controller, GameMessage message, String serverId, String sign) throws LogicException {
        System.out.println("进入20001  消息中================");
        final int sId = Integer.parseInt(serverId);
        log.info("verifySign serverId : {}, token : ", serverId, message.getToken());
        Dispatch.dispatchHttpLogic(new Runnable() {
            @Override
            public void run() {
                if (gameConfig.isNewLogin()) {
                    newVerify(controller, message, sId, sign);
                } else {
                    verify(controller, message, sId, sign);
                }
            }
        });
    }

    private void verify(MessageController controller, GameMessage message, Integer serverId, String sign) {
        try {
            log.debug("verifySign url : {}", gameConfig.getSignVerifyUrl());
            Map<?, ?> result = HttpUtil.postRequest(gameConfig.getSignVerifyUrl(), new Object[]{gameConfig.getAppId(), gameConfig.getAppCharacter(), sign});
            int code = (Integer) result.get(LoginServerResultKey.FROM_LOGIN_CODE);
            if (code == CodeSystem.Suc.getCode()) {
                Map<?, ?> data = (Map<?, ?>) result.get(LoginServerResultKey.FROM_LOGIN_DATA);
                //玩家登陆成功
                String accountId = (String) data.get(LoginServerResultKey.FROM_LOGIN_ACCOUNTID);
                String token = (String) data.get(LoginServerResultKey.FROM_LOGIN_TOKEN);
                Dispatch.dispatchGameLogic(new Runnable() {
                    @Override
                    public void run() {
                        verifySuc(controller, message, serverId, accountId, token, 1);
                    }
                });
            } else {
                Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_VerifyTokenFail));
            }
        } catch (Exception e) {
            log.error("on Exception ", e);
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_ServerException));
        }
    }

    private void newVerify(MessageController controller, GameMessage message, Integer serverId, String sign) {
        try {
            log.info("verifySign url : {}", gameConfig.getSignVerifyUrl());
            LoginVerifyUtil.VerifyResponse result = LoginVerifyUtil.doPost(gameConfig.getSignVerifyUrl(), gameConfig.getAppId(), gameConfig.getAppCharacter(), sign);
            log.info("newVerify result.getCo() : {}", result.getCo());
            if (result.getCo() == CodeSystem.Suc.getCode()) {
                LoginVerifyUtil.Attach attach = result.getAttach();
                if (attach == null) {
                    Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_LoginReturnAttachException));
                    return;
                }
                if (CacheServerState.isClose()) {
                    if(attach.getAdmin() < 1){
                        String clientIp = controller.getAttribute(SessionAttributeKey.CLIENT_IP);
                        if(!CacheIP.allow.isEmpty()){
                            log.debug(">>> check ip , clientIp = {}", clientIp);
                            log.debug(">>> allow ip  = {}", Arrays.toString(CacheIP.allow.toArray()));
                            if(!CacheIP.allow.contains(clientIp)){
                                Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_ServerNotOpen));
                                return;
                            }
                        }else {
                            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_ServerNotOpen));
                            return;
                        }
                    }
                }
                String accountId = attach.getAccountId();
                String token = attach.getToken();
                Dispatch.dispatchGameLogic(new Runnable() {
                    @Override
                    public void run() {
                        verifySuc(controller, message, serverId, accountId, token, attach.getAdmin());
                    }
                });
            } else {
                Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_VerifySignFail));
            }
        } catch (Exception e) {
            log.error("on Exception ", e);
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_ServerException));
        }
    }

    @Override
    public void verifySuc(MessageController controller, GameMessage message, Integer serverId, String accountId, String token, int admin) {
        try {
            log.debug("Verify : {}", accountId);
            CodeGeneral.General_VerifyTokenFail.throwIfTrue(StringUtil.isEmpty(token) || !token.equals(message.getToken()));
            //部分数据保存值session
            controller.setAttribute(SessionAttributeKey.ADMIN, admin);
            controller.setAttribute(SessionAttributeKey.SERVER_ID, serverId);
            controller.setAttribute(SessionAttributeKey.ACCOUNT_ID, accountId);
            controller.setAttribute(SessionAttributeKey.TOKEN, message.getToken());
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd()));
        } catch (LogicException e) {
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), e.getCode()));
        }
    }

    @Override
    public void loadSuc(MessageController controller, GameMessage message, CMD cmd, Integer serverId, String accountId, Map<String, ?> data) {
        log.debug("loadSuc : serverId :{} accountID:{} ", serverId, accountId);
        try {
            PlayerVO player = load(data, cmd);
            CodePlayer.Player_NotFindPlayer.throwIfTrue(player == null);
            EvtTypePlayer.LoadSuc.happen(GameUtil.createSimpleMap(), cmd, player);
            this.enterGame(controller, message, player, cmd);
        } catch (LogicException e) {
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), e.getCode()));
        } catch (Throwable e) {
            log.error("", e);
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_ServerException));
        }
    }

    @Override
    public PlayerVO load(Map<String, ?> data, CMD cmd) throws LogicException {
        DBObj dbObj = (DBObj) data.get(IBuildData.DATA_SIGN_PLAYER);
        if (dbObj != null) {
            PlayerVO player = CachePlayer.getPlayerByPid((int) dbObj.getPrimaryKey());
            if (player == null) {
                player = new PlayerVO();
                player.readFromDBObj(dbObj);
                playerBuilder.build(player, cmd, data);
                player.getAtt().init(GameUtil.createSimpleMap(), cmd, player);
                CachePlayer.addPlayer(player);
            }
            return player;
        }
        return null;
    }





    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void loadPlayerAndExecBiz(CMD cmd, PlayerVO pvo, int tgtPid, CmdTriFunctionEx<Map, PlayerVO, PlayerVO, Map> biz, Object ... params) {
        LoadPlayerJobFactory factory = (LoadPlayerJobFactory) SpringBeanFactory.getBean("loadPlayerJobFactory", tgtPid);
        ChainLoadFinisher finisherJob = new ChainLoadFinisher() {
			@Override
			public void finishLoad(Map<String, Object> data) {
				 Dispatch.dispatchGameLogic(new CatchRunnable(pvo, cmd) {
                     @Override
                     protected Map<String, Object> runLogic() throws LogicException {
                         PlayerVO tgtPvo = load(data, cmd);
                         CodePlayer.Player_NotFindPlayer.throwIfTrue(tgtPvo == null);
                         Map ret = biz.apply(cmd, GameUtil.createSimpleMap(), pvo, tgtPvo, params);
                         // 通知数据加载成功
                         return MsgUtil.brmAll(ret,cmd);
                     }
                 });
			}
		};
        factory.setFinisher(finisherJob);
        LoadDBObjChainJob cljob = factory.createLoadByPidJob();
        cljob.load((ConcurrentHashMap) GameUtil.createMap());
    }



    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void loadPlayerAndExecBiz(CMD cmd, PlayerVO pvo, int tgtPid, CmdTriFunction<Map, PlayerVO, PlayerVO, Map> biz) {
        LoadPlayerJobFactory factory = (LoadPlayerJobFactory) SpringBeanFactory.getBean("loadPlayerJobFactory", tgtPid);
        ChainLoadFinisher finisherJob = new ChainLoadFinisher() {
			@Override
			public void finishLoad(Map<String, Object> data) {
				 Dispatch.dispatchGameLogic(new CatchRunnable(pvo, cmd) {
                     @Override
                     protected Map<String, Object> runLogic() throws LogicException {
                         PlayerVO tgtPvo = load(data, cmd);
                         CodePlayer.Player_NotFindPlayer.throwIfTrue(tgtPvo == null);
                         Map ret = biz.apply(cmd, GameUtil.createSimpleMap(), pvo, tgtPvo);
                         // 通知数据加载成功
                         return MsgUtil.brmAll(ret,cmd);
                     }
                 });
			}
		};
        factory.setFinisher(finisherJob);
        LoadDBObjChainJob cljob = factory.createLoadByPidJob();
        cljob.load((ConcurrentHashMap) GameUtil.createMap());
    }


    /**
     * 玩家经验修改
     *
     * @param player   玩家数据
     * @param quantity 数量
     * @return 添加经验结果
     */
    @Override
    public Map changeExp(Map parent, PlayerVO player, long quantity, CMD cmd) throws LogicException {
//        int old = player.getExp();
//        int newVal = (int) (old + quantity);
//        CodePlayer.Player_Exp_Minus.throwIfTrue(newVal < 0);
//        player.setExp(newVal);
        playerDAO.save(player);
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.EXP);
        return MsgFactoryPlayer.instance().getPlayerExpUpdateMsg(parent, player);
    }

    @Override
    public Map changeVipExp(Map parent, PlayerVO player, long quantity, CMD cmd) throws LogicException {
//        int old = player.getVipExp();
//        int newVal = (int) (old + quantity);
//        CodePlayer.Player_VipExp_Minus.throwIfTrue(newVal < 0);
//        player.setVipExp(newVal);
        checkVipUpLevel(cmd, parent, player);
        playerDAO.save(player);
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.VIP);
        return MsgFactoryPlayer.instance().getPlayerVipUpdateMsg(parent, player);
    }

    private void checkVipUpLevel(CMD cmd, Map parent, PlayerVO player) {
//        if(player.getVip() >= VipLevelConfig.Configs.length - 1) return;
//        VipLevelConfig config = VipLevelConfig.Configs[player.getVip()];
//        while (player.getVipExp() >= config.getExp()){
//            player.setVip(player.getVip() + 1);
//            player.setVipExp(player.getVipExp() - config.getExp());
//            config = VipLevelConfig.Configs[player.getVip()];
//        }
//        EvtTypePlayer.VipLevelUp.happen(parent, cmd, player, EvtParamType.AFTER.val(player.getVip() + 0L));
    }

    @Override
    public Map changeVip(Map parent, PlayerVO player, int vip, CMD cmd) throws LogicException {
//        player.setVip(vip);
        playerDAO.save(player);
        EvtTypePlayer.VipLevelUp.happen(parent, cmd, player, EvtParamType.AFTER.val(vip+0L));
        return MsgFactoryPlayer.instance().getPlayerVipUpdateMsg(parent, player);
    }

    private void checkPlayerName(String name) throws LogicException {
        checkLen(name, 2, 14);
        CodePlayer.Player_NameFormationError.throwIfTrue(!StringVerifyUtil.isPATTERN_ABC_1(name));
        CodePlayer.Player_NameAlreadyUse.throwIfTrue(CacheName.isUsed(name));
    }


    private void checkLen(String name, int min, int max) throws LogicException {
        CodePlayer.Player_NameIsNull.throwIfTrue(StringUtil.isEmpty(name));
        byte[] gbks = name.getBytes(GBK);
        CodePlayer.Player_NameFormationError.throwIfTrue(gbks.length < min);
        CodePlayer.Player_NameFormationError.throwIfTrue(gbks.length > max);
        CodePlayer.Player_NameFormationError.throwIfTrue(CacheWord.haveKeyWord(name));
        CodePlayer.Player_NameFormationError.throwIfTrue(CacheWord.haveDirtyWord(name));
    }
    @Override
    public void createPlayer(MessageController controller, GameMessage message, @CParam("playerName") String cliName, int img, CMD cmd) throws LogicException {
        System.out.println("20003=====================");
        String playerName = cliName.trim();
        //TODO 测试注释
        //Integer serverId = controller.getAttribute(SessionAttributeKey.SERVER_ID);
        Integer serverId = 20180828;
        //TODO 测试注释
        //String accountId = controller.getAttribute(SessionAttributeKey.ACCOUNT_ID);
        String accountId="wqerqewr";
        String random = controller.getAndRemoveAttribute(SessionAttributeKey.Random_Name_Key);
        CodeGeneral.General_NotLogin.throwIfTrue(serverId == null || serverId <= 0 || StringUtil.isEmpty(accountId));
        //TODO 测试注释
        //checkPlayerName(playerName);
        PlayerVO player = new PlayerVO();
        player.setAccountId(accountId);
        player.setServerId(serverId);
        player.setName(playerName);

//        player.setImage(img);
        player.writeToDBObj();
        FixedDropVO fixedDropVO = new FixedDropVO();
        fixedDropVO.setPid(player.getId());
        player.setFixedDropVO(fixedDropVO);
//        fixedDropDAO.save(fixedDropVO);
        //创建玩家初始英雄
        heroimpl= SpringBeanFactory.getBean(HeroBOImpl.class);
        heroimpl.createHeroTest(player, CacheConfig.getCfg(HeroCfg.class,20122201));

        //TODO 测试道具添加



        // 保存玩家数据
        playerDAO.save(player);
        playerBuilder.buildAsCreate(player, cmd);

        Map info = GameUtil.createSimpleMap();
        player.getAtt().init(info, cmd, player);
//        PlayerLevelConfig levelConfig = PlayerLevelConfig.By_Level[player.getLevel()];
//        if(levelConfig != null){
//            FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
//            executor.addSyncFunc(levelConfig.getUpLevelPrize());
//            executor.exec(info, player);
//        }
        CachePlayer.addPlayer(player);
        CachePlayerBase.addBaseInfo(player);
        // 如果是使用的随机名字
        if (playerName.equals(random) || StringUtils.isNotEmpty(random)) CacheName.release(random);
        EvtTypePlayer.CreatePlayer.happen(info, cmd, player);
//        EvtTypePlayer.LevelUp.happen(info, cmd, player, EvtParamType.AFTER.val(player.getLevel()+0L));
        this.enterGame(controller, message, player, cmd);

    }



   /* public void addGoodsEntity(PlayerVO pvo, GoodsVO gvo, CMD cmd) throws LogicException {
        DepotVO depotVO = pvo.getDepotVO();
        if (!checkSpace(gvo.getClass(), depotVO))
            return parent;
        DepotType depotType = Enums.get(DepotType.class, gvo.getClass());
        Map<Integer, GoodsVO> voMap = depotVO.getGoodsEntities().row(depotType);
        CodeDepot.Depot_Already_In_Depot.throwIfTrue(voMap.containsKey(gvo.getGoodsIx()));
        voMap.put(gvo.getGoodsIx(), gvo);
        // 如果当前物品ID比历史最大ID大，则物品是新增
        if (gvo.getGoodsIx() > depotVO.getMaxGoodsIx()) depotVO.count(gvo, 1);
        // 记录最大物品ID
        depotVO.setMaxGoodsIx(Math.max(gvo.getGoodsIx(), depotVO.getMaxGoodsIx()));
        // 保存背包数据
        depotDAO.save(depotVO);
*/




    @Override
    public Map<String, Object> queryPlayerByPID(MessageController controller, CMD cmd, int pid) throws LogicException {
        PlayerVO playerVO = CachePlayer.getPlayerByPid(pid);
        Map ret = GameUtil.createSimpleMap();
        if(playerVO != null){
            ret = MsgFactoryPlayer.instance().getPlayerQueryMsg(ret, playerVO);
            return MsgUtil.brmAll(ret, cmd.getCmd());
        }else{
            LoadPlayerJobFactory.loadByPlayerId(pid, new ChainLoadFinisher() {
                @Override
                public void finishLoad(Map<String, Object> data) {
                    PlayerBO playerBO = SpringBeanFactory.getBean(PlayerBO.class);
                    PlayerVO player = playerBO.load(data, CMD.system.now());
                    if(player == null){
                        Push.multiThreadSend(controller, MsgUtil.brmAll(cmd.getCmd(), CodePlayer.Player_NotFindPlayer));
                    }else{
                        Map ret = MsgFactoryPlayer.instance().getPlayerQueryMsg(GameUtil.createSimpleMap(), player);
                        Push.multiThreadSend(controller, MsgUtil.brmAll(ret, cmd.getCmd()));
                    }
                }
            });
        }
        return null;
    }

    @Override
    public void reconnection(MessageController controller, GameMessage message, PlayerVO player) throws LogicException {
        int admin = 0;
        if (player.getMessageController() != null) {
            Integer a = player.getMessageController().getAttribute(SessionAttributeKey.ADMIN);
            if (a != null) admin = a.intValue();
        }
        if (CacheServerState.isClose() && admin < 1) {
            Push.multiThreadSend(controller, MsgUtil.brmAll(message.getCmd(), CodeGeneral.General_ServerNotOpen));
            return;
        }
        String token = message.getToken();
        log.debug("reconnection >>>>>>>> token:{}", token);
        CodePlayer.Player_ReconnectionTokenError.throwIfTrue(player == null);
        CodePlayer.Player_ReconnectionNotOnline.throwIfTrue(player.getMessageController() == null);
        controller = CacheMessageController.reconnection(controller, player.getMessageController());
        player.setMessageController(controller);
        Integer serverId = controller.getAttribute(SessionAttributeKey.SERVER_ID);
        String accountId = controller.getAttribute(SessionAttributeKey.ACCOUNT_ID);
        log.debug(" reconnection >>>>>>>> serverid : {}  accountId:[{}]", serverId, accountId);
        Map<String, Object> result = MsgFactoryPlayer.instance().getPlayerMessage(player);
        result.put("srvMsg", controller.getAllServerMessage());
        Push.multiThreadSend(controller, MsgUtil.brmAll(result, message.getCmd()));
    }


    @Override
    public void offline(MessageController controller) {
        Dispatch.tryDispatch(DispatchType.GAME, new Runnable() {
            @Override
            public void run() {
                log.debug("player offline ... ");
                Integer playerId = controller.getAttribute(SessionAttributeKey.PLAYER_ID);
                if (playerId == null) {
                    CacheMessageController.destroy(controller);
                    return;
                }
                PlayerVO player = CachePlayer.getPlayerByPid(playerId);
                if (null == player) {
                    CacheMessageController.destroy(controller);
                    return;
                }
                offline(player, CMD.system.now());

            }
        });
    }

    @Override
    public void offline(PlayerVO player, CMD cmd) {
        if (player == null)
            return;
        log.debug("offline player :[{}] toke: {}", player.getName(), player.getToken());
        CachePlayer.offline(player);
        MessageController controller = player.getMessageController();
//        player.getBehaviorController().destroy();
//        player.setBehaviorController(null);
        player.setLogoutDate(System.currentTimeMillis());
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.lastLogOutTime);
        EvtTypePlayer.Offline.happen(GameUtil.createSimpleMap(), cmd, player);
        playerDAO.save(player);
        CacheMessageController.destroy(controller);
        player.setMessageController(null);
    }


    @Override
    public void init() {
        List<DBObj> list = playerDAO.getAll();
        PlayerVO player;
        for (DBObj dbObj : list) {
            player = new PlayerVO();
            player.readFromDBObj(dbObj);
            // TODO add by luojf945
            CachePlayer.addPlayer(player);
            CachePlayerBase.addBaseInfo(player);
        }
        CacheName.afterLoadAll();

    }



    @Override
    public void save(PlayerVO player) {
        playerDAO.save(player);
    }
@Override
    public List<PlayerVO> loadAll() {
        List<DBObj> dbObjs = playerDAO.getAll();
        List<PlayerVO> list = GameUtil.createList();
        for (DBObj dbObj : dbObjs) {
            PlayerVO player = new PlayerVO();
            player.readFromDBObj(dbObj);
            list.add(player);
        }
        return list;
    }



    private void saveAll(PlayerVO holder) {
        playerDAO.save(holder);
    }



    /**
     * 玩家升级事件处理
     *
     * @param playerVO 玩家数据
     */
    private void onPlayerLevelUp(PlayerVO playerVO, CMD cmd) {

    }

    /**
     * 玩家进入游戏之后处理
     * <p>
     * 1、检测是否有每日数据需要重置。
     *
     * @param playerVO 玩家数据。
     */
    private void onPlayerEnterGame(PlayerVO playerVO, CMD cmd) {
        //TODO 全局注释，是自己注释，后面一句注释是原本注释
        /*refFuncTimes(null, playerVO, System.currentTimeMillis());
        // 还未曾刷新
        // 已超过系统刷新时间
        // 是否需要刷新

        // 如果需要重置
        Map ret = GameUtil.createSimpleMap();
    	if(systemResetables == null){
    	    Map<String, DailyResetable> resetables = SpringBeanFactory.getBeanByType(DailyResetable.class);
    	    if(resetables!=null){
    	    	systemResetables = resetables.values();
    	    }
    	}
    	if(systemResetables!=null){
    		for (DailyResetable r : systemResetables) {
				r.reset(cmd, ret, playerVO);
			}
    	}


        // 签到
        CheckInVO checkInVO = playerVO.getCheckInVO();
        // 如果没有
        if (checkInVO == null) playerVO.setCheckInVO(checkInVO = new CheckInVO());
        //
        checkInVO.setPlayer(playerVO.getId());
        // 如果有签到时间, 当前时间与上一次签到时间不在同一月
        if (checkInVO.getLastCheckInTime() > 0 && SYSCons.isNotSameMonth(checkInVO.getLastCheckInTime())) {
            // 重置签到次数
            checkInVO.setCheckInDaysPerMonth(0);
            //
            checkInVO.getTotalCheckInPrized().clear();
        }*/
        //
//        checkInDAO.save(checkInVO);
    }


    @Override
    public Map<String, Object> recordStory(CMD cmd, PlayerVO player, int plotId)
            throws LogicException {
        Set<Integer> plotIds = player.getPlotIds();
        Map<String, Object> result = null;
        if (!plotIds.contains(plotId)) {
            plotIds.add(plotId);
            result = MsgFactoryPlayer.instance().getAddStoryIdMessage(null, plotId);
            EvtTypePlayer.FinishPlot.happen(result, cmd, player, EvtParamType.PLOT_ID.val(plotId));
        }
        return MsgUtil.brmAll(result, cmd.getCmd());
    }



    @Override
    public void onShutDown() {
        List<PlayerVO> onlinePlayers = new ArrayList<>(CachePlayer.getOnlinePlayers());
        for (PlayerVO playerVO : onlinePlayers) {
            offline(playerVO, CMD.system.now());
        }
        playerDAO.save(onlinePlayers);
    }


    @Override
    public Map<String, Object> changeFirstPayPrizeState(Map<String, Object> parent, PlayerVO player) {
        if(FirstPayPrizeState.NotFinish.check(player)){
            FirstPayPrizeState.Finish.setState(player);
            playerDAO.save(player);
            MsgFactoryPlayer.instance().getFirstPayPrizeStateMsg(parent, player);
        }
        return parent;
    }

    @Override
    public int getFuncTimes(PlayerVO playerVO, FuncTimes funcTimes) {
        refFuncTimes(playerVO, funcTimes, System.currentTimeMillis());
//        Integer times = playerVO.getFuncTimes().get(funcTimes.getId());
        Integer times=0;
        return times;
    }

    @Override
    public void changeFuncTimes(Map<?, ?> parent, PlayerVO playerVO, FuncTimes funcTimes, int value) {
        int currTimes = getFuncTimes(playerVO, funcTimes);
        int limit = funcTimes.getLimit(playerVO);
        int after = currTimes + value;
//        if(value < 0){
//            if(currTimes >= limit){
//                playerVO.getFuncTimesLastRevTime().put(funcTimes.getId(), System.currentTimeMillis());
//            }
//            playerVO.getFuncTimes().put(funcTimes.getId(), after);
//        }else{
//            if(currTimes + value >= limit){
//                playerVO.getFuncTimes().put(funcTimes.getId(), limit);
//            }else{
//                playerVO.getFuncTimes().put(funcTimes.getId(), after);
//            }
//        }
        MsgFactoryPlayer.instance().getFuncTimesUpdate(parent, funcTimes, playerVO);
        playerDAO.save(playerVO);
    }

    @Override
    public void checkFuncTimes(PlayerVO playerVO, FuncTimes funcTimes, int value) {
        int currTimes = getFuncTimes(playerVO, funcTimes);
        CodePlayer.Player_FuncTimes_Minus.throwIfTrue(currTimes < value);
    }

    @Override
    public void refFuncTimes(Map parent, PlayerVO player, long currTime) throws LogicException {
        for (FuncTimes funcTimes : FuncTimes.values()) {
            boolean change = refFuncTimes(player, funcTimes, currTime);
            if(change && parent != null) MsgFactoryPlayer.instance().getFuncTimesUpdate(parent, funcTimes, player);
        }
    }




    @Override
    public void resetFuncTimes(PlayerVO playerVO) {
        for (FuncTimes funcTimes : FuncTimes.values()) {
            int limit = funcTimes.getLimit(playerVO);
//            playerVO.getFuncTimes().put(funcTimes.getId(), limit);
//            playerVO.getFuncTimesLastRevTime().put(funcTimes.getId(), System.currentTimeMillis());
        }
    }

    @Override
    public Map getFuncTimes(PlayerVO playerVO, CMD cmd) throws LogicException {
        long currTime = System.currentTimeMillis();
        Map result = GameUtil.createSimpleMap();
        refFuncTimes(result, playerVO, currTime);
        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map worship(PlayerVO player, CMD cmd, int playerId) throws LogicException {
//        CodePlayer.AlreadyWorship.throwIfTrue(player.isWorship());
        String name = CachePlayerBase.getPnameByPid(playerId);
        CodePlayer.Player_NotFindPlayer.throwIfTrue(name == null);
//        PlayerLevelConfig config = PlayerLevelConfig.By_Level[player.getLevel()];
//        CodePlayer.Player_NotFindConfig.throwIfTrue(config == null);
        Map result = GameUtil.createSimpleMap();
        Map prizeMap = GameUtil.createSimpleMap();
//        SyncFuncType.ChangeCur.getFunc().checkAndExec(result, prizeMap,cmd, player, new FuncParam(CurrencyType.TRE.getType(), config.getWorshipPrz()));
//        player.setWorship(true);
        playerDAO.save(player);
        EvtTypePlayer.Worship.happen(result, cmd, player, EvtParamType.PID.val(playerId));
//        MsgFactoryPlayer.instance().getWorshipUpdate(result, player);
        MsgFactoryPlayer.instance().getPrizeMsg(result, prizeMap);
        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map titleDec(PlayerVO player, CMD cmd, String titleDec) throws LogicException {
//        player.setTitleDec(titleDec);
        playerDAO.save(player);
        Map result = GameUtil.createSimpleMap();
//        MsgFactoryPlayer.instance().getTitleDecUpdate(result, player);
        CachePlayerBase.updateBasInfo(player, PlayerInfoVOUpdateType.TITLE_DEC);
        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map modifyName(PlayerVO player, CMD cmd, String newName) throws LogicException {
        String playerName = newName.trim();
        checkPlayerName(playerName);
        Map result = GameUtil.createSimpleMap();
        int cost = CacheDisDataPlayer.ModifyPlayerNameCost.first();
        SyncFuncType.ChangeGoods.getFunc().checkAndExec(result,cmd,player,new FuncParam(cost, -1));
        String oldName = player.getName();
        player.setName(playerName);
        // 重新添加到缓存 更新名字缓存    老名字将不能使用
        CachePlayer.resetPlayerName(player, oldName);
        CachePlayerBase.resetPlayerName(player, oldName);
        MsgFactoryPlayer.instance().getPlayerNameUpdate(result, player);
        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map vipLevelPrize(PlayerVO player, CMD cmd, int level) throws LogicException {
        CodePlayer.Player_Vip_Limit.throwIfTrue(level >= VipLevelConfig.Configs.length);
//        CodePlayer.Player_VipLevel_Minus.throwIfTrue(player.getVip() < level);
//        CodePlayer.AlreadyRevVipLevelPrize.throwIfTrue(player.getVipPrize().contains(level));
        VipLevelConfig config = VipLevelConfig.Configs[level];
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
        executor.addSyncFunc(config.getPrizes());
        Map result = GameUtil.createSimpleMap();
        Map prizeMap = GameUtil.createSimpleMap();
        executor.exec(result, prizeMap, player);
//        player.getVipPrize().add(level);
        MsgFactoryPlayer.instance().addPrizeMsg(result, prizeMap);
//        MsgFactoryPlayer.instance().getPlayerVipPrizeUpdateMsg(result, player);
        playerDAO.save(player);
        return MsgUtil.brmAll(result, cmd);
    }

    private boolean refFuncTimes(PlayerVO playerVO, FuncTimes funcTimes, long currTime){
//        Integer times = playerVO.getFuncTimes().get(funcTimes.getId());
//        int limit = funcTimes.getLimit(playerVO);
//        if(times == null) {
//            playerVO.getFuncTimes().put(funcTimes.getId(), limit);
//            playerVO.getFuncTimesLastRevTime().put(funcTimes.getId(), currTime);
//            return true;
//        }else{
//            if(times >= funcTimes.getLimit(playerVO)){
//                return false;
//            }
//            long lastRevTime = playerVO.getFuncTimesLastRevTime().get(funcTimes.getId());
//            if(lastRevTime <= 0){
//                playerVO.getFuncTimes().put(funcTimes.getId(), limit);
//                playerVO.getFuncTimesLastRevTime().put(funcTimes.getId(), currTime);
//                return true;
//            }
//            long offset = currTime - lastRevTime;
//            long cd = funcTimes.getCd(playerVO) * DateUtil.SECONDS_MILLIS;
//            if(cd <= 0) return false;
//            long revTimes = offset/cd;
//            if(revTimes <= 0) return false;
//            long revNum = revTimes * funcTimes.getRevNum(playerVO);
//            long revEndTime = lastRevTime + cd * revTimes;
//            int revEndTimes = (int) (revNum + times);
//            if(revEndTimes > limit) revEndTimes = limit;
//            playerVO.getFuncTimes().put(funcTimes.getId(), revEndTimes);
//            playerVO.getFuncTimesLastRevTime().put(funcTimes.getId(), revEndTime);
//            return true;
//        }
        return  true;
    }

    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypePlayer.PlayerLevelUp, EvtTypePlayer.EnterGame, EvtTypePlayer.Offline, EvtTypePlayer.CacheTimeOut, EvtTypePlayer.PI_Offline};
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> o = event.getType();
        if (o instanceof EvtTypePlayer) {
            switch ((EvtTypePlayer) o) {
                case PlayerLevelUp:
                    onPlayerLevelUp((PlayerVO) holder, cmd);
                    break;
                case EnterGame:
                    onPlayerEnterGame((PlayerVO) holder, cmd);
                    break;
                case Offline:
                    saveAll((PlayerVO) holder);
                    break;
                case CacheTimeOut:
                case PI_Offline:
                    offline((PlayerVO) holder, cmd);
                    break;
            }
        }
        return parent;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> datas) throws LogicException {
        List<DBObj> dbObjs = (List<DBObj>) datas.get(DATA_SIGN_FIXED_DROP);
        if (dbObjs != null && dbObjs.size() > 0) {
            FixedDropVO fixedDropVO = new FixedDropVO();
            fixedDropVO.readFromDBObj(dbObjs.get(0));
            fixedDropVO.setPid(player.getId());
            player.setFixedDropVO(fixedDropVO);
        }

        // DB数据
        List<DBObj> checkInData = (List<DBObj>) datas.get(DATA_SIGN_CHECK_IN);
        if (checkInData == null || checkInData.size() == 0) return;
        CheckInVO checkInVO = new CheckInVO();
        checkInVO.readFromDBObj(checkInData.get(0));
        checkInVO.setPlayer(player.getId());
        // 加入玩家数据
        player.setCheckInVO(checkInVO);
    }


	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		// TODO Auto-generated method stub

	}


}
