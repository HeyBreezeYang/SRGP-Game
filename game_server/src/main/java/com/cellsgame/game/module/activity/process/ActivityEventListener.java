package com.cellsgame.game.module.activity.process;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cellsgame.game.core.event.EventTypeAll;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.cons.EvtTypeActivity;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * File Description.
 *
 * @author Yang
 */
public class ActivityEventListener implements StaticEvtListener {
    protected static final EvtType[] CARE_EVENTS = new EvtType[]{EventTypeAll.ALL};

    @Override
    public EvtType[] getListenTypes() {
        return CARE_EVENTS;
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
    	if(!(holder instanceof PlayerVO))
    		return parent;
    	PlayerVO player = (PlayerVO)holder;
    	Enum<?> o = event.getType();
        if (o instanceof EvtTypeActivity) {
            switch ((EvtTypeActivity) o) {
                case AcceptNewActivity:
                	Collection<ActivityVO> actVOs = event.getParam(EvtParamType.ACTIVITY_LIST);
        			ActivityManager.condListener(cmd, parent, event, actVOs, player);
                    break;
                case RefActivity:
                	ActivityVO actVO = event.getParam(EvtParamType.ACTIVITY);
                	if(ActivityManager.isListener(event, actVO)){
            			ActivityManager.condListener(cmd, parent, event, actVO, player);
            		}
                    break;
            }
        }else{
        	Set<ActivityVO> acts = CacheActivity.getRunningactivities();
			ActivityManager.condAndBevListener(cmd, parent, event, acts, player);
        }
        return parent;
    }
    
}
