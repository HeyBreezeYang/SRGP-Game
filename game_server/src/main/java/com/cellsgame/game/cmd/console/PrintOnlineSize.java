package com.cellsgame.game.cmd.console;

import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.module.player.cache.CachePlayer;


/**
 * @author Aly on  2016-07-18.
 */
public class PrintOnlineSize extends AConsoleCmd {

    @Override
    public Object getName() {
        return "online";
    }

    @Override
    public Object exe(String cmd, String[] param) {
        Dispatch.dispatchGameLogic(() -> {
            System.out.println("online size : " + CachePlayer.getOnlinePlayers().size());
        });
        return null;
    }
}
