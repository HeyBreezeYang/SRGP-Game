package com.cellsgame.game.module.func.formula.impl;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.func.formula.IFormula;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 *  寻访金币奖励 - 中档资源
 *  低档资源=属性/10+1000
 *  中档资源=属性/5+3000
 *  高档资源=属性/1+5000
 * */
public class SearchMoneyCenter implements IFormula {
    @Override
    public long getValue(PlayerVO playerVO) {
        long iq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_IQ];
        return iq / 5 + 3000;
    }
}
