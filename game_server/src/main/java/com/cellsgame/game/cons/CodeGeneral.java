package com.cellsgame.game.cons;

import com.cellsgame.game.core.excption.LogicException;

public enum CodeGeneral implements ICode {
    General_NotFindProcess(2),
    General_InvokeParamError(3),
    General_ServerException(4),
    General_NotLogin(5),
    General_VerifyTokenFail(6),
    General_Csv_Error(8),
    General_Param_Error(9), //参数异常
    General_Func_Error(10), //功能错误
    General_Data_Error(11), //数据错误
    General_UnSupportOperation(12), //不支持的操作
    General_Security_Open(13), //安全锁已打开
    General_FUNCION_NOT_Open(14), //功能未开启
    General_SERVER_IS_SHUTDOWN(15), //服务器已经关闭
    General_LoginReturnAttachException(16), //登录返回登录信息错误
    General_LoginReturnSecurityException(17),//登录返回设备锁信息错误
    General_ServerNotOpen(18),//服务器没有开放
    General_VerifySignFail(19),
    ;

    private int code;

    CodeGeneral(int code) {
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.General;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void throwIfTrue(boolean IF) throws LogicException {
        if (IF) throw new LogicException(getModule() + getCode());
    }

}
