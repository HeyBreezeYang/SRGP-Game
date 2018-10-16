package com.cellsgame.game.core.message;


/**
 * Created by alyx on 17-6-29.
 * 客户端CMD 对象列表
 */
public final class CMD implements Cloneable {
    public static final CMD PayPrize = new CMD(450001, "pay_prize");
    public static final CMD PITimeout = new CMD(-3, "pi_timeout");
	public static final CMD cacheTimeout = new CMD(-2, "timeout");
    public static final CMD system = new CMD(-1, "system");
    private String name;
    private int cmd;
    private long time;

    public CMD(int cmd, String name) {
        @SuppressWarnings("ThrowableNotThrown")
        StackTraceElement[] stackTrace = new Throwable("").getStackTrace();
        String className = stackTrace[1].getClassName();
        if (!className.equals(CMD.class.getName())) {
            throw new RuntimeException("不能从外部调用此类 构造方法");
        }
        this.cmd = cmd;
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CMD now() {
        if (time > 0) {
            return this;
        } else {
            try {
                CMD clone = (CMD) clone();
                clone.time = System.currentTimeMillis();
                return clone;
            } catch (CloneNotSupportedException ignored) {
            }
        }
        return this;
    }


    public int getCmd() {
        return cmd;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    // TODO temp add by luojf945
    public void setCmd(int cmd) { this.cmd = cmd; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "CMD(" + cmd + "," + name + ")";
    }
}
