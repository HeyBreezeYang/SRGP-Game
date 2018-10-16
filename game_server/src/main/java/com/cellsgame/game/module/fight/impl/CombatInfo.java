package com.cellsgame.game.module.fight.impl;

public class CombatInfo {
    public boolean can_counter = false;
    public boolean counter = false;

    public int attack_count = 0;
    public int defend_count = 0;

    public boolean attack = false;
    public boolean can_chase = false;
    public boolean chase = false;
    public boolean can_counter_chase = false;
    public boolean counter_chase = false;

    CombatInfo(HeroEntity attacker, HeroEntity defender) {
        can_chase = (attacker.getSpeed() - defender.getSpeed()) >= 5;
        can_counter_chase = (attacker.getDexterity() > defender.getDexterity());
    }
}
