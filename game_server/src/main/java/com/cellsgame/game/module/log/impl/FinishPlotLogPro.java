package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

/**
 * Created by yfzhang on 2017/8/1.
 */
public class FinishPlotLogPro extends LogProcess {

    private static String STORY_ID = "sId";
    @Override
    public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
        Map<String, Object> result = GameUtil.createSimpleMap();
        result.put(STORY_ID, e.getParam(EvtParamType.PLOT_ID, -1));
        return result;
    }

}