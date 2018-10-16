package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.GameUtil;

import java.util.List;
import java.util.Set;

/**
 * 技能(辅助)配置
 */
public class SkillSupportCfg extends SkillCfg {
    // 辅助类型
    private int aid_type;

    private int range;


    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAid_type() {
        return aid_type;
    }

    public void setAid_type(int aid_type) {
        this.aid_type = aid_type;
    }

    @Override
    public Set<Integer> getPreIds() {
        return GameUtil.createSet();
    }
}
