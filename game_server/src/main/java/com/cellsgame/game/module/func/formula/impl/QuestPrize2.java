package com.cellsgame.game.module.func.formula.impl;

import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.func.formula.IFormula;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 *  每日任务金币奖励
 *  3档数值=智力*3+200000
 *  2档数值=智力*2+100000
 *  1档数值=智力*1+20000
 * */
public class QuestPrize2 implements IFormula {
    @Override
    public long getValue(PlayerVO playerVO) {
        long iq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_IQ];
        return iq * 2 + 100000;
    }
}
