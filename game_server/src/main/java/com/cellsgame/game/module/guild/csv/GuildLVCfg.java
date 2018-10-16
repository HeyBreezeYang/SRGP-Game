package com.cellsgame.game.module.guild.csv;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

/**
 * @author Aly on  2016-08-15.
 *         家族配置  ID 为家族等级
 */
public class GuildLVCfg extends BaseCfg {
    public static final Map<Integer, GuildLVCfg> GUILD_LV_CFG_MAP = GameUtil.createSimpleMap();
    private int lv;
    // 家族最大可申请人数列表
    private int maxCanReqSize;
    // 升级到当前等级消耗
    private int reqExp;

    public static GuildLVCfg getByLV(int lv) {
        return GUILD_LV_CFG_MAP.get(lv);
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getMaxCanReqSize() {
        return maxCanReqSize;
    }

    public void setMaxCanReqSize(int maxCanReqSize) {
        this.maxCanReqSize = maxCanReqSize;
    }

    public int getReqExp() {
        return reqExp;
    }

    public void setReqExp(int reqExp) {
        this.reqExp = reqExp;
    }
}
