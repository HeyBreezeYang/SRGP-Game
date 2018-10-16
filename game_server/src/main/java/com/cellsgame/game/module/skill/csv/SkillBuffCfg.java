package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * 技能效果配置
 */
public class SkillBuffCfg extends BaseCfg {
    private int type;
    // 功能编码
    private int func_code;
    // 功能参数
    private int func_param;
    // TODO 值类型（百分比，万分制；绝对值；公式）
    private int value_type;
    // 效果值
    private int value;
    // 叠加类型
    // TODO 叠加值相同，效果不叠加，取最大；叠加值不托管呢，效果叠加
    private int overlay_type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFunc_code() {
        return func_code;
    }

    public void setFunc_code(int func_code) {
        this.func_code = func_code;
    }

    public int getFunc_param() {
        return func_param;
    }

    public void setFunc_param(int func_param) {
        this.func_param = func_param;
    }

    public int getValue_type() {
        return value_type;
    }

    public void setValue_type(int value_type) {
        this.value_type = value_type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getOverlay_type() {
        return overlay_type;
    }

    public void setOverlay_type(int overlay_type) {
        this.overlay_type = overlay_type;
    }
}
