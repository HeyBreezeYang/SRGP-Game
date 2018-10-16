package com.cellsgame.game.module.friend.cache;

import com.cellsgame.common.util.collection.Trie;
import com.cellsgame.game.module.friend.ctx.FriendsContext;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;

/**
 * @author Aly @2017-02-07.
 */
public class CacheFriendNames {
    private static char[] nameSearchRange = null;
    public static final Trie<PlayerInfoVO> friendNameCache = new Trie<>(ch -> {
        if (null == nameSearchRange)
            nameSearchRange = FriendsContext.getCtx().getNameSearchRange();
        if (null != nameSearchRange) {
            int len = nameSearchRange.length / 2;
            for (int i = 0; i < len; i++) {
                if (ch >= nameSearchRange[2 * i] && ch <= nameSearchRange[2 * i + 1]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    });

}
