package com.cellsgame.game.module.hero.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.hero.cons.HeroType;
import com.cellsgame.game.module.hero.csv.HeroCareerCfg;
import com.cellsgame.game.module.hero.csv.HeroCfg;
import com.cellsgame.game.module.hero.csv.HeroLvCfg;

import java.util.Map;
// 缓存1级，40级，40*2级时英雄属性
public class CacheHeroProp {
    public static Map<Integer, Map<Integer, HeroProp>> heroPropMap = GameUtil.createSimpleMap();

    public static void addOneLvProps(HeroCfg cfg) {
        if (!heroPropMap.containsKey(cfg.getId())) {
            heroPropMap.put(cfg.getId(), GameUtil.createSimpleMap());
        }
        Map<Integer, HeroProp> heroProps = heroPropMap.get(cfg.getId());

        int[] hps = cfg.getHps();
        int[] speeds = cfg.getSpeeds();
        int[] atts = cfg.getAttacks();
        int[] defs = cfg.getDefenses();
        int[] mags = cfg.getMagic_defenses();
        int[] tecs = cfg.getTechniques();
        int[] lcks = cfg.getLucks();
        {
            HeroProp props = new HeroProp();
            props.setHp(hps[0]);
            props.setSpeed(speeds[0]);
            props.setAttack(atts[0]);
            props.setDefense(defs[0]);
            props.setMagic_defense(mags[0]);
            props.setTechnique(tecs[0]);
            props.setLuck(lcks[0]);

            heroProps.put(1, props);
        }
    }

    public static void addFullLvProps(HeroCfg cfg) {
        if (!heroPropMap.containsKey(cfg.getId())) {
            heroPropMap.put(cfg.getId(), GameUtil.createSimpleMap());
        }
        Map<Integer, HeroProp> heroProps = heroPropMap.get(cfg.getId());

        int[] hps = cfg.getHps();
        int[] speeds = cfg.getSpeeds();
        int[] atts = cfg.getAttacks();
        int[] defs = cfg.getDefenses();
        int[] mags = cfg.getMagic_defenses();
        int[] tecs = cfg.getTechniques();
        int[] lcks = cfg.getLucks();
        {
            HeroProp props = new HeroProp();
            props.setHp(hps[1]);
            props.setSpeed(speeds[1]);
            props.setAttack(atts[1]);
            props.setDefense(defs[1]);
            props.setMagic_defense(mags[1]);
            props.setTechnique(tecs[1]);
            props.setLuck(lcks[1]);

            heroProps.put(HeroLvCfg.FULL_LEVEL, props);
        }
    }

    /**
     * 转职后满级属性
     * @param cfg
     */
    public static void addCareerFullLvProps(HeroCareerCfg cfg) {
        if (!heroPropMap.containsKey(cfg.getId())) {
            heroPropMap.put(cfg.getId(), GameUtil.createSimpleMap());
        }
        Map<Integer, HeroProp> heroProps = heroPropMap.get(cfg.getId());

        HeroProp props = new HeroProp();
        props.setHp(cfg.getMax_hp());
        props.setSpeed(cfg.getMax_speed());
        props.setAttack(cfg.getMax_attack());
        props.setDefense(cfg.getMax_defense());
        props.setMagic_defense(cfg.getMax_magic_defense());
        props.setTechnique(cfg.getMax_technique());
        props.setLuck(cfg.getMax_luck());

        heroProps.put(HeroLvCfg.FULL_LEVEL*2, props);
    }
}
