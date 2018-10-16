package com.cellsgame.game.module.mail;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.mail.cons.MailConstant;
import com.cellsgame.game.module.mail.cons.MailFuncType;
import com.cellsgame.game.module.mail.msg.CodeMail;
import com.cellsgame.game.module.mail.vo.MailVO;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class MailFactory {

    private static MailBO mailBO = null;

    public static MailVO newMailVO(int playerId, MailType type, String title, String context) {
        long currTime = System.currentTimeMillis();
        MailVO mailVO = new MailVO();
        mailVO.setType(type.getValue());
        mailVO.setTitle(title == null ? "" : title);
        mailVO.setContext(context == null ? "" : context);
        mailVO.setSendTime(currTime);
        mailVO.setValid(currTime + DateUtil.MONTH_MILLIS);
        mailVO.setRead(false);
        mailVO.setPick(false);
        mailVO.setPlayerId(playerId);
        return mailVO;
    }

    public static MailVO newMailVO(int playerId, MailType type, String title, String context, List<FuncConfig> accessorys) {
        MailVO mailVO = newMailVO(playerId, type, title, context);
        if (null != accessorys)
            for (FuncConfig prize : accessorys) {
                FuncConfig acc = new FuncConfig();
                acc.setType(prize.getType());
                acc.setParam(prize.getParam());
                acc.setValue(prize.getValue());
                mailVO.getItemList().add(acc);
            }
        return mailVO;
    }

    public static MailVO newMailVO(int playerId, MailType type, String title, String context, List<FuncConfig> accessorys, long limitDate) {
        MailVO mvo = newMailVO(playerId, type, title, context, accessorys);
        mvo.setLimitDate(limitDate);
        return mvo;
    }

    public static MailVO newMailVO(int playerId, MailType type, String title, String context, MailFuncType funcType, String[] funcParams) throws LogicException {
        return newMailVO(playerId, type, title, context, null, funcType, funcParams);
    }

    public static MailVO newMailVO(int playerId, MailType type,
                                   String title, String context, List<FuncConfig> accessorys, MailFuncType funcType,
                                   String[] funcParams) throws LogicException {
        MailVO mailVO = newMailVO(playerId, type, title, context, accessorys);
        CodeMail.Mail_FuncTypeError.throwIfTrue(funcType == null);
        CodeMail.Mail_FuncParamsError.throwIfTrue(funcParams == null);
        mailVO.setFuncType(funcType.getType());
        mailVO.setFuncParams(funcParams);
        return mailVO;
    }

    public static Builder newMail(MailType type, int pid, String title, String content) {
        return new Builder(type, pid, title, content);
    }

    private static MailBO getMailBO() {
        if (mailBO == null) {
            synchronized (MailFactory.class) {
                if (mailBO == null) {
                    mailBO = SpringBeanFactory.getBean(MailBO.class);
                }
            }
        }
        return mailBO;
    }

    public static class Builder {
        private int playerId;
        private String title;
        private String context;
        private int type;

        private String senderName = MailConstant.MAIL_SENDER_NAME_SYS;
        private long valid = DateUtil.MONTH_MILLIS;

        private List<FuncConfig> itemList;

        private int funcType;
        private String[] funcParams;

        private long limitDate;

        Builder(MailType type, int pid, String title, String content) {
            this.type = type.getValue();
            playerId = pid;
            this.title = title;
            this.context = content;
        }

        public Builder setSenderName(String senderName) {
            if (senderName != null) {
                this.senderName = senderName;
            }
            return this;
        }

        public Builder setAccessors(List<FuncConfig> accessorys) {
            return setAccessors(accessorys, 1);
        }

        public Builder setAccessors(List<FuncConfig> accessors, int prizeTimes, int prizeTimesBase) {
            if (prizeTimes > 0 && prizeTimesBase > 0 && !CollectionUtils.isEmpty(accessors)) {
                for (FuncConfig prize : accessors) {
                    addAccessor(prize, prizeTimes, prizeTimesBase);
                }
            }
            return this;
        }

        public Builder addAccessor(FuncConfig prize) {
            addAccessor(prize, 1, 1);
            return this;
        }

        private void addAccessor(FuncConfig prize, int prizeTimes, int prizeTimesBase) {
            int v = (int)prize.getValue() * prizeTimes / prizeTimesBase;
            if (v > 0) {
                FuncConfig acc = new FuncConfig();
                acc.setType(prize.getType());
                acc.setParam(prize.getParam());
                acc.setValue(v);
                if (null == itemList) {
                    itemList = GameUtil.createList();
                }
                itemList.add(acc);
            }
        }

        public Builder setAccessors(List<FuncConfig> accessors, int prizeTimes) {
            setAccessors(accessors, prizeTimes, 1);
            return this;
        }

        /**
         * 有效期 期限 时长
         */
        public Builder setValid(long valid) {
            this.valid = valid;
            return this;
        }

        public Builder setFunc(MailFuncType funcType, String[] funcParams) {
            if (null != funcType && null != funcParams) {
                this.funcType = funcType.getType();
                this.funcParams = funcParams;
            }
            return this;
        }

        /**
         * 保护期
         */
        public Builder setLimitDate(long limitDate) {
            this.limitDate = limitDate;
            return this;
        }

        public void sendIfHavePrize() {
            if (!CollectionUtils.isEmpty(itemList)) {
                send();
            }
        }

        public void send() {
            long cur = System.currentTimeMillis();
            send(cur, valid + cur);
        }

        public void send(long sendTime, long valid) {
            MailVO mailVO = new MailVO();
            mailVO.setRead(false);
            mailVO.setPick(false);
            mailVO.setPlayerId(playerId);
            mailVO.setContext(context);
            mailVO.setTitle(title);
            mailVO.setType(type);
            mailVO.setSenderName(senderName);

            mailVO.setSendTime(sendTime);
            mailVO.setValid(valid);

            mailVO.setLimitDate(limitDate);
            if (null != funcParams) {
                mailVO.setFuncParams(funcParams);
                mailVO.setFuncType(funcType);
            }
            if (null != itemList) {
                mailVO.setItemList(itemList);
            }
            getMailBO().sendMail(mailVO);
        }
    }
}
