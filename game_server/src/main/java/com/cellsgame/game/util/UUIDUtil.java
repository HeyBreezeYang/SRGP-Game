package com.cellsgame.game.util;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.StrIDUtil;
import com.cellsgame.game.context.GameConfig;

public class UUIDUtil {

    private static StrIDUtil util = new StrIDUtil(String.valueOf(SpringBeanFactory.getBean("gameConfig", GameConfig.class).getGameServerId()));

    public static String getUUID() {
        return util.getUUIDShort();
    }

}
