//package com.cellsgame.game.module.player.vo;
//
//import java.util.List;
//
//import com.cellsgame.common.util.GameUtil;
//import com.cellsgame.orm.DBVO;
//import com.cellsgame.orm.enhanced.annotation.Save;
//
///**
// * 玩家简要数据.
// *
// * @author Yang
// */
//public final class SimplePlayerVO extends DBVO {
//    public static final String ROBOT_ID = "ro";
//    private int id;                      // 唯一ID
//    private String accountId;               // 帐号ID
//    private int serverId;                // 服务器ID
//    private int cfgId;                      // 角色配置ID
//    @Save(ix = 0)
//    private String name;                    // 名称
//    @Save(ix = 1)
//    private int image;                      // 图像
//    @Save(ix = 2)
//    private int level;                      // 等级
//    @Save(ix = 3)
//    private long fightForce;                 // 战斗力
//    @Save(ix = 10)
//    private int leader;                      // 主角
//
//    public SimplePlayerVO copyFrom(SimplePlayerVO playerVO) {
//        this.setId(playerVO.getId());
//        this.setAccountId(playerVO.getAccountId());
//        this.setServerId(playerVO.getServerId());
//        this.setCid(playerVO.getCid());
//        this.setName(playerVO.getName());
//        this.setImage(playerVO.getImage());
//        this.setLeader(playerVO.getLeader());
//        this.setLevel(playerVO.getLevel());
//        this.setFightForce(playerVO.getFightForce());
//        // 技能
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        SimplePlayerVO that = (SimplePlayerVO) o;
//
//        return id == that.id;
//    }
//
//
//    public final SimplePlayerVO clone() {
//        //
//        return new SimplePlayerVO().copyFrom(this);
//    }
//
//    public String getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(String accountId) {
//        this.accountId = accountId;
//    }
//
//    public int getServerId() {
//        return serverId;
//    }
//
//    public void setServerId(int serverId) {
//        this.serverId = serverId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
//
//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    public long getFightForce() {
//        return fightForce;
//    }
//
//    public void setFightForce(long fightForce) {
//        this.fightForce = fightForce;
//    }
//
//    public int getLeader() {
//        return leader;
//    }
//
//    public void setLeader(int leader) {
//        this.leader = leader;
//    }
//
//    @Override
//    protected Object initPrimaryKey() {
//        return id;
//    }
//
//    @Override
//    protected Object getPrimaryKey() {
//        return id;
//    }
//
//    @Override
//    protected void setPrimaryKey(Object pk) {
//        id = (int)pk;
//    }
//
//    @Override
//    protected Object[] getRelationKeys() {
//        return new Object[]{accountId, serverId};
//    }
//
//    @Override
//    protected void setRelationKeys(Object[] relationKeys) {
//        accountId = (String) relationKeys[0];
//        serverId = (int) relationKeys[1];
//    }
//
//    @Override
//    protected void init() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    @Override
//    public Integer getCid() {
//        return cfgId;
//    }
//
//    @Override
//    public void setCid(Integer cid) {
//        cfgId = cid;
//    }
//}
