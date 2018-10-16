package com.cellsgame.game.module.player.att;

import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.AttPair;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public interface IPlayerCal {

    public void init(Map parent, CMD cmd, PlayerVO playerVO);

    public void afreshCal(Map parent, CMD cmd, PlayerVO playerVO);

    public AttPair get();

}
