package com.cellsgame.game.module;

import java.util.Date;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.NDateUtil;
import com.cellsgame.game.Bootstrap;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.SessionAttributeKey;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.process.IListener;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.cache.CacheName;
import com.cellsgame.game.module.player.cache.CachePlayerPI;
import org.apache.commons.lang3.StringUtils;

public class ConnectionListener extends IListener {

    private PlayerBO playerBO;

    @Override
    public void exec(MessageController controller, GameMessage message) {
        //连接刚创建需要做的业务
        if (message.getMessageType() == GameMessage.MessageType_Open) {
            Map<String, Object> result = GameUtil.createSimpleMap();
            result.put(MsgFactory.TIME, System.currentTimeMillis());
            result.put(MsgFactory.OPEN_SERVER_DATE, NDateUtil.sdf.get().format(Bootstrap.getInstance().getBootstrapConfig().getServerOpenTime()));
            Push.multiThreadSend(controller, MsgUtil.brmAll(result, Command.General_System_Time));
        }
        // 如果有随机名字
        String name = controller.getAndRemoveAttribute(SessionAttributeKey.Random_Name_Key);
        //
        if (StringUtils.isNotEmpty(name)) CacheName.release(name);
        // 连接关闭需要做的业务
        if (message.getMessageType() == GameMessage.MessageType_Close) {
            Integer playerId = controller.getAttribute(SessionAttributeKey.PLAYER_ID);
            if (playerId != null) CachePlayerPI.recordPI(playerId, System.currentTimeMillis());
        }
    }

    public PlayerBO getPlayerBO() {
        return playerBO;
    }

    public void setPlayerBO(PlayerBO playerBO) {
        this.playerBO = playerBO;
    }
}
