package com.cellsgame.game.core.csv;

import com.cellsgame.common.util.csv.BaseCfg;

@SuppressWarnings("unused")
public abstract class AttributeConfig extends BaseCfg implements IAttribute {

    public static final String TYPE_IQ_NAME = "iq";// 智商
    public static final String TYPE_EQ_NAME = "eq";// 情商
    public static final String TYPE_EQEN_NAME = "eqen";// 口才
    public static final String TYPE_FRSG_NAME = "frsg";// 眼光

    private int iq;
    private int eq;
    private int eqen;
    private int frsg;
    /**
     * 伙伴属性
     */
    private long[] attribute;

    public int getIq() {
        return iq;
    }

    public void setIq(int iq) {
        this.iq = iq;
    }

    public int getEq() {
        return eq;
    }

    public void setEq(int eq) {
        this.eq = eq;
    }

    public int getEqen() {
        return eqen;
    }

    public void setEqen(int eqen) {
        this.eqen = eqen;
    }

    public int getFrsg() {
        return frsg;
    }

    public void setFrsg(int frsg) {
        this.frsg = frsg;
    }

    public long[] getAttribute() {
        if (null == attribute) {
            attribute = IAttribute.createAttribute();
            attribute[IAttribute.TYPE_IQ] = iq;
            attribute[IAttribute.TYPE_EQ] = eq;
            attribute[IAttribute.TYPE_EQEN] = eqen;
            attribute[IAttribute.TYPE_FRSG] = frsg;
        }
        return attribute;
    }

    private void setAttribute(long[] attribute) {
        this.attribute = attribute;
    }
}
