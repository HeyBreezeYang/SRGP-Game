package com.cellsgame.game.module.func;

import java.util.Map;

import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AsyncFunc extends AbstractFunc {

    private static final Logger log = LoggerFactory.getLogger(AsyncFunc.class);

    public Object exec(CMD cmd, Map<?, ?> parent, PlayerVO player, Object params, ICallback callback) {
        DispatchType.tryExeIn(getExecDisruptor(), new Runnable() {
            @Override
            public void run() {
                Object endParam;
                try {
                    endParam = asyncExec(parent, player, params);
                    end(cmd, callback, parent, player, endParam);
                } catch (Throwable e) {
                    onException(player, cmd, params, e);
                    if (callback.dispatchException()) {
                        end(cmd, callback, parent, player, e);
                    }
                }
            }
        });
        return null;
    }

    protected void onException(PlayerVO player, CMD cmd, Object params, Throwable e) {
        log.info("AsyncFunc onException ", e);
        if (null != player) {
            Map ret;
            if (e instanceof LogicException) {
                ret = MsgUtil.brmAll(null, cmd.getCmd(), ((LogicException) e).getCode());
            } else {
                log.error("on Exception ", e);
                ret = MsgUtil.brmAll(null, cmd.getCmd(), CodeGeneral.General_ServerException);
            }
            if (ret != null)
                Push.multiThreadSend(player.getMessageController(), ret);
        } else {
            log.warn("{}", params, e);
        }
    }


    public void end(CMD cmd, ICallback callback, Map<?, ?> parent, PlayerVO player, Object endParam) {
        DispatchType.tryExeIn(getEndDisruptor(), new Runnable() {
            @Override
            public void run() {
                log.info("AsyncFunc end ");
                try {
                    callback.callback(parent, player, cmd, endParam);
                } catch (Throwable e) {
                    onException(player, cmd, endParam, e);
                }
            }
        });
    }

    public abstract Object asyncExec(Map<?, ?> parent, PlayerVO player, Object params) throws LogicException;

    public abstract DispatchType getExecDisruptor();

    public abstract DispatchType getEndDisruptor();


    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param, int execNum) throws LogicException {
        CodeGeneral.General_Func_Error.throwException();
        return null;
    }


}
