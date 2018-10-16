package com.cellsgame.game.module.func.impl.checker;

import javax.annotation.Resource;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.msg.CodeDepot;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class DepotCapacityChecker<T extends GoodsVO> implements IChecker {
    @Resource
    private DepotBO depotBO;

    private Class<T> clazz;

    public DepotCapacityChecker(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
        // 背包空间不足
        CodeDepot.Depot_Capacity_max.throwIfTrue(!depotBO.isCapacityEnough(clazz, player.getDepotVO(), (int)param.getValue()));
    }
}
