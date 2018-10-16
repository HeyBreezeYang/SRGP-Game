package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;

import java.util.Map;

/**
 * Created by yfzhang on 2017/8/1.
 */
public class FinishStoryLogPro extends LogProcess {

    private static String Chapter = "chapter";
    private static String Part = "part";
    private static String Index = "index";
    @Override
    public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
        Map<String, Object> result = GameUtil.createSimpleMap();
        int chapter = e.getParam(EvtParamType.Chapter, 0);
        int part = e.getParam(EvtParamType.Part, 0);
        int index = e.getParam(EvtParamType.Index, 0);
        result.put(Chapter, chapter);
        result.put(Part, part);
        result.put(Index, index);
        return result;
    }

}