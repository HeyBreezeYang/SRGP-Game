package com.cellsgame.game.module.hero.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

/**
 * 英雄突破配置表
 */
public class HeroStageCfg extends BaseCfg {
    public final static Integer FULL_STAGE = 10;

    // Map<CID, Map<LEVEL, HeroStageCfg>>
    public static Map<Integer, Map<Integer, HeroStageCfg>> configs = GameUtil.createSimpleMap();;

    private int id;

    // 英雄配置ID
    private int hero_id;

    // 突破等级
    private int level;

    // 对应突破等级属性累加值 ---BEGIN
    private int hp;

    private int speed;

    private int attack;

    private int defense;

    private int magic_defense;

    private int technique;

    private int luck;
    // 对应突破等级属性累加值 ---END


    // 突破所需道具
    private List<FuncConfig> stage_costs;


    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic_defense() {
        return magic_defense;
    }

    public void setMagic_defense(int magic_defense) {
        this.magic_defense = magic_defense;
    }

    public int getTechnique() {
        return technique;
    }

    public void setTechnique(int technique) {
        this.technique = technique;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public List<FuncConfig> getStage_costs() {
        return stage_costs;
    }

    public void setStage_costs(List<FuncConfig> cost_items) {
        this.stage_costs = cost_items;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
