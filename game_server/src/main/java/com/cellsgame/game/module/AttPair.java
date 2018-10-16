package com.cellsgame.game.module;

import com.cellsgame.game.core.csv.IAttribute;

public class AttPair {

    private long[] value = IAttribute.createAttribute();

    private long[] ratio = IAttribute.createAttribute();

    public long[] getValue() {
        return value;
    }

    public void setValue(long[] value) {
        this.value = value;
    }

    public long[] getRatio() {
        return ratio;
    }

    public void setRatio(long[] ratio) {
        this.ratio = ratio;
    }

    public void sum(AttPair attPair) {
        for (int i = 0; i < attPair.getValue().length; i++) {
            value[i] += attPair.getValue()[i];
            ratio[i] += attPair.getRatio()[i];
        }
    }

    public void clear() {
        value = IAttribute.createAttribute();
        ratio = IAttribute.createAttribute();
    }
}
