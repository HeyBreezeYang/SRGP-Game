package com.cellsgame.game.module.player.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cons.SceneType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import sun.misc.Cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheScene {

    private static Map<SceneType, CacheScene> caches = GameUtil.createSimpleMap();


    public static CacheScene getScene(SceneType type){
        CacheScene scene = caches.get(type);
        if(scene == null){
            caches.put(type, scene = new CacheScene());
        }
        return scene;
    }

    private CacheScene(){}

    private Set<Integer> cachePlayers = new HashSet<>();

    private Map<Integer, List<Map>> cacheMsg = GameUtil.createSimpleMap();

    public void enter(PlayerVO playerVO){
        cachePlayers.add(playerVO.getId());
    }

    public void out(PlayerVO playerVO){
        cachePlayers.remove(playerVO.getId());
    }

    public void cacheMsg(int cmd, Map result){
        List<Map> lst = cacheMsg.get(cmd);
        if(lst == null){
            cacheMsg.put(cmd, lst = GameUtil.createList());
        }
        lst.add(result);
    }

    public void sendCacheMsg(){
        if(cacheMsg.isEmpty()) return;
        List<Integer> outs = GameUtil.createList();
        for (Integer playerId : cachePlayers) {
            PlayerVO playerVO = CachePlayer.getPlayerByPid(playerId);
            if(playerVO == null || !playerVO.isOnline()){
                outs.add(playerVO.getId());
                continue;
            }
            for (Map.Entry<Integer, List<Map>> entry : cacheMsg.entrySet()) {
                Push.multiThreadSend(playerVO.getMessageController(), MsgUtil.brmAll(entry.getValue(), entry.getKey()));
            }
        }
        cachePlayers.removeAll(outs);
        cacheMsg.clear();

    }

    public void sendMsg(Map result){
        List<Integer> outs = GameUtil.createList();
        for (Integer playerId : cachePlayers) {
            PlayerVO playerVO = CachePlayer.getPlayerByPid(playerId);
            if(playerVO == null || !playerVO.isOnline()){
                outs.add(playerVO.getId());
                continue;
            }
            Push.multiThreadSend(playerVO.getMessageController(), result);
        }
        cachePlayers.removeAll(outs);
    }

}
