package com.cellsgame.game.module.log.cons;

import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.log.impl.*;
import com.cellsgame.game.module.mail.cons.MailEventType;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.quest.cons.EventTypeQuest;
import com.cellsgame.game.module.shop.evt.EvtTypeShop;

public enum LogType {

    CREATE_PLAYER(1, new EvtType[]{EvtTypePlayer.CreatePlayer}, new PlayerCreateLogPro()),
    ENTERGAME_OFFLINE(2, new EvtType[]{EvtTypePlayer.EnterGame, EvtTypePlayer.Offline}, new PlayerEnterGameAndOfflineLogPro()),
    OFFLINE(3, new EvtType[]{EvtTypePlayer.Offline}, new PlayerOfflineLogPro()),
    PLAYER_UP_LEVEL(4, new EvtType[]{EvtTypePlayer.LevelUp}, new PlayerLevelLogPro()),
    CURRENCY(5, new EvtType[]{ EventTypeDepot.Currency}, new CurrencyLogPro()),
    GOODS(6, new EvtType[]{ EventTypeDepot.Goods}, new GoodsLogPro()),
	GUILD_DONATE(7, new EvtType[]{EvtTypeGuild.Donate}, new GuildDonateLogPro()),
    GUILD_MONEY(8, new EvtType[]{EvtTypeGuild.MoneyChange}, new GuildMoneyLogPro()),
    Shop(9, new EvtType[]{EvtTypeShop.BUY, EvtTypeShop.Refresh}, new PassShopPro()),
	FINISH_PLOT(10, new EvtType[]{EvtTypePlayer.FinishPlot}, new FinishPlotLogPro()),

    GUILD(14, new EvtType[]{EvtTypeGuild.Dissolution, EvtTypeGuild.Create, EvtTypeGuild.Join}, new GuildLogPro()),
    GUILD_BOSS(15, new EvtType[]{EvtTypeGuild.FightBoss,EvtTypeGuild.OpenBoss,EvtTypeGuild.KillBoss}, new GuildBossLogPro()),
    QUEST_FIN(16, new EvtType[]{EventTypeQuest.QUEST_FIN}, new QuestFinLogPro()),
    
    ONLINE_SIZE(37, new EvtType[]{EvtTypePlayer.OnlineSize}, new OnlineSizeLogPro()),
	MAIL(99, new EvtType[]{MailEventType.Send}, new MailLogPro()),
	;
    private int type;

    private EvtType[] evtTypes;

    private LogProcess process;


    LogType(int t, EvtType[] evtTypes, LogProcess process) {
        this.type = t;
        this.evtTypes = evtTypes;
        this.process = process;
    }

    public int getType() {
        return type;
    }

    public EvtType[] getEvtTypes() {
        return evtTypes;
    }

    public LogProcess getProcess() {
        return process;
    }

}
