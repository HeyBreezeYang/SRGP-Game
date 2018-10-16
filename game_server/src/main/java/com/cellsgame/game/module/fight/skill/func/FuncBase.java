package com.cellsgame.game.module.fight.skill.func;

public abstract class FuncBase {
    public enum FUNC_ENUM {
        ChangeProperty(1),                   // 改变属性
	    ChangeSpecialMaxCD(2),               // 改变奥义计量上限
	    ChangeSpecialCD(3),                  // 改变奥义计量
	    ChangeSpecialCDVariable(4),          // 改变奥义计量变化量
	    ChangeSpecialCDVariableAttack(5),    // 改变因攻击奥义计量变化量
	    ChangeColorRevise(6),                // 三角克制变化
	    CancelColorRevise(7),                // 三角克制无效
        CancelArmsRevise(8),                 // 兵种克制无效
        CancelWeaponRevise(9),               // 武器克制无效
        BiDingZhuiJi(10),// 必定追击
        QiangXianGongJi(11),// 抢先攻击
        LiJiZhuiJi(12),// 立即追击
        WuFaZhuiJi(13),// 无法追击
        WuShiJiLiFanJi(14),// 无视距离反击
        WuFaFanJi(15),// 无法反击
        LiJiFanJi(16),// 立即反击
        GaiBianZhanDouShunXuWuXiao(17),// 改变战斗顺序技能无效
        ChangeFixDamage(18),                  // 改变战斗伤害值
        ChangeDamageRate(19),                 // 改变战斗增伤比率
        CreateDamage(20),                     // 造成伤害
        ChooseDefense(21),                    // 防御选择
        LimitMinHP(22),                       // hp不能低于1
        CancelStaffRevise(23),                // 杖的伤害衰减无效
        ChangeActorBuff(24),                  // 改变状态
        ChangePosition(25),                   // 改变位置
        ChangeBattleBenefit(26),              // 改变战斗收益
        Reaction(27),                         // 再动
        ChangeMovement(28),                   // 改变移动距离
        LimitMovement(29),                    // 移动限制
        MoveToTarget(30),                     // 移动至X相邻的格子
        ChangePropertyBeforeCompare(31),      // 比较前改变属性
        ExtraAttack(32),                      // 额外攻击
        ;

        private int value;

        FUNC_ENUM(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }


    public abstract void execute();

    public abstract void unexecute();

    public int getValue() {
        return 0;
        // TODO 未实现
    }
}
