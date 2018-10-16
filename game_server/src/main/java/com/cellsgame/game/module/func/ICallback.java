package com.cellsgame.game.module.func;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public interface ICallback {

    /**
     * @param callbackParam 正常情况下返回 {@link FightDTO} 错误清空下返回 code (int)
     */
    void callback(Map<?, ?> parent, PlayerVO player, CMD cmd, Object callbackParam) throws LogicException;

    /**
     * 异步执行发生异常后是否继续调用向下调用
     * 如果继续向下调用 {@link ICallback#callback(Map, PlayerVO, CMD, Object)}中回掉参数 为错误码  int
     * 异常 依然会按以前的方式返回给客户端消息
     */
    default boolean dispatchException() {
        return false;
    }

}
