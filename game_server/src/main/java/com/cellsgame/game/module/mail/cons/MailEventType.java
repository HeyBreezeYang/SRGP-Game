package com.cellsgame.game.module.mail.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;

/**
 * @author Aly on  2016-09-27.
 */
public enum MailEventType implements EvtType {

    /**
     * 邮件发送
     *
     * @see EvtParamType#MAIL_VO  发送邮件
     * @see EvtParamType#MAIL_VO_S  邮件列表
     */
    Send,
    /**
     * 邮件附件领取
     * @see EvtParamType#MAIL_VO_S  邮件列表
     */
    Pick,
    /**
     * 邮件删除
     *
     * @see EvtParamType#MAIL_VO_S  被删除的邮件列表
     */
    MailDelete;
    private static AtomicInteger inc = new AtomicInteger(ModuleID.Mail);

    static {
        EvtType[] values = values();
        for (EvtType eType : values) {
            eType.setEvtCode(inc.incrementAndGet());
        }
    }

    @Override
    public int getEvtCode() {
        return 0;
    }

    @Override
    public void setEvtCode(int code) {

    }
}
