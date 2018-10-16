package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Set;

/**
 * 技能(圣印)配置
 */
public class SkillSealCfg extends SkillCfg {
    // 前置ID
    private int pre_id;
    // 后置ID
    private int rear_id;
    // 制作需要拥有的技能
    private int skill_id;
    //生产消耗道具
    private List<FuncConfig> need_items;

    //升级消耗道具
    private List<FuncConfig> upgrade_need_items;

    private int default_open;

    public int getPre_id() {
        return pre_id;
    }

    public void setPre_id(int pre_id) {
        this.pre_id = pre_id;
    }

    public int getRear_id() {
        return rear_id;
    }

    public void setRear_id(int rear_id) {
        this.rear_id = rear_id;
    }


    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }

    public List<FuncConfig> getNeed_items() {
        return need_items;
    }

    public void setNeed_items(List<FuncConfig> need_items) {
        this.need_items = need_items;
    }

    public List<FuncConfig> getUpgrade_need_items() {
        return upgrade_need_items;
    }

    public void setUpgrade_need_items(List<FuncConfig> upgrade_need_items) {
        this.upgrade_need_items = upgrade_need_items;
    }

    @Override
    public Set<Integer> getPreIds() {
        Set<Integer> list = GameUtil.createSet();
        list.add(pre_id);

        return list;
    }

    public int getDefault_open() {
        return default_open;
    }

    public void setDefault_open(int default_open) {
        this.default_open = default_open;
    }
}
