package com.cellsgame.game.core.socket;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.context.SessionController;

public class CacheMessageController {

    private static Map<SessionController, MessageController> cache = GameUtil.createMap();

    public static MessageController getController(SessionController session) {
        MessageController message = cache.get(session);
        if (message == null)
            cache.put(session, new MessageController(session));
        return cache.get(session);
    }

    public static MessageController reconnection(MessageController newController, MessageController oldController) {
    	cache.remove(oldController.getSession());
        oldController.newSession(newController.getSession());
        oldController.copyClientMessageId(newController);
        cache.put(newController.getSession(), oldController);
        return oldController;
    }

    public static void destroy(MessageController controller) {
        if(controller != null) {
            controller.sendCloseMesage();
            controller.destroy();
            if(controller.getSession() != null){
                cache.remove(controller.getSession());
            }
        }
    }
}
