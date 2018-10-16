package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.ICheckerType;
import com.cellsgame.game.module.func.cons.IRecCheckerType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;

public class SkillLearnFunc extends SyncFunc implements WeightValueFunc {

    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        return null;
    }


    private Map<?, ?> learnSkill(Map<?, ?> parent, PlayerVO player, int cid, int num, CMD cmd) throws LogicException {
        return null;
    }


    @Override
    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
        return null;
    }


    @Override
    public IChecker getParamChecker() {
        return ICheckerType.SkillLearn.getChecker();
    }

    @Override
    public String checkCfg(FuncParam config, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {

        return CacheSkill.skillMap.containsKey(config.getParam()) ? null : "不存在的技能";
    }


}
