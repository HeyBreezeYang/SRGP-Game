package com.cellsgame.game.core.dispatch;

/**
 * Created by alyx on 17-6-21.
 * 分发线程
 */
public class DispatchThread extends Thread {
    private DispatchType dispatchType;

    DispatchThread(ThreadGroup group, Runnable target, String name,
                   long stackSize) {
        super(group, target, name, stackSize);
    }

    public DispatchType getDispatchType() {
        return dispatchType;
    }

    void setDispatchType(DispatchType dispatchType) {
        this.dispatchType = dispatchType;
    }
}
