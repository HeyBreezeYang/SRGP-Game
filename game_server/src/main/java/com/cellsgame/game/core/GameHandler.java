package com.cellsgame.game.core;

import com.cellsgame.conc.disruptor.DpEvt;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.process.MessageProcess;
import com.cellsgame.game.core.push.Push;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GameHandler extends RunnableHandler {
    private static final Logger log = LoggerFactory.getLogger(GameHandler.class);

    @Override
    public void onEvent(DpEvt event, long sequence, boolean endOfBatch) throws Exception {
        if (null == event.getData()) return;
        if (event.getData() instanceof Runnable) {
            super.onEvent(event, sequence, endOfBatch);
        } else {
            Object[] data = (Object[]) event.getData();
            event.setData(null);

            MessageController controller = (MessageController) data[0];
            Object command = data[1];
            Object[] param = (Object[]) data[2];
            GameMessage message = (GameMessage) data[3];
            if (command instanceof Integer) {
                int cmd = (Integer) command;
                Map<?, ?> result;
                try {
                    result = MessageProcess.exec(controller, message, cmd, param);
                } catch (LogicException e) {
                    result = MsgUtil.brmAll(null, cmd, e.getCode());
                } catch (Throwable e) {
                    log.error("on Exception @{}", cmd, e);
                    result = MsgUtil.brmAll(null, cmd, CodeGeneral.General_ServerException);
                }
                if (result != null)
                    Push.multiThreadSend(controller, result);
            }
        }
    }
}
