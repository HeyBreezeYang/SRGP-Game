package com.gmtime.gm.service.task.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.CacheTimer;
import com.gmtime.gm.dao.OperateDBIF;
import com.gmtime.gm.service.task.TimerTaskServiceIF;
import com.gmtime.gm.service.task.bean.TaskString;
import com.gmtime.gm.sql.impl.TaskObjSql;
import com.gmtime.gm.sql.impl.TaskSql;
import com.gmtime.rmi.service.bean.TimerTaskBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("timerTaskService")
public class TimerTaskService implements TimerTaskServiceIF {
	private final OperateDBIF operateDB;

	@Autowired
	public TimerTaskService(@Qualifier("operateDB") OperateDBIF operateDB) {
		this.operateDB = operateDB;
	}

	@Override
	public void addTask(TimerTaskBean bean) throws GmException {
		this.operateDB.OperateTable(TaskSql.ADD_TASK,bean.getTimeName(),bean.getGroupName(),bean.getStartTime(),bean.getEndTime(),bean.getConfig(),bean.getClas(),bean.getStatues(),bean.getDsp());
	}

	@Override
	public void updateTask(TimerTaskBean bean) throws GmException {
	}

	@Override
	public void changeState(String name, int state) throws GmException {
		
	}

	@Override
	public void deleteTask(String name) throws GmException {
		boolean flag=this.operateDB.OperateTable(TaskSql.DEL_TASK,name);
		if(!flag){
			throw new GmException("移除改任务失败~~name:"+name);
		}
	}

	@Override
	public List<TimerTaskBean> queryTaskAll() throws GmException {
		List<TimerTaskBean> beans =new ArrayList<>();
		List<Map<String,Object>> list=this.operateDB.queryResultForLocal(TaskSql.QUERY_TASK);
		for (Map<String, Object> aList : list) {
			beans.add(new TimerTaskBean(aList));
		}
		return beans;
	}

	@Override
	public void saveCache(List<TaskString> beans) throws GmException {
		if(!beans.isEmpty()){
			List<Object[]> parms=new ArrayList<>();
			for (TaskString bean : beans) {
				parms.add(bean.getObjPrams());
			}
			this.operateDB.BatchOperate(TaskObjSql.SAVE_TO,parms);
		}
	}

	@Override
	public List<TaskString> queryCacheObj() throws GmException {
		List<TaskString> beans=new ArrayList<>();
		List<Map<String,Object>> list=this.operateDB.queryResultForLocal(TaskObjSql.QUERY_TO);
		for (Map<String, Object> aList : list) {
			beans.add(new TaskString(aList));
		}
		return beans;
	}

	@Override
	public void deleteCacheObj() throws GmException {
		this.operateDB.OperateTable(TaskObjSql.DEL_TO);
	}

	@Override
	public void backTask(Set<TimerTaskBean> beans) throws GmException {
		long now=System.currentTimeMillis();
		List<Object[]> pram_cag=new ArrayList<>();
		List<Object[]> pram_del=new ArrayList<>();
		for(TimerTaskBean bean:beans){
			if(bean.getEndTime()!=-1&&bean.getEndTime()<=now){
				pram_del.add(new Object[]{bean.getTimeName()});
				CacheTimer.removeTimer(bean.getTimeName());
			}else if(bean.getStartTime()!=-1&&bean.getStartTime()<=now){
				pram_cag.add(new Object[]{-1,bean.getEndTime(),bean.getConfig(),bean.getTimeName()});
			}
		}
		if (!pram_del.isEmpty()){
			this.operateDB.BatchOperate(TaskSql.DEL_TASK,pram_del);
		}
		if (!pram_cag.isEmpty()){
			this.operateDB.BatchOperate(TaskSql.UPDATE_TASK,pram_cag);
		}
	}

}
