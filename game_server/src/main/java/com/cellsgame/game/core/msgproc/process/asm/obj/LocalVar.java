package com.cellsgame.game.core.msgproc.process.asm.obj;

import org.objectweb.asm.Label;

/**
 * @author Aly on  2016-07-13.
 */
public class LocalVar {
    private String name;
    private String desc;
    private String signature;
    private Label start;
    private Label end;
    private int index;

    private LocalVar() {
    }

    public static LocalVar valueOf(String name, String desc, String signature, Label start, Label end, int index) {
        LocalVar var = new LocalVar();
        var.name = name;
        var.desc = desc;
        var.signature = signature;
        var.start = start;
        var.end = end;
        var.index = index;
        return var;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSignature() {
        return signature;
    }

    public Label getStart() {
        return start;
    }

    public Label getEnd() {
        return end;
    }

    public int getIndex() {
        return index;
    }
}
