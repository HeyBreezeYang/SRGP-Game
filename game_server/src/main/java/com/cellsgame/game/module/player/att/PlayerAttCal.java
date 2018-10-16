package com.cellsgame.game.module.player.att;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.AttPair;
//import com.cellsgame.game.module.player.att.impl.PlayerBeautyCal;
//import com.cellsgame.game.module.player.att.impl.PlayerChildCal;
//import com.cellsgame.game.module.player.att.impl.PlayerWorkerCal;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.cons.PlayerInfoVOUpdateType;
import com.cellsgame.game.module.player.msg.MsgFactoryPlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Arrays;
import java.util.Map;

public class PlayerAttCal implements IPlayerCal {

    private AttPair attPair = new AttPair();

//    private IPlayerCal workerCal = new PlayerWorkerCal();
//    private IPlayerCal childCal = new PlayerChildCal();
//    private IPlayerCal beautyCal = new PlayerBeautyCal();

    @Override
    public void init(Map parent, CMD cmd, PlayerVO playerVO) {
//        beautyCal.init(parent, cmd, playerVO);
//        workerCal.init(parent, cmd, playerVO);
//        childCal.init(parent, cmd, playerVO);
        afreshCal(parent, cmd, playerVO);
    }

    @Override
    public void afreshCal(Map parent, CMD cmd, PlayerVO playerVO){
        attPair.clear();
//        attPair.sum(workerCal.get());
//        attPair.sum(childCal.get());
//        long before = playerVO.getFightForce();
//        playerVO.setFightForce(attPair.getValue()[IAttribute.TYPE_IQ] + attPair.getValue()[IAttribute.TYPE_EQ]
//        + attPair.getValue()[IAttribute.TYPE_EQEN] + attPair.getValue()[IAttribute.TYPE_FRSG]);
//        EvtTypePlayer.FightForce.happen(parent, cmd, playerVO, EvtParamType.AFTER.val(playerVO.getFightForce()+0L), EvtParamType.BEFORE.val(before));
        MsgFactoryPlayer.instance().getPlayerAttUpdateMsg(parent, playerVO);
        CachePlayerBase.updateBasInfo(playerVO, PlayerInfoVOUpdateType.fightForce);
    }

    @Override
    public AttPair get() {
        return attPair;
    }
//
//    public IPlayerCal getWorkerCal() {
//        return workerCal;
//    }
//
//    public IPlayerCal getChildCal() {
//        return childCal;
//    }
}
