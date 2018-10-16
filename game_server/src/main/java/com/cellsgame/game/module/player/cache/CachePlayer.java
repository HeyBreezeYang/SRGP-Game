package com.cellsgame.game.module.player.cache;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.cache.CacheBuilder;
import com.cellsgame.common.util.cache.LocalCache.LocalManualCache;
import com.cellsgame.common.util.cache.RemovalCause;
import com.cellsgame.common.util.cache.RemovalListener;
import com.cellsgame.common.util.cache.RemovalNotification;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.PlayerUtil;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.cons.PlayerInfoVOUpdateType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class CachePlayer {

    private static LocalManualCache<Integer, PlayerVO> cacheAllPlayers = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.HOURS)
            .removalListener(new RemovalListener<Integer, PlayerVO>() {
                @Override
                public void onRemoval(RemovalNotification<Integer, PlayerVO> notification) {
                    final PlayerVO player = notification.getValue();
                    if (player == null) return;
                    if (notification.getCause() == RemovalCause.REPLACED) return;
                    Dispatch.dispatchGameLogic(new Runnable() {
                        @Override
                        public void run() {
                            if (player.isOnline()) {
                                EvtTypePlayer.CacheTimeOut.happen(GameUtil.createSimpleMap(), CMD.cacheTimeout.now(), player);
                            }
                        }
                    });
                }
            }).build();

    private static Set<PlayerVO> cacheOnlinePlayers = new HashSet<>();

    private static Map<String, Integer> tokenMappingPlayer = GameUtil.createMap();

    private static Map<String, Integer> playerNameMappingPlayer = GameUtil.createMap();

    private static Table<String, Integer, Integer> accountIdAndServerIdMappingPlayer = HashBasedTable.create();


    public static void addPlayer(PlayerVO player) {
        cacheAllPlayers.put(player.getId(), player);
        playerNameMappingPlayer.put(player.getName(), player.getId());
        accountIdAndServerIdMappingPlayer.put(player.getAccountId(), player.getServerId(), player.getId());
    }

    public static void resetPlayerName(PlayerVO player, String oldName) {
        if (null == oldName) {
            return;
        }
        playerNameMappingPlayer.remove(oldName);
        playerNameMappingPlayer.put(player.getName(), player.getId());
    }

    public static PlayerVO getPlayerByAccount(Integer serverId, String accountId) {
        Integer playerId = accountIdAndServerIdMappingPlayer.get(accountId, serverId);
        if (playerId == null) return null;
        return getPlayerByPid(playerId);
    }

    public static PlayerVO getPlayerByToken(String token) {
        Integer playerId = tokenMappingPlayer.get(token);
        if (playerId == null) return null;
        return getPlayerByPid(playerId);
    }

    public static PlayerVO getPlayerByPlayerName(String playerName) {
        Integer playerId = playerNameMappingPlayer.get(playerName);
        if (playerId == null) return null;
        return getPlayerByPid(playerId);
    }

    public static void online(PlayerVO player) {
        player.setOnline(true);
        tokenMappingPlayer.put(player.getToken(), player.getId());
        cacheOnlinePlayers.add(player);
    }

    public static void offline(PlayerVO player) {
        player.setOnline(false);
        tokenMappingPlayer.remove(player.getToken());
        cacheOnlinePlayers.remove(player);
    }

    public static PlayerVO getPlayerByPid(int pid) {
        return cacheAllPlayers.getIfPresentNotClear(pid);
    }

//    /**
//     * 更新玩家简要数据
//     *
//     * @param playerVO 需要更新的玩家简要数据
//     */
//    public static SimplePlayerVO updatePlayerSimpleData(SimplePlayerVO playerVO) {
//        SimplePlayerVO vo = playerSimpleData.get(playerVO.getId());
//        // 如果不存在
//        if (vo == null) {
//            // 加入缓存
//            playerSimpleData.put(playerVO.getId(), vo = playerVO);
//        } else {
//            // 更新
//            vo.copyFrom(playerVO);
//        }
//        // 返回最新数据
//        return vo;
//    }

    /**
     * 获取所有在线玩家数据。
     * <p>
     * 此数据集只能查看, 不能添加或者删除.
     *
     * @return 在线玩家列表
     */
    public static Collection<PlayerVO> getOnlinePlayers() {
        return Collections.unmodifiableCollection(cacheOnlinePlayers);
    }


    public static void clearCache() {
        cacheAllPlayers.cleanUp();
    }
}
