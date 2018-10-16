package com.cellsgame.game.module.func.cons;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecCur;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGoods;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGuildMny;

public enum CheckRecType {
    Cur(new CheckRecCur()),
    Goods(new CheckRecGoods()),
    GuildMny(new CheckRecGuildMny()),
    ;
    private Class<? extends CheckRec> recClass;
    private CheckRec<?> checkRec;

    <T extends CheckRec<T>> CheckRecType(T checkRec) {
        this.checkRec = checkRec;
        recClass = checkRec.getClass();
        SpringBeanFactory.autowireBean(checkRec);
    }

    public <T extends CheckRec> T getCheckRec() {
        return (T) checkRec.create();
    }

    public void setCheckRec(CheckRec<?> checkRec) {
        this.checkRec = checkRec;
    }

    public Class<? extends CheckRec> getRecClass() {
        return recClass;
    }

    public void setRecClass(Class<? extends CheckRec> recClass) {
        this.recClass = recClass;
    }


}
