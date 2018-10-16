package com.cellsgame.game.module.guild.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;

/**
 * 家族
 */
public enum CodeGuild implements ICode {

    NameTooShort(1),               // 名字太短
    NameTooLong(2),                // 名字太长
    HaveKeyWords(3),          // 包含关键字
    SameName(4),                    // 名字已经存在
    PlayerLevelMinus(5),            // 等级被限制了
    NotFindGuild(6),                    // 公会不存在
    EnterGuildCD(7),                  // 进入公会CD中
    AlredyInGuild(8),                 // 已经在某个公会里面了
    RightLevelMemberFull(9),      // 目标权限人数已经满了
    MaxReqSize(10),                // 达到最大请求数量
    MemberFull(11),              // 公会成员已经达到最大值
    NotJoinGuild(12),                    // 未加入任何公会
    RightMinus(13),                    // 权限不足
    ReqTimeout(14),                // 请求不存在 或者已经过期
    AlreadyDissolution(15),              // 公会正在解散中
    AlreadyInGuild(16),                    // 已经在公会中了
    SystemBuys(17),                    // 系统繁忙
    NotFindDonateConfig(18),           //没有找到捐献配置
    AlreadyDonate(19),                 //已经捐献过
    LevelMinus(20),                    // 公会等级不足
    GuildMoneyMinus(21),                   // 公会资金不足
    GuildCoinMinus(22),                    //公会贡献不足
    NotFindBossConfig(23),           //没有找到boss配置
    NotOpenBoss(24),      // 没有开放BOSS，或者已经击杀
    WorkerAlreadyFight(25),                    // 员工已经攻击过
    LevelLimit(26),                 // 公会等级达到最大值
    NotUseEmptyName(27),      // 名字不能为数字
    NameFormationError(28),      // 名字格式不对
    NotMatchGuild(29),                 // 没有匹配到公会
    ;
    private int code;

    CodeGuild(int code) {
        this.code = code;
    }


    @Override
    public int getModule() {
        return ModuleID.Guild;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void throwIfTrue(boolean IF) throws LogicException {
        if (IF) throw new LogicException(getModule() + getCode());
    }

}
