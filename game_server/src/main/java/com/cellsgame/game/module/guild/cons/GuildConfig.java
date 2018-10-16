package com.cellsgame.game.module.guild.cons;

import com.cellsgame.common.util.SpringBeanFactory;

/**
 * @author Aly on  2016-08-15.
 */
public class GuildConfig {

    private static GuildConfig config;
    /**
     * 玩家等级限制
     */
    private int createLimitPlayerLV;
    /**
     * 进入家族CD  小时
     *
     * @see java.util.concurrent.TimeUnit#HOURS
     */
    private int enterGuildCD;

    /**
     * 每个玩家最大请求的家族数量
     */
    private int maxReqGuildSize;
    private int maxNoticeLen;
    private int maxDescLen;
    /**
     * 家族解散CD  提交申请后 多少时间后执行实际解散
     *
     * @see java.util.concurrent.TimeUnit#HOURS
     */
    private long dissolutionCD;

    private String guildRightChgMailTitle;         //家族权限变更
    private String guildRightChgMailContent;       //你的权限发生了变化
    private String guildInviteMailTitle;           //公会邀请
    private String guildInviteMailContent;         //{name}邀请你加入公会{guildName}
    private String guildRejectJoinContent;
    private String guildRejectJoinTitle;

    private String guildTOutTitle;                      // 公会被踢 标题
    private String guildTOutContent;                    // 公会被踢 内容

    public static GuildConfig getConfig() {
        if (null == config) {
            synchronized (GuildConfig.class) {
                if (null == config)
                    config = SpringBeanFactory.getBean(GuildConfig.class);
            }
        }
        return config;
    }

    public int getCreateLimitPlayerLV() {
        return createLimitPlayerLV;
    }

    public void setCreateLimitPlayerLV(int createLimitPlayerLV) {
        this.createLimitPlayerLV = createLimitPlayerLV;
    }

    public int getEnterGuildCD() {
        return enterGuildCD;
    }

    public void setEnterGuildCD(int enterGuildCD) {
        this.enterGuildCD = enterGuildCD;
    }

    public int getMaxReqGuildSize() {
        return maxReqGuildSize;
    }

    public void setMaxReqGuildSize(int maxReqGuildSize) {
        this.maxReqGuildSize = maxReqGuildSize;
    }

    public int getMaxNoticeLen() {
        return maxNoticeLen;
    }

    public void setMaxNoticeLen(int maxNoticeLen) {
        this.maxNoticeLen = maxNoticeLen;
    }

    public long getDissolutionCD() {
        return dissolutionCD;
    }

    public void setDissolutionCD(long dissolutionCD) {
        this.dissolutionCD = dissolutionCD;
    }

    public String getGuildRightChgMailTitle() {
        return guildRightChgMailTitle;
    }

    public void setGuildRightChgMailTitle(String guildRightChgMailTitle) {
        this.guildRightChgMailTitle = guildRightChgMailTitle;
    }

    public String getGuildRightChgMailContent() {
        return guildRightChgMailContent;
    }

    public void setGuildRightChgMailContent(String guildRightChgMailContent) {
        this.guildRightChgMailContent = guildRightChgMailContent;
    }

    public String getGuildInviteMailTitle() {
        return guildInviteMailTitle;
    }

    public void setGuildInviteMailTitle(String guildInviteMailTitle) {
        this.guildInviteMailTitle = guildInviteMailTitle;
    }

    public String getGuildInviteMailContent() {
        return guildInviteMailContent;
    }

    public void setGuildInviteMailContent(String guildInviteMailContent) {
        this.guildInviteMailContent = guildInviteMailContent;
    }

    public String getGuildRejectJoinContent() {
        return guildRejectJoinContent;
    }

    public void setGuildRejectJoinContent(String guildRejectJoinContent) {
        this.guildRejectJoinContent = guildRejectJoinContent;
    }

    public String getGuildRejectJoinTitle() {
        return guildRejectJoinTitle;
    }

    public void setGuildRejectJoinTitle(String guildRejectJoinTitle) {
        this.guildRejectJoinTitle = guildRejectJoinTitle;
    }

    public int getMaxDescLen() {
        return maxDescLen;
    }

    public void setMaxDescLen(int maxDescLen) {
        this.maxDescLen = maxDescLen;
    }

    public String getGuildTOutTitle() {
        return guildTOutTitle;
    }

    public void setGuildTOutTitle(String guildTOutTitle) {
        this.guildTOutTitle = guildTOutTitle;
    }

    public String getGuildTOutContent() {
        return guildTOutContent;
    }

    public void setGuildTOutContent(String guildTOutContent) {
        this.guildTOutContent = guildTOutContent;
    }

}
