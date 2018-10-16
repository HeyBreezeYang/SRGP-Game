package com.cellsgame.game.module.func.formula.impl;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.func.formula.IFormula;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class OperateMoney implements IFormula {
    @Override
    public long getValue(PlayerVO playerVO) {
        long iq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_IQ];
        return iq;
    }
}
