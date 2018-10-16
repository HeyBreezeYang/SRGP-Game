package com.cellsgame.game.core;

import com.cellsgame.conc.disruptor.DpEventHandler;
import com.cellsgame.conc.disruptor.DpEvt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnableHandler implements DpEventHandler {
    private static Logger log = LoggerFactory.getLogger(RunnableHandler.class);

    @Override
    public void onEvent(DpEvt event, long sequence, boolean endOfBatch) throws Exception {
        try {
            Runnable logic = (Runnable) event.getData();
            if (null != logic) {
                event.setData(null);
                logic.run();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("", e);
        }
    }

}
