package com.cellsgame.game.module.func.formula.impl;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.func.formula.IFormula;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * 寻访人脉奖励 - 中档资源
 *  低档资源=属性/10+1000
 *  中档资源=属性/5+3000
 *  高档资源=属性/1+5000
 * */
public class SearchContactsCenter implements IFormula {
    @Override
    public long getValue(PlayerVO playerVO) {
        long eq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_EQ];
        return eq / 5 + 3000;
    }
}
