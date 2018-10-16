package com.cellsgame.game.module.player.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;

public enum CodePlayer implements ICode {
    // 玩家
    Player_NotFindPlayer(1), //没有找到
    Player_ExpMinus(2),//经验不足
    Player_ReconnectionTokenError(3),   // 重连Token错误
    Player_MoneyMinus(4),   //金币不足
    Player_NameAlreadyUse(5),   //  名字已经被使用
    Player_NameIsNull(6),   //  名字为空
    Player_ReconnectionNotOnline(7),    //重连玩家不在线
    Player_Ap_Minus(8),    //体力不足
    Player_SkillNotFound(9), // 技能不存在
    Player_LearnedSkill(10), // 技能已学习
    Player_Error_LearnCost(11), // 技能不存在
    Player_Energy_Minus(12), // 活力值不足
    Player_Activeness_Minus(13), // 活跃度不足
    Player_NotFindConfig(14),
    Player_Level_Minus(15), // 等级不足
    Player_Buy_Ap_Limit(16), // 体力购买次数上限
    Player_NO_SKIll_UP_LV(17), // 没有需要升级的技能
    Player_NameLengthLimit(18),//名字字数超过上限
    Player_NameFormationError(19),//名字格式错误
    NOT_OWN_IMG2(20),           // 没有拥有此头像
    Player_Buy_Energy_Limit(21), // 活力值购买次数上限
    No_Login_Prize(22), // 没有登陆奖励
    Login_Prized(23), // 已领取
    Already_Check_In(24), // 已签到
    No_Check_In_Prize(25), // 没有签到奖励
    Player_Buy_Money_Limit(26), // 金币购买次数上限
    Player_Frozen(27),//已经冻结
    Player_LuckyVal_Minus(28),//幸运值不足
    Player_FirstPayNotFinish(29),//第一次充值未完成
    Player_FirstPayAlreadyRev(30),//第一次充值奖励已经领取
    Player_FirstPayNotFindConfig(31),// 没有找到配置
    Player_FuncTimes_Minus(32),//功能次数不足
    Player_Level_Limit(33), // 等级到达上限
    Player_Exp_Minus(34),//经验不足
    Player_PartyTimes_Minus(35),//酒会次数不足
    Player_ChildPos_Minus(36),//子嗣位不足
    Player_Vip_Limit(37), // VIP等级到达上限
    AlreadyWorship(38),//已经崇拜
    Player_VipExp_Minus(39),//VIP经验不足
    AlreadyRevVipLevelPrize(40),//已经领取VIP奖励
    Player_VipLevel_Minus(41),//VIP等级不足
    ;
    private int code;

    CodePlayer(int code) {
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.Player;
    }

    @Override
    public int getCode() {
        return code;
    }

}
