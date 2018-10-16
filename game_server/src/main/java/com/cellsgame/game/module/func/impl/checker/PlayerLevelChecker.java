package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class PlayerLevelChecker implements IChecker {

    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
        // 目标等级
        long val = param.getValue();
        // 等级不够
//        CodePlayer.Player_Level_Minus.throwIfTrue(player.getLevel() < val);
    }
}
