package com.cellsgame.game.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cellsgame.conc.disruptor.DpEvt;
import com.cellsgame.conc.disruptor.DpWorkHandler;

public class RunableWorkHandler implements DpWorkHandler {
	
    private static Logger log = LoggerFactory.getLogger(RunableWorkHandler.class);

	@Override
	public void onEvent(DpEvt event) throws Exception {
		try {
            Runnable logic = (Runnable) event.getData();
            if (null != logic) {
                event.setData(null);
                logic.run();
            }
        } catch (Throwable e) {
            log.error("", e);
        }
	}
	
	public static RunableWorkHandler[] create(int size){
		RunableWorkHandler[] handlers = new RunableWorkHandler[size];
		for (int i = 0; i < handlers.length; i++) {
			handlers[i] = new RunableWorkHandler();
		}
		return handlers;
	}
	
}
