package com.cellsgame.game.module.player;

import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.SessionAttributeKey;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class PlayerUtil {

    // 解析客户端参数的时候 会自动调用这个方法
    public static PlayerVO checkAndGetPlayerVO(MessageController controller) throws LogicException {
        Integer playerId = controller.getAttribute(SessionAttributeKey.PLAYER_ID);
        CodeGeneral.General_NotLogin.throwIfTrue(null == playerId);
        PlayerVO player = CachePlayer.getPlayerByPid(playerId);
        CodeGeneral.General_NotLogin.throwIfTrue(null == player);
        return player;
    }

    public static PlayerVO getPlayerVO(MessageController controller) {
    	 Integer playerId = controller.getAttribute(SessionAttributeKey.PLAYER_ID);
         if(playerId == null)
        	 return null;
        return CachePlayer.getPlayerByPid(playerId);
    }
    

}
