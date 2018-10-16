package com.cellsgame.game.module.player.cache;

import java.util.concurrent.TimeUnit;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.cache.CacheBuilder;
import com.cellsgame.common.util.cache.LocalCache;
import com.cellsgame.common.util.cache.RemovalCause;
import com.cellsgame.common.util.cache.RemovalListener;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2017/7/21.
 */
public class CachePlayerPI {

    private static LocalCache.LocalManualCache<Integer, Long> PI = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
//            .removalListener(new RemovalListener<String, Long>() {
//                @Override
//                public void onRemoval(RemovalNotification<String, Long> notification) {
//                    System.out.println(String.format("cache remove [entry=%s], reason=%s ", notification.toString(), notification.getCause()));
//                }
//            })
            .removalListener((RemovalListener<Integer, Long>) notification -> {
                final int playerId = notification.getKey();
                if (playerId <= 0) return;
                if (notification.getCause() == RemovalCause.REPLACED) return;
                Dispatch.dispatchGameLogic(() -> {
                    PlayerVO player = CachePlayer.getPlayerByPid(playerId);
                    if (player == null) return;
                    EvtTypePlayer.PI_Offline.happen(GameUtil.createSimpleMap(), CMD.PITimeout.now(), player);
                });
            })
            .build();

    public static void recordPI(PlayerVO player, long time) {
        recordPI(player.getId(), time);
    }

    public static void recordPI(int key, long time) {
        PI.put(key, time);
    }

    public static void clearCache() {
        PI.cleanUp();
    }

}
