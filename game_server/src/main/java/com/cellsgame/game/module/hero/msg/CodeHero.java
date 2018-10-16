package com.cellsgame.game.module.hero.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;


/**
 * 英雄
 *
 * @see ModuleID#Hero
 */

public enum CodeHero implements ICode {
    HERO_NOT_CONFIG(1),                      // 未配置该英雄
    HERO_NOT_FOUND(2),                       // 英雄未找到;
    HERO_ITEM_ILLEGAL(3),                    // 英雄使用非法道具
    HERO_FULL_LEVEL(4),                      // 英雄满级
    HERO_NOT_FULL_LEVEL(5),                  // 英雄未满级
    HERO_MAX_STAR(6),                        // 英雄满星
    HERO_MAX_STAGE(7),                       // 英雄满
    HERO_NOT_SAME_STAR(8),                   // 英雄星级不等
    HERO_NOT_SAME_ID(9),                     // 不是同名英雄
    HERO_CAREER_EXCHANGED(10),                     // 英雄已经转过职
    HERO_SP_MINUS(11),                       // 英雄SP值不足
        ;
    private int code;

    CodeHero(int code){
        this.code = code;
    }


    @Override
    public int getModule() {
        return ModuleID.Hero;
    }

    @Override
    public int getCode() {
        return code;
    }
}
