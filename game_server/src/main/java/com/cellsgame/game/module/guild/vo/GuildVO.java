package com.cellsgame.game.module.guild.vo;

import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CachePlayerDBID;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.module.activity.vo.ActivityRecVO;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly on  2016-08-13.
 */
public class GuildVO extends DBVO implements EvtHolder {
    public static final AtomicIntegerFieldUpdater<GuildVO> statusUpdater = AtomicIntegerFieldUpdater.newUpdater(GuildVO.class, "status");
    public static final int NORMAL = 0;             // 正常
    public static final int IN_DISSOLUTION = 1;     // 解散中
    public static final int DISSOLUTION_END = 2;    // 已经解散


    private int id;

    @Save(ix = 1)
    private int level;
    @Save(ix = 2)
    private int exp;
    @Save(ix = 3)
    private String name;                            // 名字
    @Save(ix = 4)
    private String desc;                            // 家族简介
    @Save(ix = 5)
    private String notice;                          // 公告
    @Save(ix = 6)
    private String qq;                            // qq
    @Save(ix = 7)
    private String vx;                            // 微信
    @Save(ix = 8)
    private int owner;                           // 拥有者
    @Save(ix = 9)
    private int creator;                         // 创建者
    @Save(ix = 10)
    private long fightForce;                        // 战力
    @Save(ix = 11)
    private boolean enterNeedReq;                   // 进入是否需要申请
    @Save(ix = 12)
    private long mny;                               // 家族资金
    @Save(ix = 13)
    private volatile int status;                    // 家族状态 --> 目前只有在解散的时候会用
    @Save(ix = 14)
    private long dissolutionTime;                   // 家族解散时间
    @Save(ix = 15)
    private Map<Integer, Integer> memberRights;      // 权限表
    @Save(ix = 16)
    private Set<Integer> inviteList;                 // 邀请列表
    @Save(ix = 17)
    private List<GuildLogVO> logs;                  // LOG

    //----------------------------------逻辑数据------------------------
    private Map<String, ActivityRecVO> activityRecs;                    // 活动数据

    public GuildVO() {
        super();
    }

    //-----------------GET SET------------------


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getLevel() {
        return level;
    }
    
    public long getRankVal(){
    	return level*10_000_000+exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getFightForce() {
        return fightForce;
    }

    public void setFightForce(long fightForce) {
        this.fightForce = fightForce;
    }

    public boolean isEnterNeedReq() {
        return enterNeedReq;
    }

    public void setEnterNeedReq(boolean enterNeedReq) {
        this.enterNeedReq = enterNeedReq;
    }

    public long getMny() {
        return mny;
    }

    public void setMny(long mny) {
        this.mny = mny;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDissolutionTime() {
        return dissolutionTime;
    }

    public void setDissolutionTime(long dissolutionTime) {
        this.dissolutionTime = dissolutionTime;
    }

    public Map<Integer, Integer> getMemberRights() {
        return memberRights;
    }

    public void setMemberRights(Map<Integer, Integer> memberRights) {
        this.memberRights = memberRights;
    }

    public Set<Integer> getInviteList() {
        return inviteList;
    }

    public void setInviteList(Set<Integer> inviteList) {
        this.inviteList = inviteList;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getVx() {
        return vx;
    }

    public void setVx(String vx) {
        this.vx = vx;
    }

    //------------------------------------------------------------------------------------------------------------------
    public List<GuildLogVO> getLogs() {
        return logs;
    }

    public void setLogs(List<GuildLogVO> logs) {
        this.logs = logs;
    }

    @Override
    protected Object initPrimaryKey() {
        id = CachePlayerDBID.GUILD_DBID.incrementAndGet();
        return id;
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = ((int) pk);
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {

    }

  
    @Override
    protected void init() {
        name = "";
        qq = "";
        vx = "";
        desc = "";
        notice = "";
        owner = 0;
        creator = 0;
        memberRights = new HashMap<>();
        inviteList = new HashSet<>();
        logs = new ArrayList<>();
        activityRecs = GameUtil.createSimpleMap();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public int getMemberSize() {
        return memberRights.size();
    }

	public Map<String, ActivityRecVO> getActivityRecs() {
		return activityRecs;
	}

	public void setActivityRecs(Map<String, ActivityRecVO> activityRecs) {
		this.activityRecs = activityRecs;
	}


}
