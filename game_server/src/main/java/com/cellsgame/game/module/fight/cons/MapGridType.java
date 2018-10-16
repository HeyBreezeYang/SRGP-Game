package com.cellsgame.game.module.fight.cons;

import java.util.Map;

public enum MapGridType {
    MOVABLE(1),
    BLOCK(2),              // 障碍，山脉，城墙，河流等
    FOREST(3),             // 森林，骑兵不能通过
    FLYING_ONLY(4),        // 只允许飞行单元可通过
    RESTORE(5),            // 回复点
    DESTRUCTIBLE(6),       // 可破坏的
    HOUSE(7),              // 房屋
    WARP_PORTAL(8),        // 传送阵
    ;

    private int value;
    MapGridType(int v) {
        value = v;
    }

    public Integer getValue() {
        return value;
    }

    public static void main(String args[]) {
        System.out.println("测试枚举类型和整形值对比");
        System.out.println("MapGridType.BLOCK.equals(1):"+MapGridType.BLOCK.toString() == "1");
    }
}

