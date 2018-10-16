package com.cellsgame.game.module.chat.process;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * Created by yfzhang on 2017/7/24.
 */
public class ArenaNotifyPro extends NotifyProcess {

    @Override
    protected String[] builderChatMsg(PlayerVO player, GameEvent e) {
        boolean win = e.getParam(EvtParamType.WIN, false);
        if(win){
            int oldGrade = e.getParam(EvtParamType.OLD_GRADE, 0);
            int newGrade = e.getParam(EvtParamType.GRADE, 0);
            int rankPoint = e.getParam(EvtParamType.RANK, 0);
            if(oldGrade !=  newGrade && newGrade <= 11){
                return new String[]{player.getName(), String.valueOf(newGrade), String.valueOf(rankPoint)};
            }
        }
        return null;
    }
}