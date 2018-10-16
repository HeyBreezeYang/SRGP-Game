package com.cellsgame.game.module.player.vo;

/**
 * Created by Aly on 2015-08-25.
 * 玩家数据基本信息  用在CachePlayer里面
 */
public class PlayerInfoVO {
    private String uid;
    private int serverId;
    private int pid;
    private String name;
    private int image;
    private int vip;
    private int plv;
    private int exp;
    private int guildId;
    private String guildName = "";
    private long lastLoginTime;
    private long lastLogOutTime;
    private String titleDec = "";
    private long fightForce;


    public PlayerInfoVO(int pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPlv() {
        return plv;
    }

    public void setPlv(int plv) {
        this.plv = plv;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getGuildId() {
        return guildId;
    }

    public void setGuildId(int guildId) {
        this.guildId = guildId;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getLastLogOutTime() {
        return lastLogOutTime;
    }

    public void setLastLogOutTime(long lastLogOutTime) {
        this.lastLogOutTime = lastLogOutTime;
    }

    public long getFightForce() {
        return fightForce;
    }

    public void setFightForce(long fightForce) {
        this.fightForce = fightForce;
    }

    public String getTitleDec() {
        return titleDec;
    }

    public void setTitleDec(String titleDec) {
        this.titleDec = titleDec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfoVO that = (PlayerInfoVO) o;

        return that.pid == pid;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }


}