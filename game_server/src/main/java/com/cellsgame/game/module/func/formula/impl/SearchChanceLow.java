package com.cellsgame.game.module.func.formula.impl;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.func.formula.IFormula;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 *  寻访机会奖励奖励 - 低档资源
 *  低档资源=属性/10+1000
 *  中档资源=属性/5+3000
 *  高档资源=属性/1+5000
 * */
public class SearchChanceLow implements IFormula {
    @Override
    public long getValue(PlayerVO playerVO) {
        long frsg = playerVO.getAtt().get().getValue()[IAttribute.TYPE_FRSG];
        return frsg / 10 + 1000;
    }
}
