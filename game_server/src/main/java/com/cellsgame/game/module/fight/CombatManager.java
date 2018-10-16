package com.cellsgame.game.module.fight;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.module.fight.cons.ReviseType;
import com.cellsgame.game.module.fight.impl.CombatInfo;
import com.cellsgame.game.module.fight.impl.HeroEntity;
import com.cellsgame.game.module.fight.skill.impl.SkillBase;
import com.cellsgame.game.module.fight.skill.impl.SkillSpecial;
import com.cellsgame.game.module.hero.cons.HeroColorType;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.cons.SkillType;
import com.cellsgame.game.module.skill.cons.SkillWeaponType;
import com.cellsgame.game.module.skill.csv.SkillWeaponCfg;

import java.util.List;
import java.util.Map;

/**
 * 战斗（一个回合中的）
 */
public class CombatManager implements EvtHolder {

    public class CombatEntity {
        public HeroEntity attacker;
        public HeroEntity defender;

        public int Attack;
        public int Defense;

        public double base_damage;
        public int final_damage;

        public HeroEntity cur_attacker;

        public List<HeroEntity> attacker_list;
        public boolean is_updata;

        public double dmg_rate;
        public int fix_dmg;


        public Map<ReviseType, Double> revises;
    }

    private static Map<Integer, CombatEntity> combats = GameUtil.createSimpleMap();

    /**
     * 执行攻击
     *
     * @param battle
     * @param attacker
     * @param defender
     */
    public static void doAtkAction(Battle battle, HeroEntity attacker, HeroEntity defender) {
        attacker.setTarget(defender);
        defender.setTarget(attacker);

        CombatEntity combat = battle.getCombat();
        combat.attacker = attacker;
        combat.defender = defender;
        combat.attacker_list.clear();

        // TODO 执行奥义技能

        initAtkData(combat);
        createNextAtk(combat);

        combat.is_updata = true;

        update(combat);
    }

    public static void update(CombatEntity combat) {
        while (true) {
            if (!combat.is_updata) {
                return;
            }
            if (combat.attacker_list.size() == 0) {
                return;
            }
            if (null == combat.cur_attacker) {
                switchAttacker(combat);
            }
            // TODO
            {
                combat.attacker_list.remove(0);

                createNextAtk(combat);
                switchAttacker(combat);
            }
        }
    }

    public static void initAtkData(CombatEntity combat) {
        HeroEntity attacker = combat.attacker;
        HeroEntity defender = combat.defender;
        {
            CombatInfo combatInfo = attacker.getCombatInfo();
            combatInfo.attack_count = 0;
            combatInfo.defend_count = 0;

            combatInfo.attack = false;
            combatInfo.can_chase = (attacker.getSpeed() - defender.getSpeed()) >= 5;
            combatInfo.chase = false;

            combatInfo.can_counter_chase = (attacker.getDexterity() > defender.getDexterity());
            combatInfo.chase = false;
        }
        {
            CombatInfo combatInfo = defender.getCombatInfo();
            combatInfo.attack_count = 0;
            combatInfo.defend_count = 0;

            combatInfo.attack = false;
            combatInfo.can_chase = (defender.getSpeed() - attacker.getSpeed()) >= 5;
            combatInfo.chase = false;

            combatInfo.can_counter_chase = (defender.getDexterity() > attacker.getDexterity());
            combatInfo.chase = false;
        }
    }

