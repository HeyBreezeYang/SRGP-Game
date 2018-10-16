package com.cellsgame.game.module.sys.funOpen;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * Created by alyx on 17-6-14.
 * 功能开启检查类型
 */
public enum FunOpenCheckType {
    PLAYER_LV(1, EvtTypePlayer.PlayerLevelUp, CodePlayer.Player_Level_Minus),
    ;

    private static BiMap<EvtType, FunOpenCheckType> type;

    static {
        ImmutableBiMap.Builder<EvtType, FunOpenCheckType> builder = ImmutableBiMap.builder();
        for (FunOpenCheckType checkType : FunOpenCheckType.values()) {
            EvtType key = checkType.listenEvt;
            if (key != null) {
                builder.put(key, checkType);
            }
        }
        type = builder.build();
    }

    private final int id;
    private final EvtType listenEvt;
    private final ICode code;

    FunOpenCheckType(int id, EvtType listenEvt, ICode code) {
        this.id = id;
        this.listenEvt = listenEvt;
        this.code = code;
    }

    public static FunOpenCheckType getByEvtType(EvtType evtType) {
        return type.get(evtType);
    }

    public int getId() {
        return id;
    }

    public EvtType getListenEvt() {
        return listenEvt;
    }

	public ICode getCode() {
		return code;
	}
    
}
