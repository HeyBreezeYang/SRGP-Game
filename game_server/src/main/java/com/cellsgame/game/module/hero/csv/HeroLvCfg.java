package com.cellsgame.game.module.hero.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

/**
 * 英雄升级配置表
 */
public class HeroLvCfg extends BaseCfg {
    // 40级满级
    public final static Integer FULL_LEVEL = 40;

    private int id;

    private int color;

    private int level;

    // 转职前所需经验
    private int exp1;
    // 转职前所需道具
    private List<FuncConfig> cost_items_1;
    // 转职前所需通用道具
    private List<FuncConfig> cost_general_items_1;
    // 转职后所需经验
    private int exp2;
    // 转职后所需道具
    private List<FuncConfig> cost_items_2;
    // 转职后所需通用道具
    private List<FuncConfig> cost_general_items_2;


    @Override
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

    public int getExp1() {
        return exp1;
    }

    public void setExp1(int exp1) {
        this.exp1 = exp1;
    }

    public List<FuncConfig> getCost_items_1() {
        return cost_items_1;
    }

    public void setCost_items_1(List<FuncConfig> cost_items_1) {
        this.cost_items_1 = cost_items_1;
    }

    public List<FuncConfig> getCost_general_items_1() {
        return cost_general_items_1;
    }

    public void setCost_general_items_1(List<FuncConfig> cost_general_items_1) {
        this.cost_general_items_1 = cost_general_items_1;
    }

    public int getExp2() {
        return exp2;
    }

    public void setExp2(int exp2) {
        this.exp2 = exp2;
    }

    public List<FuncConfig> getCost_items_2() {
        return cost_items_2;
    }

    public void setCost_items_2(List<FuncConfig> cost_items_2) {
        this.cost_items_2 = cost_items_2;
    }

    public List<FuncConfig> getCost_general_items_2() {
        return cost_general_items_2;
    }

    public void setCost_general_items_2(List<FuncConfig> cost_general_items_2) {
        this.cost_general_items_2 = cost_general_items_2;
    }
}