    public static void createNextAtk(CombatEntity combat) {
        CombatInfo attacker_info = combat.attacker.getCombatInfo();
        CombatInfo defender_info = combat.defender.getCombatInfo();

        // todo 生效点攻击开始前

        HeroEntity actor = null;
        if (! attacker_info.attack) {
            attacker_info.attack = true;
            actor = combat.attacker;
        } else if(defender_info.can_counter && !defender_info.counter) {
            defender_info.counter = true;
            actor = combat.defender;
        } else if( attacker_info.can_chase && !attacker_info.chase ){
            attacker_info.chase = true;
            actor = combat.attacker;
        } else if( defender_info.counter && defender_info.can_chase && !defender_info.chase) {
            defender_info.chase = true;
            actor = combat.defender;
        } else if( attacker_info.chase && defender_info.counter && defender_info.can_counter_chase && !defender_info.counter_chase ) {
            defender_info.counter_chase = true;
            actor = combat.defender;
        } else if( defender_info.chase && attacker_info.can_counter_chase && !attacker_info.counter_chase) {
            attacker_info.counter_chase = true;
            actor = combat.attacker;
        }

        if (null != actor ) {
            addAttacker(combat, actor, false);
        }
    }

    // TODO create_action to
    public static void addAttacker(CombatEntity combat, HeroEntity actor, boolean is_front) {
//        local action = coroutine.create(func);
//        table.insert(self.attacker_list, is_front and 1 or #self.attacker_list + 1, table.pack(action, self, actor));

        if (is_front) {
            combat.attacker_list.add(0, actor);
        } else {
            combat.attacker_list.add(actor);
        }
    }

    // TODO switch_action to switchAttacker
    public static void switchAttacker(CombatEntity combat) {
//        local params = combat.attacker_list.get(0);
//        if (null == params ) {
//            combat.cur_attacker = null;
//            return;
//        }
//
//        combat.cur_attacker = params[1];
//        coroutine.resume(table.unpack(params));
        // TODO
        combat.cur_attacker = combat.attacker_list.get(0);

        doAtk(combat, combat.cur_attacker, combat.cur_attacker.getTarget());
    }

    public static void finishAtkList(CombatEntity combat) {
        combat.is_updata = false;
        combat.cur_attacker = null;
        combat.attacker_list.clear();

//        if (onActionCompleteCallback()) {
//
//        }
    }
    /**
     * 攻击
     */
    public static void doAtk(CombatEntity combat, HeroEntity attacker, HeroEntity defender) {

        resetCaculateValue(combat);
        setAtkDefValue(combat, attacker, defender);
        setReviseValue(combat, attacker, defender);

        onAtkComplete(combat, attacker, defender);
    }

    public static void onAtkComplete(CombatEntity combat, HeroEntity attacker, HeroEntity defender) {
        defender.takeDamage(getFinalDamage(combat));

        {
            SkillSpecial special = (SkillSpecial)attacker.getSkills().get(SkillType.SPECIAL.getValue());
            if (null != special) special.reduce_cd_attack();
        }

        {
            SkillSpecial special = (SkillSpecial)defender.getSkills().get(SkillType.SPECIAL.getValue());
            if (null != special) special.reduce_cd_defense();
        }

        // TODO 发送事件

        if (defender.getHp() == 0) {
            finishAtkList(combat);
        }
        // TODO
    }


    public static void setAtk(CombatEntity combat, Integer val) {
        combat.Attack = val;
    }

    public static void setDef(CombatEntity combat, Integer val) {

        combat.Defense = val;
    }

    /**
     * 设置武器特效修正
     */
    public static void setWeaponRevise(CombatEntity combat, SkillBase skill_weapon, int defender_weapon) {
        combat.revises.remove(ReviseType.WEAPON);

        SkillWeaponCfg cfg = CacheSkill.skillWeaponMap.get(skill_weapon.getCid());
        if (null != cfg) {
            int[] counters = cfg.getWeapon_counters();
            for (int i = 0; i < counters.length; i++) {
                if (defender_weapon == counters[i]) {
                    combat.revises.put(ReviseType.WEAPON, 0.5d);
                    break;
                }
            }
        }
    }

