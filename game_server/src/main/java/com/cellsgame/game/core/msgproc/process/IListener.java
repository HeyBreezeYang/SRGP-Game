package com.cellsgame.game.core.msgproc.process;

import javax.annotation.PostConstruct;

import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.message.GameMessage;

public abstract class IListener {
    @PostConstruct
    public void init() {
        MessageProcess.regListener(this);
    }

    public abstract void exec(MessageController controller, GameMessage message);

}
