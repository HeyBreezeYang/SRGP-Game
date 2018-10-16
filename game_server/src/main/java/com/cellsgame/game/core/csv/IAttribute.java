package com.cellsgame.game.core.csv;

public interface IAttribute {
    int LEN = 7;
    int TYPE_COST = 0;
    int TYPE_EQEN = 1;//口才
    int TYPE_IQ = 2;//智商
    int TYPE_EQ = 3;//情商
    int TYPE_FRSG = 4;//眼光
    int TYPE_CRIT = 5;//暴击几率
    int TYPE_HURT = 6;//暴击伤害
    long[] EMPTY = createAttribute();

    static long[] createAttribute() {
        return new long[LEN];
    }

    long[] getAttribute();

    static int[] baseAtt = new int[]{TYPE_IQ, TYPE_EQ, TYPE_EQEN, TYPE_FRSG};

}