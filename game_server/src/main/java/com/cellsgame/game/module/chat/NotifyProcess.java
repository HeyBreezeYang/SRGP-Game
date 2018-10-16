package com.cellsgame.game.module.chat;

import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public abstract class NotifyProcess {

    protected abstract String[] builderChatMsg(PlayerVO player, GameEvent e);

}
