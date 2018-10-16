package com.cellsgame.game.cmd.chat;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * @author Aly on 2017-03-13.
 */
public class AddGuildExp extends AChatCMD {
    @Override
    public Object getName() {
        return "addGuildExp";
    }

    @Override
    public Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception {
        Map<Object, Object> ret = GameUtil.createSimpleMap();
        GuildBO guildBO = SpringBeanFactory.getBean(GuildBO.class);
        if(src.getGuild() != null)
            guildBO.addGuildExp(cmd, ret, src.getGuild(), src, Integer.valueOf(parm[1]));
        return ret;
    }
}
