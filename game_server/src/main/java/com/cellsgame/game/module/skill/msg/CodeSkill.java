package com.cellsgame.game.module.skill.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodeSkill implements ICode {
    SKILL_NOT_CONFIG(1),                // 未配置该技能
    SKILL_NOT_FOUND(2),                 // 技能未找到
    SKILL_PRE_NEEDED(3),                // 前置技能不满足
    SKILL_EQUIPPED_ON(4),               // 技能已装备
    SKILL_EQUIPPED_OFF(5),              // 技能未装备
    SKILL_NOT_A(6),
    SKILL_NOT_B(7),
    SKILL_NOT_C(8),
    SKILL_NOT_SUPPORT(9),
    SKILL_NOT_SPECIAL(10),
    SKILL_NOT_SEAL(11),
    SKILL_NOT_IN(12),                   // 技能不在英雄的技能列表里
    SKILL_FULL_LEVEL(13),               // 技能满级
    SKILL_ARM_LIMIT(14),                // 技能兵种限制
    SKILL_WEAPON_LIMIT(15),             // 技能武器限制
    SEAL_NOT_EQUIP(16),                 // 圣印未装备
    SKILL_NO_SLOT(17),                  // 英雄没有空位可装备该技能
    SKILL_NOT_LEARN(18),                // 英雄没有空位可装备该技能
    SKILL_STAR_LIMIT(19),               // 英雄星级限制
    SEAL_PRE_NEEDED(20),                // 圣印前置技能不满足
    SKILL_ONLY_SELF(21),                // 技能仅限本人装备
    SKILL_INHERIT_LIMIT(22),            // 技能继承数量限制
    ;

    private  int code;

    CodeSkill(int code) { this.code = code; }

    @Override
    public int getModule() {
        return ModuleID.Skill;
    }

    @Override
    public int getCode() {
        return code;
    }
}
