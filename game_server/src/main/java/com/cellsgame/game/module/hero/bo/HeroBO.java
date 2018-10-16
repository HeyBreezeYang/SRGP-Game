package com.cellsgame.game.module.hero.bo;

import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.player.vo.PlayerVO;

@AModule(ModuleID.Hero)
public interface HeroBO extends StaticEvtListener, IBuildData {

    void init();

    @Client(Command.HERO_SUMMON_IN)
    public Map summonIn(CMD cmd, PlayerVO pvo, @CParam("group")int group);

    @Client(Command.HERO_SUMMON_OUT)
    public Map summonOut(CMD cmd, PlayerVO pvo);

    @Client(Command.HERO_SUMMON)
    public Map summon(CMD cmd, PlayerVO pvo, @CParam("heroCId")int heroCId) throws LogicException;

    @Client(Command.HERO_LEVEL_UP)
    public Map levelUp(CMD cmd, PlayerVO pvo, @CParam("heroId")int heroId, @CParam("useItems")Map<Integer, Integer> useItems) throws LogicException;

    @Client(Command.HERO_STAR_UP)
    public Map starUp(CMD cmd, PlayerVO pvo, @CParam("heroId")int heroId, @CParam("useItems")Map<Integer, Integer> useItems) throws LogicException;

    @Client(Command.HERO_STAGE_UP)
    public Map stageUp(CMD cmd, PlayerVO pvo, @CParam("heroId1")int heroId1, @CParam("heroId2")int heroId2) throws LogicException;

    @Client(Command.HERO_CHAREER_NEW)
    public Map careerNew(CMD cmd, PlayerVO pvo, @CParam("heroId")int heroId, @CParam("useItems")Map<Integer, Integer> useItems) throws LogicException;

}
