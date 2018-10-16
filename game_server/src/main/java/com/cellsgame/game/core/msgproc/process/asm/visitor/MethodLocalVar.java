package com.cellsgame.game.core.msgproc.process.asm.visitor;

/**
 * @author Aly on  2016-07-11.
 */
public class MethodLocalVar {
    private String name;

    private String desc;
    private String signature;
    private int index;

    public MethodLocalVar(String name, String desc, String signature, int index) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
