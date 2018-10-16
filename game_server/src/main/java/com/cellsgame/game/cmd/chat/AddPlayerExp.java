package com.cellsgame.game.cmd.chat;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.context.DefaultPlayerConfig;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on  2016-09-12.
 */
public class AddPlayerExp extends AChatCMD {
    @Override
    public Object getName() {
        return "ape";
    }

    @Override
    public Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception {
//    	src.getLeader();
        PlayerBO playerBO = SpringBeanFactory.getBean("playerBO");
        int exp= Integer.parseInt(parm[1]);
        return playerBO.changeExp(GameUtil.createSimpleMap(), src, exp, cmd);
    }
}
