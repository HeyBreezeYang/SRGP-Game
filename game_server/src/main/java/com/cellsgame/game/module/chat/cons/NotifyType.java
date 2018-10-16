package com.cellsgame.game.module.chat.cons;

import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.chat.NotifyProcess;
import com.cellsgame.game.module.chat.process.*;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;

public enum NotifyType {
    PLAYERSKILL_BREAK(10, new EvtType[]{EvtTypePlayer.SkillBreak}, new PlayerSkillBreakNotifyPro()),
    FIGHTFORCE_TOP1(13, new EvtType[]{EvtTypePlayer.EnterGame}, new EnterGameNotifyPro(EnterGameNotifyPro.NType.FightForceRank)),
    POPULAR_TOP1(14, new EvtType[]{EvtTypePlayer.EnterGame}, new EnterGameNotifyPro(EnterGameNotifyPro.NType.Popular)),
    SYSTEM(15, new EvtType[]{}, null),
    WORLD_EVENT(16, new EvtType[]{}, null),
    ;

    private int type;

    private EvtType[] evtTypes;

    private NotifyProcess process;

    NotifyType(int type, EvtType[] evtTypes, NotifyProcess process) {
        this.type = type;
        this.evtTypes = evtTypes;
        this.process = process;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public EvtType[] getEvtTypes() {
        return evtTypes;
    }

    public void setEvtTypes(EvtType[] evtTypes) {
        this.evtTypes = evtTypes;
    }

    public NotifyProcess getProcess() {
        return process;
    }

    public void setProcess(NotifyProcess process) {
        this.process = process;
    }
}
