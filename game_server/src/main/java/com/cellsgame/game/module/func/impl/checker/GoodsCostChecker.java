package com.cellsgame.game.module.func.impl.checker;

import javax.annotation.Resource;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.cons.CheckRecType;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGoods;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class GoodsCostChecker implements IRecChecker<CheckRecGoods> {
    @Resource
    private DepotBO depotBO;

    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
        CheckRecGoods rec = recCheck(param);
        if (rec != null)
            checkRec(player, rec);
    }

    @Override
    public void checkRec(PlayerVO player, CheckRecGoods rec) throws LogicException {
        depotBO.checkGoodsEnough(player, rec.getGoodsCost());
    }

    private CheckRecGoods recCheck(FuncParam param) {
        CheckRecGoods rec = null;
        if (param.getValue() < 0) {
            rec = CheckRecType.Goods.getCheckRec();
            rec.add(param.getParam(), (int)Math.abs(param.getValue()));
        }
        return rec;
    }
}
