package com.cellsgame.game.module.hero.cache;

import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;

public class HeroLv {
    private int id;

    private int color;

    // level代表转职前，转职后等级的展开形式
    private int level;

    private int exp;

    private List<FuncConfig> cost_items;

    private List<FuncConfig> cost_general_items;
    // Adjuster后设置的值 END ------


    // Adjuster后设置的值
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public List<FuncConfig> getCost_items() {
        return cost_items;
    }

    public void setCost_items(List<FuncConfig> cost_items) {
        this.cost_items = cost_items;
    }

    public List<FuncConfig> getCost_general_items() {
        return cost_general_items;
    }

    public void setCost_general_items(List<FuncConfig> cost_general_items) {
        this.cost_general_items = cost_general_items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
