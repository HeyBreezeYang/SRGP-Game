package com.cellsgame.game.module.rank.bo;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.DailyResetable;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on 2017-03-14.
 */
@AModule(ModuleID.RANK)
public interface RankBO extends IBuildData, DailyResetable, StaticEvtListener{
    /**
     * 获取排行
     * star end 指排名
     */
    @Client(Command.RANK_GET_RANK_LIST)
    Map<String, Object> getRank(PlayerVO pvo, CMD cmd, @CParam("type") int type, @CParam("st") int start, @CParam("ed") int end) throws LogicException;

    /**
     * 点赞
     */
    @Client(Command.RANK_ADD_LIKE)
    Map<String, Object> addLike(PlayerVO pvo, CMD cmd, @CParam("type") int type) throws LogicException;

    void init();

    void onShutDown();

	/**
	 * @param type
	 * @param key
	 * @param range
	 * @return
	 */
	List<Integer> getRankAround(int type, int key, int range);

	/**
	 * @param gvo
	 */
	void build(GuildVO gvo);
}
