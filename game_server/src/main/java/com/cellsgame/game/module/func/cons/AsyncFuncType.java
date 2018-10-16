package com.cellsgame.game.module.func.cons;

import java.util.Map;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.AsyncFunc;
import com.cellsgame.game.module.func.ICallback;
import com.cellsgame.game.module.player.vo.PlayerVO;


public enum AsyncFuncType {
    
    ;
    private String type;
    private AsyncFunc func;

    AsyncFuncType(String type, AsyncFunc func) {
        this.type = type;
        this.func = func;
        SpringBeanFactory.autowireBean(func);
    }


    public String getType() {
        return type;
    }

    public AsyncFunc getFunc() {
        return (AsyncFunc) func.clone();
    }

    public Object exec(CMD cmd, Map<?, ?> parent, PlayerVO player, Object params, ICallback callback) throws LogicException {
        return func.exec(cmd, parent, player, params, callback);
    }
}
