package com.cellsgame.game.module.fight.impl;

import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.module.fight.IUnit;
import com.cellsgame.game.module.fight.cons.CodeFight;
import com.cellsgame.game.module.fight.skill.IBuff;
import com.cellsgame.game.module.fight.skill.impl.SkillBase;
import com.cellsgame.game.module.hero.cons.HeroType;
import com.cellsgame.game.module.hero.csv.HeroCfg;
import com.cellsgame.game.module.hero.vo.HeroVO;

import java.util.List;
import java.util.Map;

/**
 * 英雄实体
 */
public class HeroEntity implements IUnit<HeroEntity> {
    private int id;
    private int playerId;

    private int color;
    private int weapon;
    private int move;
    private int arm;
    private int type;

    private int hp;
    private int maxHp;

    private int speed;
    private int attack;
    private int defense;
    private int magicDefense;
    // 敏捷
    private int dexterity;

    private int attackDistance;

    private List<IBuff> buffs;

    private CombatInfo combatInfo;

    private HeroEntity target;

    private int x;
    private int y;

    HeroEntity(HeroCfg cfg) {
        type = cfg.getHero_type();
        color = cfg.getColor_type();
        weapon = cfg.getWeapon_type();
        move = cfg.getMove_type();

        // TODO 武器技上的属性
        attackDistance = 0;
    }

    public static HeroEntity createAsHero(HeroVO vo, int levelId) {

        HeroCfg cfg = CacheConfig.getCfg(HeroCfg.class, vo.getCid());
        HeroEntity entity = new HeroEntity(cfg);

        entity.setHp(vo.getHp());
        entity.setMaxHp(vo.getHp());

        entity.setSpeed(vo.getSpeed());
        entity.setAttack(vo.getAttack());
        entity.setDefense(vo.getDefense());
        entity.setMagicDefense(vo.getMagicDefense());
        entity.setDexterity(vo.getTechnique());

        // TODO 从关卡获取位置
        entity.setX(1);
        entity.setY(1);

        return entity;
    }

    public static HeroEntity createAsMonster(int heroCId, int levelId) {

        HeroCfg cfg = CacheConfig.getCfg(HeroCfg.class, heroCId);
        HeroEntity entity = new HeroEntity(cfg);

        // TODO 从关卡获取配置信息完成初始化
        entity.setHp(0);
        entity.setMaxHp(0);

        entity.setSpeed(0);
        entity.setAttack(0);
        entity.setDefense(0);
        entity.setMagicDefense(0);
        entity.setDexterity(0);

        // TODO 从关卡获取位置
        entity.setX(6);
        entity.setY(6);

        return entity;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() { return playerId; }

    public Map<Integer, SkillBase> getSkills() {
        // TODO 未实现
        return null;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getColor() { return color; }

    public int getWeapon() { return weapon; }

    public int getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) { this.attack = attack;}

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) { this.defense = defense; }

    public int getMagicDefense() {
        return magicDefense;
    }

    public void setMagicDefense(int magicDefense) { this.magicDefense = magicDefense;}

    public int getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(int attackDistance) { this.attackDistance = attackDistance; }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public List<IBuff> getBuffs() {
        return buffs;
    }

    public CombatInfo getCombatInfo() {
        return combatInfo;
    }

    public void setCombatInfo(CombatInfo combatInfo) {
        this.combatInfo = combatInfo;
    }

    public HeroEntity getTarget() {
        return target;
    }

    public void setTarget(HeroEntity target) {
        this.target = target;
    }

    public void takeDamage(int damage) {
        takeDamage(damage, 0);
    }

    public void takeDamage(int damage, int limit) {
    }

    public void addBuff(IBuff buff) {
        // TODO
        buffs.add(buff);
    }

    public boolean getAtk_twice() {
        // TODO 未实现
        return false;
    }
}
