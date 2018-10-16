package com.cellsgame.game.core.dispatch;

import java.util.Map;

import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on 2017-02-14.
 */
public abstract class CatchRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CatchRunnable.class);
    protected final MessageController controller;
    protected final CMD cmd;

    public CatchRunnable() {
        this((MessageController) null, CMD.system.now());
    }

    public CatchRunnable(MessageController controller, CMD cmd) {
        this.cmd = cmd;
        this.controller = controller;
    }


    public CatchRunnable(PlayerVO pvo, CMD cmd) {
        this.cmd = cmd;
        if (null != pvo) {
            this.controller = pvo.getMessageController();
        } else
            this.controller = null;
    }

    @Override
    public final void run() {
        Map result;
        try {
            result = runLogic();
        } catch (LogicException e) {
            result = MsgUtil.brmAll(null, cmd.getCmd(), e.getCode());
        } catch (Throwable e) {
            log.error("on Exception ", e);
            result = MsgUtil.brmAll(null, cmd.getCmd(), CodeGeneral.General_ServerException);
        }
        if (result != null)
            Push.multiThreadSend(controller, result);
    }

    protected abstract Map<String, Object> runLogic() throws LogicException;
}
