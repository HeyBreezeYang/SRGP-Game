package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.*;
import com.cellsgame.game.module.func.cons.ICheckerType;
import com.cellsgame.game.module.func.impl.exec.FuncsExecutorCollection;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;

/**
 * 功能组道具 功能
 * 内部使用一个执行器集合, 在执行的每个阶段都对功能组道具包含的子功能的进行相应的深度优先递归调用
 * 注意!!!!!!!
 * 1.在功能组道具中直接包含以及之后派生的所有子功能中同样包含 功能组道具功能 的情况下 ,
 * 不论层级严格禁止任意一个子节点上的道具id跟父节点上的道具id相同, 即严禁一个功能
 * 道具中以直接或间接方式包含自身
 * 2.在一次完整的调用过程中, 该功能对象是一个有状态对象
 *
 * @author peterveron
 */
public class FuncsGoodsFunc extends SyncFunc {

    private FuncsExecutorCollection innerExecutors;


    @Override
    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
        GoodsConfig goodsCfg = CacheGoods.getGoodsConfigById(param.getParam());
        if (goodsCfg instanceof ItemGoodsConfig) {
            innerExecutors = new FuncsExecutorCollection();
            ItemGoodsConfig funcGoods = (ItemGoodsConfig) goodsCfg;
            int count = (int)param.getValue();
            for (int i = 0; i < count; i++) {
                // 执行的时候重置CMD
                innerExecutors.addExecutor(funcGoods.getFuncs(CMD.system.now()));
            }
            innerExecutors.selectAndRec(player);
            return innerExecutors.getCheckRec();
        }
        return null;
    }


    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        if (innerExecutors != null) {
            innerExecutors.setCmd(cmd);
            innerExecutors.runSelectedFuncs(parent, prizeMap, player, execNum);
        }
        return null;
    }


    @Override
    public IChecker getParamChecker() {
        return ICheckerType.FuncsGoods.getChecker();
    }

    @Override
    public AbstractFunc clone() {
        FuncsGoodsFunc clone = (FuncsGoodsFunc) super.clone();
        if (innerExecutors != null) {
            clone.innerExecutors = innerExecutors.copy();
        }
        return clone;
    }

    @Override
    public String checkCfg(FuncParam config, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        GoodsConfig goodsCfg = CacheGoods.getGoodsConfigById(config.getParam());
        if (null == goodsCfg || !(goodsCfg instanceof ItemGoodsConfig)) {
            return "不存在的 ItemGoodsConfig 配置:" + config.getParam();
        }
        return null;
    }
}
