package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.cons.CheckRecType;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGoods;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import javax.annotation.Resource;

/**
 * 英雄(突破)吞噬检查
 */
public class HeroSwallowChecker implements IChecker {
    @Resource
    private PlayerBO playerBO;

    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
    }
}
