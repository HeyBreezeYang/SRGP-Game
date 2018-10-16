package com.cellsgame.game.module.func.impl.checkrec;


import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.cons.IRecCheckerType;

public class CheckRecGuildMny extends CheckRec<CheckRecGuildMny> {

    private long num;

    @Override
    protected void accept(CheckRecGuildMny rec) {
        addNum(rec.getNum());
    }

    @Override
    public IRecChecker<CheckRecGuildMny> getChecker() {
        return IRecCheckerType.GuildMnyCost.getChecker();
    }

    @Override
    public CheckRecGuildMny create() {
        return new CheckRecGuildMny();
    }

    public long addNum(long num) {
        return this.num += num;
    }

    public long getNum() {
        return num;
    }
}
