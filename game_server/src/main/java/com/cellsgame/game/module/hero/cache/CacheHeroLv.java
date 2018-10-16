package com.cellsgame.game.module.hero.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.hero.csv.HeroLvCfg;

import java.util.Map;

public class CacheHeroLv {
    // Map<COLOR, Map<LEVEL, HeroLvCfg>>
    public static Map<Integer, Map<Integer, HeroLv>> heroLvMap = GameUtil.createSimpleMap();

    /**
     * 转职前等级消耗
     * @param config
     */
    public static void addHeroLv(HeroLvCfg config) {
        Integer color = config.getColor();
        if (!CacheHeroLv.heroLvMap.containsKey(color)) {
            CacheHeroLv.heroLvMap.put(color, GameUtil.createSimpleMap());
        }

        Map<Integer, HeroLv> levelCfg = CacheHeroLv.heroLvMap.get(color);
        {
            HeroLv cache = new HeroLv();

            cache.setId(config.getId());
            cache.setColor(config.getColor());
            cache.setLevel(config.getLevel());

            cache.setExp(config.getExp1());
            cache.setCost_items(config.getCost_items_1());
            cache.setCost_general_items(config.getCost_general_items_1());
            levelCfg.put(config.getLevel(), cache);
        }
    }
    /**
     * 转职后等级消耗
     * @param config
     */
    public static void addHeroCareerLv(HeroLvCfg config) {
        Integer color = config.getColor();
        if (!CacheHeroLv.heroLvMap.containsKey(color)) {
            CacheHeroLv.heroLvMap.put(color, GameUtil.createSimpleMap());
        }

        Map<Integer, HeroLv> levelCfg = CacheHeroLv.heroLvMap.get(color);
        {
            HeroLv cache = new HeroLv();

            cache.setId(config.getId());
            cache.setColor(config.getColor());
            cache.setLevel(config.getLevel() + HeroLvCfg.FULL_LEVEL);

            cache.setExp(config.getExp2());
            cache.setCost_items(config.getCost_items_2());
            cache.setCost_general_items(config.getCost_general_items_2());
            levelCfg.put(config.getLevel() + HeroLvCfg.FULL_LEVEL, cache);
        }
    }
}
