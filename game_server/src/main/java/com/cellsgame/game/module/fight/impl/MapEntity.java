package com.cellsgame.game.module.fight.impl;

import com.cellsgame.game.module.fight.IAttackable;
import com.cellsgame.game.module.fight.IMapChecker;
import com.google.common.collect.HashBasedTable;

/**
 * 地图实体
 */
public class MapEntity implements IAttackable {

    private Integer width;

    private Integer height;

    private HashBasedTable<Integer, Integer, Integer> grids;

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public HashBasedTable<Integer, Integer, Integer> getGrids() {
        return grids;
    }

    public void setGrids(HashBasedTable<Integer, Integer, Integer> grids) {
        this.grids = grids;
    }

    @Override
    public void onAttack(Integer i, Integer j) {
    }
}
