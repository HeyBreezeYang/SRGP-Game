package com.cellsgame.game.module.player.cons;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2017/8/17.
 */
public enum FirstPayPrizeState {

    NotFinish(0),
    Finish(1),
    Prizeed(2),
    ;
    private int type;

    FirstPayPrizeState(int t){
        this.type = t;
    }

    public boolean check(PlayerVO player){
        return player.getFirstPayPrizeState() == type;
    }

    public void setState(PlayerVO player){
        player.setFirstPayPrizeState(type);
    }

}
