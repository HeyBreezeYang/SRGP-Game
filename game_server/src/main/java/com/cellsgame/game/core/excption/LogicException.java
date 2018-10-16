package com.cellsgame.game.core.excption;


public class LogicException extends RuntimeException {
    private static final long serialVersionUID = 7243881150126768082L;
    private int code;

    public LogicException(int code) {
        super(null, null, false, false);
        this.code = code;
    }

    public LogicException(int code, String message) {
        super(message, null, false, false);
        this.code = code;

    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " " + code;
    }

    public int getCode() {
        return code;
    }
}