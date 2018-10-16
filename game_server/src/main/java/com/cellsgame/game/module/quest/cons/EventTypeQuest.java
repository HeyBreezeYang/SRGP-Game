package com.cellsgame.game.module.quest.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

public enum EventTypeQuest implements EvtType {
	FIN_CDITS_SATISFIED,
	QUEST_FIN,
	UPDATE,
	;
	private static final AtomicInteger incr = new AtomicInteger(ModuleID.Quest);

	static {
		EvtType[] values = values();
		for (EvtType eType : values) {
			eType.setEvtCode(incr.incrementAndGet());
		}
	}

	private int evtCode;

	@Override
	public int getEvtCode() {
		return evtCode;
	}

	@Override
	public void setEvtCode(int code) {
		this.evtCode = code;
	}
}
