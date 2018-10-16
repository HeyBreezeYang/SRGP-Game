package com.cellsgame.game.module.func.impl.func;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.PrizeConstant;
import com.cellsgame.game.module.player.cons.CacheDisDataPlayer;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.msg.MsgFactoryPlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

/**
 * @author Aly @2016-12-14.
 */
public class AddActivenessFunc extends SyncFunc {
    @Resource
    private BaseDAO<PlayerVO> playerDAO;

    @Override
    protected Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
        return null;
    }

    @Override
    protected Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        // 增加活跃度
//        player.setActiveness(Math.min(player.getActiveness() + (int)param.getValue(), CacheDisDataPlayer.ActivenessLimit.getData()[0]));
//        EvtTypePlayer.ACTIVENESS.happen(parent, cmd, player, EvtParamType.AFTER.val(player.getActiveness()+0L));
        ///
        MsgFactoryPlayer.instance().getActivenessMsg(parent, player);
        //
        //
        playerDAO.save(player);
        if(prizeMap != null)
            PrizeConstant.addPlayerActiveness(prizeMap, (int)param.getValue());
        //
        return parent;
    }
}
