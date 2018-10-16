package com.cellsgame.game.module.test;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.hero.bo.impl.HeroBOImpl;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public class HeroTest extends Bootstrap {
    private PlayerVO pvo;
    private HeroBOImpl heroBO;

    public HeroTest(PlayerVO pvo) throws Exception {
        super();

        this.pvo = pvo;

        this.heroBO = SpringBeanFactory.getBean("heroBO");
    }

    public void commonIn() {

    }

    public void summonOut() {

    }

    public HeroVO summon(int heroCId) {
        CMD cmd = CMD.system.now();
        cmd.setCmd(Command.HERO_SUMMON);
        cmd.setName("HeroBO.summon");

        Map map = heroBO.summon(cmd, pvo, heroCId);
        return null;
    }

    public void levelUp() {

    }

    public void starUp() {

    }

    public void stageUp() {

    }

    public void chareerExchange() {

    }

    public PlayerVO getPvo() {
        return pvo;
    }

    public void setPvo(PlayerVO pvo) {
        this.pvo = pvo;
    }

    public static void main(String args[]) throws Exception {
        PlayerVO pvo = CachePlayer.getPlayerByPlayerName("player_hero_test");
        if (null == pvo) {
            pvo = PlayerTest.createPlayer("player_hero_test", 0);
        }

        HeroTest test = new HeroTest(pvo);

        test.setPvo(pvo);

        test.summon(1);
    }
}
