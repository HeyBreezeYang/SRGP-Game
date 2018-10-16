package com.cellsgame.game.core;


import java.util.Map;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.cellsgame.conc.disruptor.DpEvt;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.log.cons.LogType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHandler extends RunnableHandler {
    private static final Logger Logger = LoggerFactory.getLogger("OPLog");
    private static final Logger Log = LoggerFactory.getLogger(LogHandler.class);
    private static int gameServerId = 0;
    private static String logTitle = null;

    @Override
    public void onEvent(DpEvt event, long sequence, boolean endOfBatch) throws Exception {
        Object[] data = (Object[]) event.getData();
        CMD cmd = (CMD) data[0];
        EvtHolder player = (EvtHolder) data[1];
        LogType logType = (LogType) data[2];
        GameEvent e = (GameEvent) data[3];
        try (SerializeWriter out = new SerializeWriter()) {
            Map<String, Object> json = logType.getProcess().getJsonInfo(cmd, player, e);
            out.append(getLogTitle())
                    .append("|+|").append(String.valueOf(getGameServerId()))
                    .append("|+|").append(String.valueOf(logType.getType()))
                    .append("|+|");
            new JSONSerializer(out).write(json);
            Logger.info(out.toString());
        } catch (Throwable t) {
            Log.error("e.getType().toString() : " + e.getType().toString(), t);

        }
    }

    private String getLogTitle() {
        if (logTitle == null) {
            logTitle = GameConfig.getConfig().getLogTitle();
        }
        return logTitle;
    }

    private int getGameServerId() {
        if (0 == gameServerId) {
            gameServerId = GameConfig.getConfig().getGameServerId();
        }
        return gameServerId;
    }
}
