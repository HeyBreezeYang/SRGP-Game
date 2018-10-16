package com.cellsgame.game.util;

/**
 * @author Aly on 2017-05-12.
 */
public class PairInt<L> {
    private L left;
    private int right;

    public static <T> PairInt<T> valueOf(T left, int right) {
        PairInt<T> pair = new PairInt<>();
        pair.left = left;
        pair.right = right;
        return pair;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}
