package com.cellsgame.game.module.sys.funOpen;

import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by alyx on 17-6-14.
 * 功能开启事件检查
 */
public class FunOpenListener implements StaticEvtListener {

    @Override
    public EvtType[] getListenTypes() {
        FunOpenCheckType[] values = FunOpenCheckType.values();

        EvtType[] evtTypes = new EvtType[values.length + 1];
        for (int i = 0; i < values.length; i++) {
            evtTypes[i + 1] = values[i].getListenEvt();
        }
        evtTypes[0] = EvtTypePlayer.EnterGame;
        return evtTypes;
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        if (!(holder instanceof PlayerVO)) return parent;
        PlayerVO pvo = (PlayerVO) holder;

        // 登录游戏检查所有
        EvtType eventType = event.getType();
        if (eventType == EvtTypePlayer.EnterGame) {
            for (FunType type : FunType.values()) {
                parent = checkAndInvoke(parent, pvo, type, cmd);
            }
        } else {
            FunOpenCheckType checkType = FunOpenCheckType.getByEvtType(eventType);
            if (null == checkType) return parent;

            List<FunType> cfgs = FunType.getByCheckType(checkType);
            if (CollectionUtils.isEmpty(cfgs)) return parent;

            for (FunType funType : cfgs) {
                checkAndInvoke(parent, pvo, funType, cmd);

            }
        }
        return parent;
    }

    private Map checkAndInvoke(Map parent, PlayerVO pvo, FunType funType, CMD cmd) {
        if (funType.param == null || funType.listenOpen == null) {
            return parent;
        }
        if (funType.checkPre(pvo)) {
            // do on event function
            parent = funType.listenOpen.listen(cmd, parent, pvo);
        }
        return parent;
    }
}
