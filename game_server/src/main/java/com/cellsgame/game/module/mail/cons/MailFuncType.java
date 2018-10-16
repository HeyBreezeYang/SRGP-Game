package com.cellsgame.game.module.mail.cons;

public enum MailFuncType {
    NULL(0),
    AddGuild(1),
    Fight(2),;

    private int type;

    MailFuncType(int type) {
        this.type = type;
	}

	public int getType() {
		return type;
	}
	
}
