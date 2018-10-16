package com.cellsgame.game.cmd.chat;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * @author Aly on  2016-09-12.
 */
public class AddPlayerVipExp extends AChatCMD {
    @Override
    public Object getName() {
        return "addVipExp";
    }

    @Override
    public Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception {
        PlayerBO playerBO = SpringBeanFactory.getBean("playerBO");
        int exp= Integer.parseInt(parm[1]);
        return playerBO.changeVipExp(GameUtil.createSimpleMap(), src, exp, cmd);
    }
}
