package com.cellsgame.game.module.friend.ctx;

import com.cellsgame.common.util.SpringBeanFactory;

/**
 * @author Aly @2017-02-07.
 */
public class FriendsContext {
    private static FriendsContext ctx;
    private char[] nameSearchRange;
    private String mailTitle;
    private String mailContent;

    public static FriendsContext getCtx() {
        if (null == ctx) {
            synchronized (FriendsContext.class) {
                if (null == ctx) ctx = SpringBeanFactory.getBean(FriendsContext.class);
            }
        }
        return ctx;
    }

    public char[] getNameSearchRange() {
        return nameSearchRange;
    }

    public void setNameSearchRange(char[] nameSearchRange) {
        this.nameSearchRange = nameSearchRange;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
}
