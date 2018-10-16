package com.cellsgame.game.module.friend.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.friend.vo.FriendBlessVO;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CacheFriendBlessing {
    private static final Map<Integer, FriendBlessVO> blessingVOs = GameUtil.createSimpleMap();

    public static void addCache(FriendBlessVO vo) {
        blessingVOs.put(vo.getPid(), vo);
    }

    public static FriendBlessVO get(int pvoId, Function<Integer, FriendBlessVO> fun) {
        return blessingVOs.computeIfAbsent(pvoId, fun);
    }

    public static void forEach(BiConsumer<Integer, FriendBlessVO> consumer) {
        blessingVOs.forEach(consumer);
    }
}