    /**
     * 设置兵种特效修正
     */
    public static void setArmsRevise(CombatEntity combat, SkillBase skill_weapon, int defender_arms) {
        combat.revises.remove(ReviseType.ARMS);

        SkillWeaponCfg cfg = CacheSkill.skillWeaponMap.get(skill_weapon.getCid());
        if (null != cfg) {
            int[] counters = cfg.getArms_counters();
            for (int i = 0; i < counters.length; i++) {
                if (defender_arms == counters[i]) {
                    combat.revises.put(ReviseType.ARMS, 0.5d);
                    break;
                }
            }
        }
    }

    /**
     * 设置相性修正
     * @param combat
     * @param attacker_color
     * @param defender_color
     */
    public static void setColorRevise(CombatEntity combat, int attacker_color, int defender_color) {
        combat.revises.remove(ReviseType.COLOR);
        combat.revises.put(ReviseType.COLOR, getColorRevise(combat, attacker_color, defender_color));
    }
    /**
     * 获取相性修正
     */
    public static double getColorRevise(CombatEntity combat, int attacker_color, int defender_color) {
        double ret = 0.0d;
        if ((HeroColorType.RED.getValue() == attacker_color && HeroColorType.GREEN.getValue() == defender_color) ||
                (HeroColorType.GREEN.getValue() == attacker_color && HeroColorType.BLUE.getValue() == defender_color) ||
                (HeroColorType.BLUE.getValue() == attacker_color && HeroColorType.RED.getValue() == defender_color)) {
            ret = 0.2d;
        } else if ((HeroColorType.RED.getValue() == attacker_color && HeroColorType.BLUE.getValue() == defender_color) ||
                (HeroColorType.GREEN.getValue() == attacker_color && HeroColorType.RED.getValue() == defender_color) ||
                (HeroColorType.BLUE.getValue() == attacker_color && HeroColorType.GREEN.getValue() == defender_color)) {
            ret = -0.2d;
        }
        return ret;
    }

    /**
     * 设置杖修正
     */
    public static void setStaffRevise(CombatEntity combat, int weapon) {
        combat.revises.remove(ReviseType.STAFF);
        if (SkillWeaponType.STAFF.getValue() == weapon) {
            combat.revises.put(ReviseType.STAFF, 0.5d);
        }
    }

    /**
     * 伤害倍率修正
     */
    public static void changeDamageRate(CombatEntity combat, float val) {
        combat.dmg_rate = combat.dmg_rate + val;
    }

    /**
     * 固定伤害修正
     */
    public static void changeFixDmg(CombatEntity combat, int val) {
        combat.fix_dmg = combat.fix_dmg + val;
    }

    private static double getEffectiveRevise(CombatEntity combat) {
        if (combat.revises.containsKey(ReviseType.WEAPON)) {
            return combat.revises.get(ReviseType.WEAPON);
        }
        if (combat.revises.containsKey(ReviseType.ARMS)) {
            return combat.revises.get(ReviseType.ARMS);
        }
        return 0.0f;
    }

    private static double getRevise(CombatEntity combat, ReviseType type, double default_value) {
        if (combat.revises.containsKey(type)) {
            return combat.revises.get(type);
        }
        return default_value;
    }

    /**
     * 获取基础伤害
     */
    public static double getBaseDamage(CombatEntity combat) {
        double effective_revise = getEffectiveRevise(combat);
        double color_revise = getRevise(combat, ReviseType.COLOR, 0.0f);
        double ret = combat.Attack * (1 + effective_revise) * (1 + color_revise) - combat.Defense;
        combat.base_damage = ret;
        return ret;
    }

    /**
     * 获取最终伤害
     */
    public static int getFinalDamage(CombatEntity combat) {
        double staff_revise = getRevise(combat, ReviseType.STAFF, 1.0F);
        int ret = (int) Math.floor(getBaseDamage(combat) * (staff_revise + combat.dmg_rate) + combat.fix_dmg);
        if (ret < 0) {
            ret = 0;
        }
        combat.final_damage = ret;
        return ret;
    }

