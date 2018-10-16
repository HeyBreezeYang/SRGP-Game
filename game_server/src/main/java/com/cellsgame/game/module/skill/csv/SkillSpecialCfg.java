package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.GameUtil;

import java.util.List;
import java.util.Set;

/**
 * 技能(奥义)配置
 */
public class SkillSpecialCfg extends SkillCfg {
    private int aoyi_type;

    private int max_cd;

    public int getAoyi_type() {
        return aoyi_type;
    }

    public void setAoyi_type(int aoyi_type) {
        this.aoyi_type = aoyi_type;
    }

    public int getMax_cd() {
        return max_cd;
    }

    public void setMax_cd(int max_cd) {
        this.max_cd = max_cd;
    }
}
