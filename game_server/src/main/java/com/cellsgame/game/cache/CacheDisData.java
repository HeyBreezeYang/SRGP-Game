package com.cellsgame.game.cache;

public interface CacheDisData {
    default int createId(int id) {
        return 9000000 + getModule() + id;
    }

    int getModule();

    int getId();

    int[] getData();

    void setData(int[] data);

    default int get(int ix) {
        return getData()[ix];
    }

    default int first() {
        return getData()[0];
    }

    default boolean needSort() {
        return false;
    }

    default int  size() {
        return getData().length;
    }
}
