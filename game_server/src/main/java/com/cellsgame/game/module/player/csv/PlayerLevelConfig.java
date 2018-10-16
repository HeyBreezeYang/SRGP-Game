package com.cellsgame.game.module.player.csv;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

/**
 * 与玩家等级相关配置数据.
 * <p>
 * 如:
 * <p>
 * 玩家标准战斗力等。
 *
 * @author Yang
 */
public class PlayerLevelConfig extends BaseCfg {
    public static PlayerLevelConfig[] By_Level;
    private int level;// 等级
    private int reqExp;
    private int funcTimeLimit; //功能次数上限
    private int funcTimeRevCd; //功能次数恢复CD
    private int funcTimeRevNum; // 功能次数恢复数量
    private List<FuncConfig> upLevelPrize; // 升级奖励
    private int worshipPrz;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getReqExp() {
        return reqExp;
    }

    public void setReqExp(int reqExp) {
        this.reqExp = reqExp;
    }

    public int getFuncTimeLimit() {
        return funcTimeLimit;
    }

    public void setFuncTimeLimit(int funcTimeLimit) {
        this.funcTimeLimit = funcTimeLimit;
    }

    public int getFuncTimeRevCd() {
        return funcTimeRevCd;
    }

    public void setFuncTimeRevCd(int funcTimeRevCd) {
        this.funcTimeRevCd = funcTimeRevCd;
    }

    public int getFuncTimeRevNum() {
        return funcTimeRevNum;
    }

    public void setFuncTimeRevNum(int funcTimeRevNum) {
        this.funcTimeRevNum = funcTimeRevNum;
    }

    public List<FuncConfig> getUpLevelPrize() {
        return upLevelPrize;
    }

    public void setUpLevelPrize(List<FuncConfig> upLevelPrize) {
        this.upLevelPrize = upLevelPrize;
    }

    public int getWorshipPrz() {
        return worshipPrz;
    }

    public void setWorshipPrz(int worshipPrz) {
        this.worshipPrz = worshipPrz;
    }
}
