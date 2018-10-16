package com.cellsgame.game.module.mail.bo.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.mail.MailFactory;
import com.cellsgame.game.module.mail.MailType;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.mail.cache.CacheMail;
import com.cellsgame.game.module.mail.cons.MailConstant;
import com.cellsgame.game.module.mail.cons.MailEventType;
import com.cellsgame.game.module.mail.cons.MailFuncType;
import com.cellsgame.game.module.mail.msg.CodeMail;
import com.cellsgame.game.module.mail.msg.MsgFactoryMail;
import com.cellsgame.game.module.mail.vo.MailVO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailBOImpl implements MailBO{
    private static final Logger log = LoggerFactory.getLogger(MailBOImpl.class);
    @Resource
    private BaseDAO<MailVO> mailDAO;

    @Override
    @SuppressWarnings("unchecked")
    public void sendSysMail(int pid, Map configMap) {
        String title = (String) configMap.get(MailConstant.MAIL_SIGN_TITLE);
        String content = (String) configMap.get(MailConstant.MAIL_SIGN_CONTENT);
        List<FuncConfig> prizes = (List<FuncConfig>) configMap.get(MailConstant.MAIL_SIGN_PRIZE);
        sendSysMail(pid, title, content, prizes);
    }

    @Override
    public void sendSysMail(int pid, String title, String content, List<FuncConfig> prizes) {
        sendMailByID(MailConstant.MAIL_SENDER_NAME_SYS, pid, MailType.System, title, content, prizes);
    }


    @Override
    public void sendSysMail(int pid, String title, String content, List<FuncConfig> prizes, long limitDate) {
        sendMailByID(MailConstant.MAIL_SENDER_NAME_SYS, pid, MailType.System, title, content, prizes, limitDate);
    }


  

    @Override
    public Map<?, ?> pick(PlayerVO player, CMD cmd, int mailId) throws LogicException {
        MailVO mailVO = player.getMailVOMap().get(mailId);
        CodeMail.Mail_NotFind.throwIfTrue(mailVO == null);
        CodeMail.Mail_AlreadyPick.throwIfTrue(mailVO.isPick());
        List<FuncConfig> accessoryVOList = mailVO.getItemList();
        CodeMail.Mail_EmptyAccessory.throwIfTrue(accessoryVOList == null || accessoryVOList.size() < 1);
        CodeMail.Mail_InLimit.throwIfTrue(mailVO.getLimitDate() > System.currentTimeMillis());
        Map<?, ?> result = pick(null, player, mailVO, cmd);
        return MsgUtil.brmAll(result, Command.Mail_Pick);
    }


    @Override
    public Map<?, ?> pick(PlayerVO playerVO, Integer[] mailIds, CMD cmd) throws LogicException {
        CodeMail.Mail_ParamsError.throwIfTrue(mailIds == null || mailIds.length < 1);
        Map<?, ?> result = GameUtil.createSimpleMap();
        List<MailVO> pickable = GameUtil.createList();
        for (int mailId : mailIds) {
            MailVO mailVO = playerVO.getMailVOMap().get(mailId);
            if (mailVO == null || mailVO.isPick())
                continue;
            List<FuncConfig> accessoryVOList = mailVO.getItemList();
            if (accessoryVOList == null || accessoryVOList.size() < 1)
                continue;
            pickable.add(mailVO);
        }
        pick(result, playerVO, pickable, cmd);
        return MsgUtil.brmAll(result, Command.Mail_Picks);
    }

    private Map<?, ?> pick(Map<?, ?> parent, PlayerVO playerVO, MailVO mailVO, CMD cmd) throws LogicException {
        List<MailVO> mails = GameUtil.createList();
        mails.add(mailVO);
        return pick(parent, playerVO, mails, cmd);
    }

    private Map<?, ?> pick(Map<?, ?> parent, PlayerVO playerVO, Collection<MailVO> mailVOs, CMD cmd) throws LogicException {
        if (parent == null)
            parent = GameUtil.createSimpleMap();
        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
        for (MailVO mailVO : mailVOs) {
            List<FuncConfig> accessoryVOList = mailVO.getItemList();
            exec.addSyncFunc(accessoryVOList);
        }
        Map prize = GameUtil.createSimpleMap();
        exec.exec(parent, prize, playerVO);
        for (MailVO mailVO : mailVOs) {
            mailVO.setPick(true);
            mailVO.setRead(true);
            parent = MsgFactoryMail.instance().createUpdateMailMessage(parent, mailVO);
        }
        parent = MsgFactoryMail.instance().createPrizeMessage(parent, prize);
        MailEventType.Pick.happen(parent, cmd, playerVO, EvtParamType.MAIL_VO_S.val(mailVOs));
        mailDAO.save(mailVOs);
        return parent;
    }

    @Override
    public void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context) {
        MailVO mailVO = MailFactory.newMailVO(targetPlayerId, type, title, context);
        send(sendName, mailVO);
    }


    @Override
    public Map<?, ?> send(PlayerVO sendPlayerVO, String targetPlayerName, String title, String context) throws LogicException {
        PlayerVO targetPlayer = CachePlayer.getPlayerByPlayerName(targetPlayerName);
        if (targetPlayer == null) {
            Integer targetPlayerId = CachePlayerBase.getPIDByPname(targetPlayerName);
            CodeMail.Mail_NotFindPlayer.throwIfTrue(targetPlayerId == null);
            sendMailByID(sendPlayerVO.getName(), targetPlayerId, MailType.Guild, title, context);
        } else {
            MailFactory.newMail(MailType.Guild, targetPlayer.getId(), title, context)
                    .setSenderName(sendPlayerVO.getName())
                    .send();
        }
        return MsgUtil.brmAll(Command.Mail_Send);
    }

    @Override
    public Map<?, ?> getMailInfo(PlayerVO playerVO, int mailId) throws LogicException {
        MailVO mailVO = playerVO.getMailVOMap().get(mailId);
        CodeMail.Mail_NotFind.throwIfTrue(mailVO == null);
        mailVO.setRead(true);
        mailDAO.save(mailVO);
        Map<?, ?> result = MsgFactoryMail.instance().createUpdateMailMessage(GameUtil.createSimpleMap(), mailVO);
        return MsgUtil.brmAll(result, Command.Mail_Get);
    }

    @Override
    public Map<?, ?> delete(PlayerVO playerVO, Integer[] mailIds, CMD cmd) throws LogicException {
        List<MailVO> deletes = GameUtil.createList();
        for (int mailId : mailIds) {
            MailVO mailVO = playerVO.getMailVOMap().get(mailId);
            if (mailVO == null)
                continue;
            CodeMail.Mail_CanNotDeleteMail.throwIfTrue(! isDelete(mailVO));
            deletes.add(mailVO);
            playerVO.getMailVOMap().remove(mailId);
        }
        mailDAO.delete(deletes);
        Map<?, ?> result = MsgFactoryMail.instance().createDeleteMailMessage(null, deletes);
        MailEventType.MailDelete.happen(result, cmd, playerVO, EvtParamType.MAIL_VO_S.val(deletes));
        return MsgUtil.brmAll(result, Command.Mail_Delete);
    }

    private boolean isDelete(MailVO mailVO) {
        return mailVO.getItemList().size() <= 0 || mailVO.isPick();
    }

    @Override
    public void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context, List<FuncConfig> accessorys) {
        MailVO mailVO = MailFactory.newMailVO(targetPlayerId, type, title, context, accessorys);
        send(sendName, mailVO);
    }

    @Override
    public void sendMailByID(String sendName, int targetPlayerId, MailType type, String title, String context, List<FuncConfig> accessorys, long limitDate) {
        MailVO mailVO = MailFactory.newMailVO(targetPlayerId, type, title, context, accessorys, limitDate);
        send(sendName, mailVO);
    }


    @Override
    public void sendMailByID(String sendName, int targetPlayerId,
                             MailType type, String title, String context, MailFuncType funcType,
                             String[] funcParams) throws LogicException {
        MailVO mailVO = MailFactory.newMailVO(targetPlayerId, type, title, context, funcType, funcParams);
        send(sendName, mailVO);
    }

    private void send(String sendName, MailVO mailVO) {
        mailVO.setSenderName(sendName);
        sendMail(mailVO);
    }

    @Override
    public void sendMail(MailVO mailVO) {
        mailDAO.save(mailVO);
        int playerId = mailVO.getPlayerId();
        PlayerVO targetPlayerVO = CachePlayer.getPlayerByPid(playerId);
        if (targetPlayerVO != null) {
            targetPlayerVO.getMailVOMap().put(mailVO.getId(), mailVO);
            if (targetPlayerVO.isOnline()) {
                Map<String, Object> result = MsgFactoryMail.instance().createAddMailMessage(null, mailVO);
                MailEventType.Send.happen(result, CMD.system.now(), targetPlayerVO, EvtParamType.MAIL_VO.val(mailVO));
                Push.multiThreadSend(targetPlayerVO.getMessageController(), MsgUtil.brmAll(result, Command.Mail_Add));
            }
        } else {
            PlayerInfoVO playerInfoVO = CachePlayerBase.getBaseInfo(playerId);
            if(playerInfoVO != null) {
                CacheMail.cacheNewMail(playerId, mailVO);
            }
        }
    }

    @Override
    public Map<?, ?> list(PlayerVO playerVO) throws LogicException {
        Collection<MailVO> mails = playerVO.getMailVOMap().values();
        Map<?, ?> result = MsgFactoryMail.instance().createMailListMessage(null, mails);
        return MsgUtil.brmAll(result, Command.Mail_List);
    }

    @Override
    public void clearOverdueMail(PlayerVO player, CMD cmd) {
        long time = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, MailVO>> it = player.getMailVOMap().entrySet().iterator();
        List<MailVO> deletes = GameUtil.createList();
        while (it.hasNext()) {
            Map.Entry<Integer, MailVO> entry = it.next();
            MailVO mail = entry.getValue();
            //验证邮件是否已经失效
            if (time >= mail.getValid()) {
                it.remove();
                deletes.add(mail);
            }
        }
        if (! deletes.isEmpty()) {
            MailEventType.MailDelete.happen(null, cmd, player, EvtParamType.MAIL_VO_S.val(deletes));
            mailDAO.delete(deletes);
        }
    }

    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypePlayer.EnterGame,EvtTypePlayer.LoadSuc};
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> o = event.getType();
        PlayerVO playerVO = (PlayerVO) holder;
        if (o == EvtTypePlayer.EnterGame) {
            clearOverdueMail(playerVO, cmd);
        }
        if (o == EvtTypePlayer.LoadSuc) {
            Map<Integer, MailVO> mailVOMap = CacheMail.getCacheNewMail(playerVO);
            if (mailVOMap != null) {
                playerVO.getMailVOMap().putAll(mailVOMap);
                for (MailVO mailVO : mailVOMap.values()) {
                    MailEventType.Send.happen(GameUtil.createSimpleMap(), CMD.system.now(), playerVO, EvtParamType.MAIL_VO.val(mailVO));
                }
            }
        }
        return parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        List<DBObj> dbObjs = (List<DBObj>) data.get(DATA_SIGN_MAIL);
        if (dbObjs == null)
            return;
        for (DBObj dbObj : dbObjs) {
            MailVO mail = new MailVO();
            mail.readFromDBObj(dbObj);
            mail.setPlayerId(player.getId());
            player.getMailVOMap().put(mail.getId(), mail);
        }
    }
    
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		// TODO Auto-generated method stub
		
	}

}
