package com.cellsgame.game.module.func.impl.checker;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.impl.func.FuncsGoodsFunc;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.cons.GoodsConstant;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.msg.CodeGoods;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class FuncsGoodsChecker implements IChecker {

    private static Set<Integer> validGoodsId = GameUtil.createSet();

    private static Map<Integer, LogicException> invalidGoodsId = GameUtil.createMap();

    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
        CodeGeneral.General_Func_Error.throwIfTrue(param == null);
        int goodsId = param.getParam();
        if (validGoodsId.contains(goodsId))
            return;
        LogicException oldException = invalidGoodsId.get(goodsId);
        if (oldException != null)
            throw oldException;

        Set<Integer> existsFuncGoodsId = GameUtil.createSet();
        try {
            checkFuncGoodsEndlessLoop(param, existsFuncGoodsId);
        } catch (LogicException e) {
            invalidGoodsId.put(goodsId, e);
            throw e;
        }
        validGoodsId.add(goodsId);
    }


    private void checkFuncGoodsEndlessLoop(FuncParam param, Set<Integer> existsFuncGoodsId) throws LogicException {
        CodeGeneral.General_Func_Error.throwIfTrue(param == null);
        int goodsId = param.getParam();
        CodeGoods.GOODS_FUNCS_ENDLESSLOOP.throwIfTrue(existsFuncGoodsId.contains(goodsId));
        GoodsConfig goodsCfg = CacheGoods.getGoodsConfigById(goodsId);
        CodeGoods.Goods_ConfigError.throwIfTrue(goodsCfg == null);
        CodeGoods.Goods_Type_Error.throwIfTrue(goodsCfg.getGoodsType() != GoodsConstant.GOODS_TYPE_FUNC
            && goodsCfg.getGoodsType() != GoodsConstant.GOODS_TYPE_ITEM);
        ItemGoodsConfig funcGoodsCfg = (ItemGoodsConfig) goodsCfg;
        // 检查的时候可以不用设置CMD
        FuncsExecutor<?> exec = funcGoodsCfg.getFuncs(null);
        existsFuncGoodsId.add(goodsId);
        Collection<AbstractFunc> allFuncs = exec.getAllFuncs();
        for (AbstractFunc func : allFuncs) {
            if (func instanceof FuncsGoodsFunc) {
                Set<Integer> parentGoodsIds = GameUtil.createSet();
                parentGoodsIds.addAll(existsFuncGoodsId);
                checkFuncGoodsEndlessLoop(func.getParam(), parentGoodsIds);
            }
        }
    }


}
