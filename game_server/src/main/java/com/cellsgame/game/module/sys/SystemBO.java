package com.cellsgame.game.module.sys;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.module.sys.cons.SysDateRecType;

/**
 * @author Aly on  2016-07-18.
 */
@AModule(ModuleID.System)
public interface SystemBO {
    void init();

    void recordTime(SysDateRecType recordType, long time);

    long getRecordTime(SysDateRecType recordType);

    int randomWeightValue(int weightId);
}
