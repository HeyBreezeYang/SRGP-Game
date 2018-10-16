package com.cellsgame.game.module.skill.bo;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.List;
import java.util.Map;

@AModule(ModuleID.Skill)
public interface SkillBO extends IBuildData {
    @Client(Command.SKILL_LEARN)
    Map heroSkillLearn(PlayerVO pvo, @CParam("heroId") int heroId, @CParam("skillCId") int skillCId, CMD cmd) throws LogicException;

    @Client(Command.SKILL_INHERIT)
    Map heroSkillInherit(PlayerVO pvo, @CParam("heroId1") int heroId1, @CParam("heroId2") int heroId2, @CParam("skills") List<Integer> inheritSkills, CMD cmd) throws LogicException;

    // TODO 圣印不使用该接口
    @Client(Command.SKILL_EQUIP_ON)
    Map heroSkillEquipOn(PlayerVO pvo, @CParam("heroId") int heroId, @CParam("skillId") int skillId, CMD cmd) throws LogicException;
    // TODO 圣印不使用该接口
    @Client(Command.SKILL_EQUIP_OFF)
    Map heroSkillEquipOff(PlayerVO pvo, @CParam("heroId") int heroId, @CParam("skillId") int skillId, CMD cmd) throws LogicException;

    @Client(Command.SEAL_CREATE)
    Map sealCreate(PlayerVO pvo, @CParam("skillId") int skillId, CMD cmd) throws LogicException;

    @Client(Command.SEAL_UPGRADE)
    Map sealUpgrade(PlayerVO pvo, @CParam("skillId") int skillId, CMD cmd) throws LogicException;

    // TODO 圣印使用该接口
    @Client(Command.SEAL_EQUIP_ON)
    Map heroSealEquipOn(PlayerVO pvo, @CParam("heroId") int heroId, @CParam("skillId") int skillId, CMD cmd) throws LogicException;
    // TODO 圣印使用该接口
    @Client(Command.SEAL_EQUIP_OFF)
    Map heroSealEquipOff(PlayerVO pvo, @CParam("heroId") int heroId, @CParam("skillId") int skillId, CMD cmd) throws LogicException;
}
