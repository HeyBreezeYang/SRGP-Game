package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.ICheckerType;
import com.cellsgame.game.module.hero.cache.CacheHeroProp;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;

/**
 * 英雄(突破)吞噬检查，
 */
public class HeroSwallowFunc extends SyncFunc implements WeightValueFunc {


    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        return null;
    }


    private Map<?, ?>  swallowHero(Map<?, ?> parent, PlayerVO player, int cid, int num, CMD cmd) throws LogicException {
        return null;
    }


    @Override
    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
        return null;
    }


    @Override
    public IChecker getParamChecker() {
        return ICheckerType.HeroSwallow.getChecker();
    }

    @Override
    public String checkCfg(FuncParam config, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        int cid = config.getParam();

        return CacheHeroProp.heroPropMap.containsKey(cid) ? null : "不存在的英雄:" + cid;
    }


}
