package com.cellsgame.game.module.activity.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;

/**
 * 活动
 */
public enum CodeActivity implements ICode {

    ACTIVITY_EXEC_BEV_ERROR(1),               // 行为执行错误
    ACTIVITY_PARAMS_ERROR(2),               // 参数错误
    ACTIVITY_NOT_IN_ACTION(3),//活动未开启
    ACTIVITY_NOT_EXISTS(4), //活动不存在
    ACTIVITY_WORK_MODE_ERROR(5),//活动模式错误
    ACTIVITY_EXEC_BEV_LIMIT(6),//行为执行上限
    ACTIVITY_BEV_SCOPE_CONF_ERROR(7),//配置错误
    ACTIVITY_EXEC_PARAMS_ERROR(8),//执行参数错误
    ACTIVITY_EXEC_CD_EXISTS(9),//CD中
    ACTIVITY_SERVER_DATA_ERROR(10),//数据错误
    ACTIVITY_ID_ALREADY(11),//活动已经发布
    ACTIVITY_NOT_FIND(12),//活动不存在
    ActivityHonorNotEnough(13),//活动荣誉不足
    NotFindPrize(14),//没有找到奖励
    NotEnterRank(15),//没有进入排行榜
    ExecBevTimeError(16),//执行行为时间未到
    NotFindGroup(17),//没有该分组
    
    ;
    private int code;

    CodeActivity(int code) {
        this.code = code;
    }


    @Override
    public int getModule() {
        return ModuleID.Activity;
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
