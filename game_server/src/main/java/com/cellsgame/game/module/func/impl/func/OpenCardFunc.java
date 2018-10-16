package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.card.bo.CardBO;
import com.cellsgame.game.module.card.csv.CardConfig;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.Table;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * @author Aly @2016-12-14.
 */
public class OpenCardFunc extends SyncFunc {

    @Resource
    private CardBO cardBO;

    @Override
    protected Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
        return null;
    }

    @Override
    protected Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        return cardBO.openCard(parent, player, param.getParam());
    }

    @Override
    public String checkCfg(FuncParam param, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        // 接受任务的ID
        int cardId = param.getParam();
        // 任务数据
        BaseCfg cardConfig = allCfgData.get(CardConfig.class, cardId);
        if (null == cardConfig) return " 错误的任务ID:" + cardId;
        return null;

    }

}
