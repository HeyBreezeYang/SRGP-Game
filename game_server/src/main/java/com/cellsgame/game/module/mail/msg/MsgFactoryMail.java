package com.cellsgame.game.module.mail.msg;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.mail.vo.MailVO;

/**
 * Created by yfzhang on 2016/8/22.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MsgFactoryMail extends MsgFactory {
    public static final String MAIL = "mil";
    public static final String MAIL_READ = "rd";
    public static final String MAIL_PICK = "pk";
    public static final String MAIL_ACCESSORY = "acc";
    private static final MsgFactoryMail instance = new MsgFactoryMail();

    public static MsgFactoryMail instance() {
        return instance;
    }

    public static Map<String, Object> getMailVOSimpleMsg(MailVO mailVO){
        Map<String, Object> message = GameUtil.createSimpleMap();
        message.put(ID, mailVO.getId());
        message.put(TYPE, mailVO.getType());
        message.put(TITLE, mailVO.getTitle());
        message.put(MAIL_READ, mailVO.isRead());
        message.put(MAIL_PICK, mailVO.isPick());
        message.put(MAIL_ACCESSORY, mailVO.getItemList().size() >= 1);
        message.put(DATE, (int)(mailVO.getSendTime()/DateUtil.SECONDS_MILLIS));
        message.put(VALIDATE, (int)(mailVO.getValid()/DateUtil.SECONDS_MILLIS));
        message.put(FROM, mailVO.getSenderName());
        return message;
    }

    @Override
    public String getModulePrefix() {
        return MAIL;
    }

    public Map<String, Object> createAddMailMessage(Map<String, Object> parent, MailVO mailVO) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Map<String, Object>> listInfo = gocLstIn(op, ADD);
        listInfo.add(getMailVOQryMsg(mailVO));
        return parent;
    }

    public Map<?, ?> getQueryMailMsg(Map<?, ?> parent, MailVO mailVO){
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        Map<String, Object> qry = gocMapIn(op, QUERY);
        qry.putAll( getMailVOQryMsg(mailVO));
        return parent;
    }




	private Map getMailVOQryMsg(MailVO mailVO) {
		Map mailInfo = GameUtil.createSimpleMap();
        mailInfo.put(ID, mailVO.getId());
        mailInfo.put(TITLE, mailVO.getTitle());
        mailInfo.put(CONTENT, mailVO.getContext());
        mailInfo.put(TYPE, mailVO.getFuncType());
        mailInfo.put(PARAM, mailVO.getFuncParams());
        mailInfo.put(DATE, (int)(mailVO.getSendTime()/DateUtil.SECONDS_MILLIS));
        mailInfo.put(VALIDATE, (int)(mailVO.getValid()/DateUtil.SECONDS_MILLIS));
        mailInfo.put(MAIL_READ, mailVO.isRead());
        mailInfo.put(MAIL_PICK, mailVO.isPick());
        mailInfo.put(FROM, mailVO.getSenderName());
        if(mailVO.getItemList().size() >= 1){
            List<Map<?, ?>> listInfo = gocLstIn(mailInfo, MAIL_ACCESSORY);
        	for(FuncConfig acc : mailVO.getItemList()){
        		Map<String, Object> m = GameUtil.createSimpleMap();
        		m.put(TYPE, acc.getType());
        		m.put(ID, acc.getParam());
        		m.put(NUM, acc.getValue());
        		listInfo.add(m);
        	}
        }
		return mailInfo;
	}

    public Map<?, ?> createUpdateMailMessage(Map<?, ?> parent, MailVO mailVO){
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Map> update = gocLstIn(op, UPDATE);
        update.add(getMailVOStateMsg(mailVO));
        return parent;
    }

    public Map<String, Object> getMailVOStateMsg(MailVO mailVO){
        Map<String, Object> message = GameUtil.createSimpleMap();
        message.put(ID, mailVO.getId());
        message.put(MAIL_READ, mailVO.isRead());
        message.put(MAIL_PICK, mailVO.isPick());
        return message;
    }

    public Map createDeleteMailMessage(Map parent, List<MailVO> deletes) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Integer> delete = gocLstIn(op, DELETE);
        for(MailVO mailVO : deletes){
            delete.add(mailVO.getId());
        }
        return parent;
    }

	public Map<String, Object> createMailListMessage(Map parent, Collection<MailVO> mails) {
		parent = creIfNull(parent);
		List<Map> mailInfo = gocInfoLst(parent);
		for(MailVO mailVO : mails){
			mailInfo.add(getMailVOQryMsg(mailVO));
		}
		return parent;
	}

	public Map<?, ?> createPrizeMessage(Map<?, ?> parent, Map prize) {
		Map msdInfoOp = gocOpMap(parent);
		addPrizeMsg(msdInfoOp, prize);
		return parent;
	}

	
}
