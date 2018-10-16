package com.cellsgame.game.module.test;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.BuildPlayerProcess;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

public class PlayerTest {
    public static PlayerVO createPlayer(String playerName, int img) {
        BaseDAO<PlayerVO> playerDAO = SpringBeanFactory.getBean("playerDAO");
        BuildPlayerProcess playerBuilder = SpringBeanFactory.getBean("playerBuilder");
        PlayerVO player = new PlayerVO();
        player.setAccountId("accountId");
        player.setServerId(20180924);
        player.setName(playerName);
        player.writeToDBObj();

        playerDAO.save(player);
        playerBuilder.buildAsCreate(player, null);

        CachePlayer.addPlayer(player);
        CachePlayerBase.addBaseInfo(player);

        return player;
    }

    public static void removePlayer(Integer pid) {
        BaseDAO<PlayerVO> playerDAO = SpringBeanFactory.getBean("playerDAO");
        PlayerVO vo = CachePlayer.getPlayerByPid(pid);
        if(null != vo) playerDAO.delete(vo);
    }

    public static void main(String args[]) {
        
    }
}
