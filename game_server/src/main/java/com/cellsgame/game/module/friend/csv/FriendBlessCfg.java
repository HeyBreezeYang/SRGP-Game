package com.cellsgame.game.module.friend.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.util.DebugTool;

import java.util.List;
import java.util.Map;

public class FriendBlessCfg extends BaseCfg {
    private static Map<Integer, FriendBlessCfg> cache = GameUtil.createSimpleMap();
    private int lv;
    // 单次祝福奖励
    private List<FuncConfig> blessPrize;
    // 收获奖励
    private List<FuncConfig>[] blessedPrize;

    public static void addCache(FriendBlessCfg cfg) {
        FriendBlessCfg put = cache.put(cfg.getLv(), cfg);
        if (null != put) {
            DebugTool.throwException("重复的配置FriendBlessCfg：" + cfg.getLv());
        }
    }

    public static FriendBlessCfg get(int level) {
        return cache.get(level);
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public List<FuncConfig> getBlessPrize() {
        return blessPrize;
    }

    public void setBlessPrize(List<FuncConfig> blessPrize) {
        this.blessPrize = blessPrize;
    }

    public List<FuncConfig> getBlessedPrize(int ix) {
        if (blessedPrize != null && ix >= 0 && ix < blessedPrize.length) {
            return blessedPrize[ix];
        }
        return null;
    }

    public void setBlessedPrize(List<FuncConfig>[] blessedPrize) {
        this.blessedPrize = blessedPrize;
    }
}
