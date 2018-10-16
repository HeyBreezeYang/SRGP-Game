package com.cellsgame.game.module.mail.bo;


import java.util.List;
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
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.mail.MailType;
import com.cellsgame.game.module.mail.cons.MailFuncType;
import com.cellsgame.game.module.mail.vo.MailVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang n 2016/8/19.
 */
@AModule(ModuleID.Mail)
public interface MailBO extends  IBuildData, StaticEvtListener{

    @Client(Command.Mail_Pick)
    Map<?, ?> pick(PlayerVO player, CMD cmd, @CParam("id") int mailId) throws LogicException;

    @Client(Command.Mail_Picks)
    Map<?, ?> pick(PlayerVO player, @CParam("ids") Integer[] mailIds, CMD cmd) throws LogicException;

    @Client(Command.Mail_Send)
    Map<?, ?> send(PlayerVO sendPlayerVO, @CParam("target") String targetPlayerName, @CParam("title") String title, @CParam("cont") String context) throws LogicException;

    @Client(Command.Mail_Get)
    Map<?, ?> getMailInfo(PlayerVO playerVO, @CParam("id") int mailId) throws LogicException;

    @Client(Command.Mail_Delete)
    Map<?, ?> delete(PlayerVO playerVO, @CParam("ids") Integer[] mailIds, CMD cmd) throws LogicException;

    @Client(Command.Mail_List)
    Map<?, ?> list(PlayerVO playerVO) throws LogicException;


    void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context);

    void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context, List<FuncConfig> accessorys);

    void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context, List<FuncConfig> accessorys, long limitDate);

    void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context, MailFuncType funcType, String[] funcParams) throws LogicException;

    void sendSysMail(int pid, Map configMap);

    void sendSysMail(int pid, String title, String content, List<FuncConfig> prizes);

    void sendSysMail(int pid, String title, String content, List<FuncConfig> prizes, long limitDate);


    void clearOverdueMail(PlayerVO player, CMD cmd);

    /**
     * 发送邮件
     */
    void sendMail(MailVO mailVO);
}
