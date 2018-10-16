package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.cons.CheckRecType;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGoods;
import com.cellsgame.game.module.hero.msg.CodeHero;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import javax.annotation.Resource;

/**
 * 技能学习检查
 */
public class SkillLearnChecker implements IChecker {
    private HeroVO hero;

    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
        CodeHero.HERO_NOT_FOUND.throwIfTrue(hero.getStar() < param.getValue());
    }

    public SkillLearnChecker setHero(HeroVO hero) {
        this.hero = hero;
        return this;
    }
}
