package com.cellsgame.game.cmd.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import com.cellsgame.game.Bootstrap;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by alyx on 17-7-6.
 * 一键下线所有玩家
 */
public class OutAll extends AConsoleCmd {
    @Override
    public Object getName() {
        return "outall";
    }

    @Override
    public Object exe(String cmd, String[] param) throws Exception {
        boolean b = param.length >= 2 && "in".equals(param[1]);

        Dispatch.dispatchGameLogic(() -> {
            if (b) {
                Bootstrap.allowPlayerIn.set(true);
            } else {
                Bootstrap.allowPlayerIn.set(false);

                Collection<PlayerVO> onlinePlayers = CachePlayer.getOnlinePlayers();
                List<PlayerVO> al = new ArrayList<>(onlinePlayers);
                PlayerBO bean = SpringBeanFactory.getBean(PlayerBO.class);
                for (PlayerVO playerVO : al) {
                    bean.offline(playerVO.getMessageController());
                }
            }
        });
        return null;
    }
}
