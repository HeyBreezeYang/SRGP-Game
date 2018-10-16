package com.cellsgame.game.module.chat.process;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2017/7/24.
 */
public class PlayerSkillBreakNotifyPro extends NotifyProcess {

    @Override
    protected String[] builderChatMsg(PlayerVO player, GameEvent e) {
        int skillCid = e.getParam(EvtParamType.Skill_CID, 0);
        int level = e.getParam(EvtParamType.LEVEL, 0);
        if(skillCid > 0 && level >= 20){
            return new String[]{player.getName(), String.valueOf(skillCid), String.valueOf(level)};
        }
        return null;
    }
}