    /**
     * 设置攻击、防御
     */
    public static void setAtkDefValue(CombatEntity combat, HeroEntity attacker, HeroEntity defender) {
        setAtk(combat, attacker.getAttack());
        int def = 0;
        if (isMagicWeapon(attacker.getWeapon())) {
            def = defender.getMagicDefense();
        } else {
            def = defender.getDefense();
        }


        setDef(combat, def);
    }

    /**
     * 设置伤害修正数据
     */
    public static void setReviseValue(CombatEntity combat, HeroEntity attacker, HeroEntity defender) {
        SkillBase skill = attacker.getSkills().get(SkillType.WEAPON.getValue());
        setWeaponRevise(combat, skill, defender.getWeapon());
        // TODO actorType?
//        setArmsRevise(combat, skillVO, defender.actorType);
        setColorRevise(combat, attacker.getColor(), defender.getColor());
        setStaffRevise(combat, attacker.getWeapon());
    }

    /**
     * 重置伤害计算数据
     */
    public static void resetCaculateValue(CombatEntity combat) {
        combat.Attack = 0;
        combat.Defense = 0;

        combat.revises.clear();

        combat.dmg_rate = 0;
        combat.fix_dmg = 0;
    }

    public static List<Map> getPreAtkData(CombatEntity combat, HeroEntity attacker, HeroEntity defender) {
        List<Map> ret = GameUtil.createList();
        int pre_damage = 0;
        int atk_damage = 0;
        int times = 1;

        resetCaculateValue(combat);
        setAtkDefValue(combat, attacker, defender);
        setReviseValue(combat, attacker, defender);

        SkillSpecial skill_special = (SkillSpecial) attacker.getSkills().get(SkillType.SPECIAL.getValue());
        if (null != skill_special && skill_special.is_group_attack() && skill_special.getCd() == 0) {
            // TODO 未实现
        }

        atk_damage = getFinalDamage(combat);

        int twice_times = attacker.getAtk_twice() ? 2 : 1;
        int chase_times = (attacker.getSpeed() - defender.getSpeed()) >= 5 ? 2 : 1;

        times = twice_times * chase_times;

        Map<String, Integer> attacker_show = GameUtil.createSimpleMap();
        attacker_show.put("pre_damage", pre_damage);
        attacker_show.put("atk_damage", atk_damage);
        attacker_show.put("times", times);


        pre_damage = 0;
        atk_damage = 0;
        times = 1;

        resetCaculateValue(combat);
        setAtkDefValue(combat, defender, attacker);
        setReviseValue(combat, defender, attacker);

        skill_special = (SkillSpecial) attacker.getSkills().get(SkillType.SPECIAL.getValue());
        if (null != skill_special && skill_special.is_group_attack() && skill_special.getCd() == 0) {
            // TODO 未实现
        }


        atk_damage = getFinalDamage(combat);

        twice_times = defender.getAtk_twice() ? 2 : 1;
        chase_times = (attacker.getSpeed() - defender.getSpeed()) >= 5 ? 2 : 1;
        times = twice_times * chase_times;

        Map<String, Integer> defender_show = GameUtil.createSimpleMap();
        defender_show.put("pre_damage", pre_damage);
        defender_show.put("atk_damage", atk_damage);
        defender_show.put("times", times);

        ret.add(attacker_show);
        ret.add(defender_show);
        return ret;
    }

    /**
     * 判断武器是否为魔法武器
     */
    public static boolean isMagicWeapon(Integer weapon) {
        if (weapon == SkillWeaponType.BOOK.getValue() ||
        weapon == SkillWeaponType.DRAGON_STONE.getValue() ||
        weapon == SkillWeaponType.STAFF.getValue()) {
            return true;
        }
        return false;
    }

    /**
     * 判断武器是否为远程武器
     */
    public static boolean isRangedWeapon(Integer weapon) {
        if (weapon == SkillWeaponType.SWORD.getValue() ||
                weapon == SkillWeaponType.AXE.getValue() ||
                weapon == SkillWeaponType.GUN.getValue()) {
            return false;
        }

        return true;
    }
}
