package com.cellsgame.game.module.chat.process;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * Created by yfzhang on 2017/7/24.
 */
public class EnterGameNotifyPro extends NotifyProcess {

    private NType type;

    private Map<Integer, Long> notifyTime = GameUtil.createSimpleMap();

    public enum NType {
        FightForceRank("fightforce"),
        Popular("popular")

        ;
        private String type;

        NType(String s){
            this.type = s;
        }

        public String getType(){
            return type;
        }
    }

    public EnterGameNotifyPro(NType type) {
        this.type = type;
    }


    @Override
    protected String[] builderChatMsg(PlayerVO player, GameEvent e) {
        long sysTime = System.currentTimeMillis();
        Long time = notifyTime.get(player.getId());
        if(time != null){
            if(sysTime - time < DateUtil.MIN_MILLIS * 10){
                return null;
            }
        }
        switch (this.type){
            case Popular:{
//                Integer pid = RankTypes.POPULAR.getKeyByRank(0);
//                if(pid != null && player.getId() == pid.intValue()){
//                    notifyTime.put(pid, sysTime);
//                    return new String[]{player.getName(), "1"};
//                }
                break;
            }
            case FightForceRank:{
//                Integer pid = RankTypes.FIGHT_FORCE.getKeyByRank(0);
//                if(pid != null && player.getId() == pid.intValue()){
//                    notifyTime.put(player.getId(), sysTime);
//                    return new String[]{player.getName(), "1"};
//                }
                break;
            }
        }
        return null;
    }

}