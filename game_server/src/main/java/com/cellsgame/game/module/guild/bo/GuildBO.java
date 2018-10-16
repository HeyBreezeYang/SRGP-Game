package com.cellsgame.game.module.guild.bo;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.guild.cons.GuildRight;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on  2016-08-13.
 */
@AModule(ModuleID.Guild)
public interface GuildBO extends IBuildData{
    GuildVO getGuildVOAndCheckRight(PlayerVO pvo, GuildRight right) throws LogicException;

    void init();

    @Client(Command.Guild_Create)
    Map createGuild(PlayerVO pvo, CMD cmd, @CParam("name") String name,
                    @CParam("qq") String qq, @CParam("vx") String vx,
                    @CParam("notice") String notice, @CParam("needReq") boolean needReq) throws LogicException;

    @Client(Command.Guild_Join)
    Map joinGuild(CMD cmd, PlayerVO pvo, @CParam("id") int guildID) throws LogicException;

    @Client(Command.Guild_Enter)
    Map enterGuild(PlayerVO pvo, CMD cmd) throws LogicException;

    @Client(Command.Guild_ShowMemberList)
    Map queryMemberList(PlayerVO pvo, CMD cmd) throws LogicException;

    @Client(Command.Guild_Query)
    Map queryGuild(CMD cmd, PlayerVO ovo, @CParam("name") String name) throws LogicException;

    @Client(Command.Guild_AcceptJoin)
    Map acceptPlayerJoinGuild(CMD cmd, PlayerVO pvo, @CParam("pid") int pid, @CParam("accept") boolean accept) throws LogicException;

    @Client(Command.Guild_NotAllJoinReq)
    Map notAllJoinReq(CMD cmd, PlayerVO pvo) throws LogicException;

    @Client(Command.Guild_ModifyNotice)
    Map modifyNotice(CMD cmd, PlayerVO pvo, @CParam("content") String content) throws LogicException;

    @Client(Command.Guild_ModifyDesc)
    Map modifyDesc(CMD cmd, PlayerVO pvo, @CParam("content") String content) throws LogicException;

    @Client(Command.Guild_ModifyQQ)
    Map modifyQQ(CMD cmd, PlayerVO pvo, @CParam("qq") String logo) throws LogicException;

    @Client(Command.Guild_ModifyVX)
    Map modifyVX(CMD cmd, PlayerVO pvo, @CParam("vx") String logo) throws LogicException;

    @Client(Command.Guild_ModifyName)
    Map modifyName(CMD cmd, PlayerVO pvo, @CParam("name") String name) throws LogicException;

    @Client(Command.Guild_ModifyRight)
    Map modifyRight(CMD cmd, PlayerVO pvo, @CParam("pid") int pid, @CParam("right") int right) throws LogicException;

    @Client(Command.Guild_Out)
    Map outGuild(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;

    @Client(Command.Guild_ChgOwner)
    Map chgOwner(CMD cmd, PlayerVO pvo, @CParam("pid") int tgtPid) throws LogicException;

    @Client(Command.Guild_Dissolution)
    Map dissolutionGuild(PlayerVO pvo, CMD cmd) throws LogicException;

    @Client(Command.Guild_CancelDissolution)
    Map cancelDissolutionGuild(PlayerVO pvo, CMD cmd) throws LogicException;

    @Client(Command.Guild_Donate)
    Map donate(CMD cmd, PlayerVO pvo, @CParam("dCid") int donateCid) throws LogicException;

    @Client(Command.Guild_InvitePlayer)
    Map invitePlayerJoin(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;

    @Client(Command.Guild_ShowReqList)
    Map showReqList(CMD cmd, PlayerVO pvo) throws LogicException;

    @Client(Command.Guild_ModifyNeedReqStatus)
    Map modifyNeedReqStatus(CMD cmd, PlayerVO pvo, @CParam("need") boolean need) throws LogicException;

    @Client(Command.Guild_GetGuildLog)
    Map getGuildLOG(CMD cmd, PlayerVO pvo) throws LogicException;

    Map addGuildExp(CMD cmd, Map parent, GuildVO guildVO, PlayerVO playerVO, int exp);
    /**
     * 检查 家族解散
     */
    void innerDissolutionGuild(List<GuildVO> guildVOS);

    void clearGuildReq();

    void checkInDissolutionGuild();

    /**
     * 系统重置数据
     */
    void systemReset(Map<String, Object> ret, PlayerVO playerVO);

    public void cmdReset(Map<String, Object> ret, PlayerVO playerVO);

    /**
     * 系统重置数据
     */
    void systemResetGuildData();

    /**
     * 更新公会战力
     */
    void updateGuildFightForce();

    void changeGuildMny(CMD cmd, Map parent, PlayerVO player, GuildVO guildVO, long delta);

    void replaceGuildLeader();
}
