package com.cellsgame.game.module.activity.vo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.activity.cons.ActivityConstant;
import com.cellsgame.game.util.UUIDUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ActivityVO extends DBVO {

    private String id;

    //活动开始日期
	@Save(ix = 1)
  	private long startDate;
  	//活动结束日期
	@Save(ix = 2)
  	private long endDate;
  	//刷新间隔{单位：分钟}
	@Save(ix = 3)
  	private int refInterval;
	//** 工作模式  0:全检查-全执行模式   1:逐一检查-逐一执行模式 ， 2 : 活动结束时执行*/
	@Save(ix = 4)
	private int workMode;
	//自动行为
	@Save(ix = 5)
	private boolean autoBev;
  	//客户端参数
	@Save(ix = 7)
  	private String clientAtts;
  	//条件行为组
	@Save(ix = 8)
  	private Map<Integer, ActivityGroupVO> groups;
	//活动状态
	@Save(ix = 10)
	private int status;
	//活动刷新时间
	@Save(ix = 11)
	private long refTime;
	//行为执行开始时间
	@Save(ix = 12)
	private long execBevStartDate;
	//冲榜结束时间
	@Save(ix = 13)
	private long bevListenEndDate;
	
	//--------------------逻辑数据----------------------
	private ActivityRecVO recVO;
	
	private Set<EvtType> concernEvents;
	
	 public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public int getRefInterval() {
		return refInterval;
	}

	public void setRefInterval(int refInterval) {
		this.refInterval = refInterval;
	}

	public String getClientAtts() {
		return clientAtts;
	}

	public void setClientAtts(String clientAtts) {
		this.clientAtts = clientAtts;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<EvtType> getConcernEvents() {
		return concernEvents;
	}

	public void setConcernEvents(Set<EvtType> concernEvents) {
		this.concernEvents = concernEvents;
	}

	public ActivityRecVO getRecVO() {
		return recVO;
	}

	public void setRecVO(ActivityRecVO recVO) {
		this.recVO = recVO;
	}

	public int getWorkMode() {
		return workMode;
	}

	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}

	public boolean isAutoBev() {
		return autoBev;
	}

	public void setAutoBev(boolean autoBev) {
		this.autoBev = autoBev;
	}

	public long getRefTime() {
		return refTime;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}

	public Map<Integer, ActivityGroupVO> getGroups() {
		return groups;
	}

	public void setGroups(Map<Integer, ActivityGroupVO> groups) {
		this.groups = groups;
	}

	public long getExecBevStartDate() {
		return execBevStartDate;
	}

	public void setExecBevStartDate(long execBevStartDate) {
		this.execBevStartDate = execBevStartDate;
	}

	public long getBevListenEndDate() {
		return bevListenEndDate;
	}

	public void setBevListenEndDate(long bevListenEndDate) {
		this.bevListenEndDate = bevListenEndDate;
	}

	//------------------------------------------------------------------------------------------------------------------
	@Override
    protected Object initPrimaryKey() {
        id = UUIDUtil.getUUID();
        return id;
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = pk.toString();
    }

    @Override
    protected Object[] getRelationKeys() {
        return null;
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
    }

    @Override
    protected void init() {
    	clientAtts = "";
    	groups = GameUtil.createSimpleMap();
    	concernEvents = new HashSet<EvtType>();
    	startDate = 0;
    	endDate = 0;
    }

    @Override
    public Integer getCid() {
        return 0;
    }

    @Override
    public void setCid(Integer cid) {

    }

	public boolean vaildate() {
		return status == ActivityConstant.ACTIVITY_STATUS_RUNNING;
	}

	public void initConcernEvents(){
		for(ActivityGroupVO group : getGroups().values()){
			for(EvtType evt : group.getConcernEvents())
				getConcernEvents().add(evt);
		}
	}

	public boolean isListener() {
		if(bevListenEndDate <= 0) return true;
		long time = System.currentTimeMillis();
		return time <= bevListenEndDate;
	}
}
