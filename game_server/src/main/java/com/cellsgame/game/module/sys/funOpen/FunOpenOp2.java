package com.cellsgame.game.module.sys.funOpen;

import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public interface FunOpenOp2<T> {
    Map listen(T bean, CMD cmd, Map parent, PlayerVO pvo);
}
