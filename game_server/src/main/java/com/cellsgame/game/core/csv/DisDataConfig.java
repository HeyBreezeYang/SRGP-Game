package com.cellsgame.game.core.csv;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * 基础离散数据配置表
 */
public class DisDataConfig extends BaseCfg {

    private int[] data;

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }
}

