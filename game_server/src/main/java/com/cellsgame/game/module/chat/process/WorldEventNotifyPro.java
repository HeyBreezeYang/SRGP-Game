package com.cellsgame.game.module.chat.process;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2017/7/24.
 */
public class WorldEventNotifyPro extends NotifyProcess {

    @Override
    protected String[] builderChatMsg(PlayerVO player, GameEvent e) {
        int teamId = e.getParam(EvtParamType.MONSTER_TEAM_CID, 0);
        int eventId = e.getParam(EvtParamType.EVT_CID, 0);
        return new String[]{player.getName(), String.valueOf(eventId), String.valueOf(teamId)};
    }
}
