package com.cellsgame.game.module.func.formula.impl;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.func.formula.IFormula;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * 经营机会奖励计算
 * */
public class OperateChance implements IFormula {
    @Override
    public long getValue(PlayerVO playerVO) {
        long frsg = playerVO.getAtt().get().getValue()[IAttribute.TYPE_FRSG];
        return frsg;
    }
}
