package com.cellsgame.game.module.player.bo;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.DailyResetable;
import com.cellsgame.game.module.player.cons.FuncTimes;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.util.CmdTriFunction;
import com.cellsgame.game.util.CmdTriFunctionEx;

@AModule(ModuleID.Player)
public interface PlayerBO extends StaticEvtListener, DailyResetable, IBuildData {

    public void init();

    @Client(Command.Player_VerifySign)
    void verifySign(MessageController controller, GameMessage message, @CParam("serverId") String serverID, @CParam("sign") String sign) throws LogicException;

    void reconnection(MessageController controller, GameMessage message, PlayerVO player) throws LogicException;

    @Client(Command.Player_EnterGame)
    void enterGame(MessageController controller, GameMessage message, CMD cmd) throws LogicException;

    @Client(Command.Player_Create)
    void createPlayer(MessageController controller, GameMessage message, @CParam("playerName") String playerName, @CParam("img") int img, CMD cmd) throws LogicException;

    @Client(Command.Player_Random_Name)
    Map randomName(MessageController controller, CMD cmd, @CParam("m") boolean male) throws LogicException;

    @Client(Command.Player_Query_By_Pid)
    Map<String, Object> queryPlayerByPID(MessageController controller, CMD cmd, @CParam("pid") int pid) throws LogicException;

    @Client(Command.Player_Record_Story)
    Map<String, Object> recordStory(CMD cmd, PlayerVO player, @CParam("storyId") int storyId) throws LogicException;

    @Client(Command.Player_CHECK_IN)
    Map checkIn(PlayerVO player, CMD cmd) throws LogicException;

    @Client(Command.Player_GET_TOTAL_CHECK_IN_PRIZE)
    Map getTotalCheckInPrize(PlayerVO player, CMD cmd, @CParam("ds") int days) throws LogicException;

    @Client(Command.Player_PI)
    Map pi(PlayerVO player, CMD cmd) throws LogicException;

    @Client(Command.Player_ServerTime)
    Map serverTime(PlayerVO player, CMD cmd) throws LogicException;

    @Client(Command.Player_RevFirstPayPrize)
    Map revFirstPayPrize(PlayerVO player, CMD cmd) throws LogicException;

    @Client(Command.Player_UpLevel)
    Map upLevel(PlayerVO player, CMD cmd) throws LogicException;

    @Client(Command.Player_FuncTimes)
    Map getFuncTimes(PlayerVO player, CMD cmd) throws LogicException;

    @Client(Command.Player_Worship)
    Map worship(PlayerVO player, CMD cmd, @CParam("playerId") int playerId) throws LogicException;

    @Client(Command.Player_TitleDec)
    Map titleDec(PlayerVO player, CMD cmd, @CParam("titleDec") String titleDec) throws LogicException;

    @Client(Command.Player_ModifyName)
    Map modifyName(PlayerVO player, CMD cmd, @CParam("name") String newName) throws LogicException;

    @Client(Command.Player_VipLevelPrize)
    Map vipLevelPrize(PlayerVO player, CMD cmd, @CParam("level") int level) throws LogicException;

    void offline(MessageController controller);

    void offline(PlayerVO player, CMD cmd);

    void verifySuc(MessageController controller, GameMessage message, Integer serverId, String accountId, String token, int admin);

    void loadSuc(MessageController controller, GameMessage message, CMD cmd, Integer serverId, String accountId, Map<String, ?> data);

    /**
     * 添加玩家经验
     *
     * @param parent   添加结果
     * @param player   玩家数据
     * @param quantity 数量
     * @return 添加经验结果
     */
    Map changeExp(Map parent, PlayerVO player, long quantity, CMD cmd) throws LogicException;

    /**
     * 添加玩家VIP经验
     *
     * @param parent   添加结果
     * @param player   玩家数据
     * @param quantity 数量
     * @return 添加经验结果
     */
    Map changeVipExp(Map parent, PlayerVO player, long quantity, CMD cmd) throws LogicException;

    /**
     * 改变VIP
     */
    Map changeVip(Map parent, PlayerVO player, int vip, CMD cmd) throws LogicException;

    List<PlayerVO> loadAll();

    PlayerVO load(Map<String, ?> data, CMD cmd) throws LogicException;

    void save(PlayerVO player);

    /**
     * 系统关闭 处理逻辑
     */
    void onShutDown();

    Map<String, Object>  changeFirstPayPrizeState(Map<String, Object> parent, PlayerVO player);

    public int getFuncTimes(PlayerVO playerVO, FuncTimes funcTimes);

    void changeFuncTimes(Map<?, ?> parent, PlayerVO playerVO,  FuncTimes funcTimes, int value);

    void checkFuncTimes(PlayerVO playerVO, FuncTimes funcTimes, int value);

    void refFuncTimes(Map parent, PlayerVO player, long currTime) throws LogicException;


    void resetFuncTimes(PlayerVO src);
    
    
    /**
	 * @param cmd
	 * @param pvo
	 * @param tgtPid
	 * @param biz
	 */
	void loadPlayerAndExecBiz(CMD cmd, PlayerVO pvo, int tgtPid, CmdTriFunction<Map, PlayerVO, PlayerVO, Map> biz);

	/**
	 * @param cmd
	 * @param pvo
	 * @param tgtPid
	 * @param biz
	 * @param params
	 */
	void loadPlayerAndExecBiz(CMD cmd, PlayerVO pvo, int tgtPid, CmdTriFunctionEx<Map, PlayerVO, PlayerVO, Map> biz, Object ... params);



}
