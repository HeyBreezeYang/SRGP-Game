package com.cellsgame.game.module.chat.process;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * Created by yfzhang on 2017/7/24.
 */
public class TowerNotifyPro extends NotifyProcess {

    @Override
    protected    String[] builderChatMsg(PlayerVO player, GameEvent e) {
        boolean win = e.getParam(EvtParamType.WIN, false);
        if(win){
            int level = e.getParam(EvtParamType.DUP_CID, 0);
            BaseCfg config = e.getParam(EvtParamType.CONFIG);
            if(level % 6 == 5 && config != null ){
                return new String[]{player.getName(), String.valueOf(config.getId())};
            }
        }
        return null;
    }
}
