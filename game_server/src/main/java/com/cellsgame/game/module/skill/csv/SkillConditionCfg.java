package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * 技能生效条件配置
 */
public class SkillConditionCfg extends BaseCfg {
    // 条件功能编码
    private int func_code;
    // 运算符
    private int operator;
    // TODO 值类型（百分比，万分制；绝对值；公式）
    private int value_type;
    // 值
    private int value;

    public int getFunc_code() {
        return func_code;
    }

    public void setFunc_code(int func_code) {
        this.func_code = func_code;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
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
}
