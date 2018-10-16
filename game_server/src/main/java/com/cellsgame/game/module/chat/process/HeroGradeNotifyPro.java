package com.cellsgame.game.module.chat.process;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * Created by yfzhang on 2017/7/24.
 */
public class HeroGradeNotifyPro extends NotifyProcess {

    @Override
    protected String[] builderChatMsg(PlayerVO player, GameEvent e) {
        int heroCid = e.getParam(EvtParamType.HERO_CID, 0);
        int grade =  e.getParam(EvtParamType.GRADE, 0);
        if (heroCid > 0) {
            if(grade == 4 || grade == 7 || grade == 11) {
                return new String[]{player.getName(), String.valueOf(heroCid), String.valueOf(grade)};
            }
         }
        return null;
    }
}
