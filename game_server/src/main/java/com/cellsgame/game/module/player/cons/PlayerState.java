package com.cellsgame.game.module.player.cons;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2017/8/17.
 */
public enum  PlayerState {
    FROZEN(1),
    BAN_CHAT(2),
    ;
    private int type;

    PlayerState(int t){
        this.type = t;
    }

    public boolean check(PlayerVO player){
        return GameUtil.checkIntFlag(player.getState(), type);
    }

    public void setState(PlayerVO player){
        player.setState(GameUtil.setIntFlag(player.getState(), type));
    }

    public void resetState(PlayerVO player){
        player.setState(GameUtil.resetIntFlag(player.getState(), type));
    }

}
