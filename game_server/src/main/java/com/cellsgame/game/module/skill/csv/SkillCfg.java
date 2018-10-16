package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;

import java.util.Set;

public abstract  class SkillCfg extends BaseCfg {
    private int type;

    private int need_sp;
    // 前置技能1
    private int pre_1_id;
    // 前置技能2
    private int pre_2_id;

    // TODO 【效果组,效果触发点组,效果生效点组】一一对应
    // 效果目标
    private int effect_1_targets;
    // 效果条件组
    private int[] effect_1_conditions;
    // 效果组
    private int[] effects_1;
    // 效果触发点组
    private int[] effect_1_trigger_points;
    // 效果生效点组
    private int[] effect_1_take_effects;
    // 持续类型
    private int[] continue_1;

    // 效果目标
    private int effect_2_targets;
    // 效果条件组
    private int[] effect_2_conditions;
    // 效果组
    private int[] effects_2;
    // 效果触发点组
    private int[] effect_2_trigger_points;
    // 效果生效点组
    private int[] effect_2_take_effects;
    // 持续类型
    private int[] continue_2;

    // 效果目标
    private int effect_3_targets;
    // 效果条件组
    private int[] effect_3_conditions;
    // 效果组
    private int[] effects_3;
    // 效果触发点组
    private int[] effect_3_trigger_points;
    // 效果生效点组
    private int[] effect_3_take_effects;
    // 持续类型
    private int[] continue_3;


    private int[] limit_weapons;

    private int[] limit_arms;



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNeed_sp() {
        return need_sp;
    }

    public void setNeed_sp(int need_sp) {
        this.need_sp = need_sp;
    }

    public int getPre_1_id() {
        return pre_1_id;
    }

    public void setPre_1_id(int pre_1_id) {
        this.pre_1_id = pre_1_id;
    }

    public int getPre_2_id() {
        return pre_2_id;
    }

    public void setPre_2_id(int pre_2_id) {
        this.pre_2_id = pre_2_id;
    }

    public int getEffect_1_targets() {
        return effect_1_targets;
    }

    public void setEffect_1_targets(int effect_1_targets) {
        this.effect_1_targets = effect_1_targets;
    }

    public int[] getEffect_1_conditions() {
        return effect_1_conditions;
    }

    public void setEffect_1_conditions(int[] effect_1_conditions) {
        this.effect_1_conditions = effect_1_conditions;
    }

    public int[] getEffects_1() {
        return effects_1;
    }

    public void setEffects_1(int[] effects_1) {
        this.effects_1 = effects_1;
    }

    public int[] getEffect_1_trigger_points() {
        return effect_1_trigger_points;
    }

    public void setEffect_1_trigger_points(int[] effect_1_trigger_points) {
        this.effect_1_trigger_points = effect_1_trigger_points;
    }

    public int[] getEffect_1_take_effects() {
        return effect_1_take_effects;
    }

    public void setEffect_1_take_effects(int[] effect_1_take_effects) {
        this.effect_1_take_effects = effect_1_take_effects;
    }

    public int getEffect_2_targets() {
        return effect_2_targets;
    }

    public void setEffect_2_targets(int effect_2_targets) {
        this.effect_2_targets = effect_2_targets;
    }

    public int[] getEffect_2_conditions() {
        return effect_2_conditions;
    }

    public void setEffect_2_conditions(int[] effect_2_conditions) {
        this.effect_2_conditions = effect_2_conditions;
    }

    public int[] getEffects_2() {
        return effects_2;
    }

    public void setEffects_2(int[] effects_2) {
        this.effects_2 = effects_2;
    }

    public int[] getEffect_2_trigger_points() {
        return effect_2_trigger_points;
    }

    public void setEffect_2_trigger_points(int[] effect_2_trigger_points) {
        this.effect_2_trigger_points = effect_2_trigger_points;
    }

    public int[] getEffect_2_take_effects() {
        return effect_2_take_effects;
    }

    public void setEffect_2_take_effects(int[] effect_2_take_effects) {
        this.effect_2_take_effects = effect_2_take_effects;
    }

    public int getEffect_3_targets() {
        return effect_3_targets;
    }

    public void setEffect_3_targets(int effect_3_targets) {
        this.effect_3_targets = effect_3_targets;
    }

    public int[] getEffect_3_conditions() {
        return effect_3_conditions;
    }

    public void setEffect_3_conditions(int[] effect_3_conditions) {
        this.effect_3_conditions = effect_3_conditions;
    }

    public int[] getEffects_3() {
        return effects_3;
    }

    public void setEffects_3(int[] effects_3) {
        this.effects_3 = effects_3;
    }

    public int[] getEffect_3_trigger_points() {
        return effect_3_trigger_points;
    }

    public void setEffect_3_trigger_points(int[] effect_3_trigger_points) {
        this.effect_3_trigger_points = effect_3_trigger_points;
    }

    public int[] getEffect_3_take_effects() {
        return effect_3_take_effects;
    }

    public void setEffect_3_take_effects(int[] effect_3_take_effects) {
        this.effect_3_take_effects = effect_3_take_effects;
    }

    public int[] getLimit_weapons() {
        return limit_weapons;
    }

    public void setLimit_weapons(int[] limit_weapons) {
        this.limit_weapons = limit_weapons;
    }

    public int[] getLimit_arms() {
        return limit_arms;
    }

    public void setLimit_arms(int[] limit_arms) {
        this.limit_arms = limit_arms;
    }

    public Set<Integer> getPreIds() {
        Set<Integer> set = GameUtil.createSet();
        set.add(pre_1_id);
        set.add(pre_2_id);

        return set;
    }

    public int[] getContinue_1() {
        return continue_1;
    }

    public void setContinue_1(int[] continue_1) {
        this.continue_1 = continue_1;
    }

    public int[] getContinue_2() {
        return continue_2;
    }

    public void setContinue_2(int[] continue_2) {
        this.continue_2 = continue_2;
    }

    public int[] getContinue_3() {
        return continue_3;
    }

    public void setContinue_3(int[] continue_3) {
        this.continue_3 = continue_3;
    }
}
