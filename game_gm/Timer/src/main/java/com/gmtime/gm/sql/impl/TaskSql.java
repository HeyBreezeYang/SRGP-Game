package com.gmtime.gm.sql.impl;

import com.gmtime.gm.sql.SqlIF;

/**
 * 
* @ClassName: TaskSql
* @Description: 任务管理表 t_timer
* @author DJL
* @date 2015-12-23 下午5:16:51
*
 */
public enum TaskSql implements SqlIF{
	DEL_TASK(){
		@Override
		public String querySql() {
			return "delete from gm_dispoly.t_timer where timeName=?;";
		}
	},
	UPDATE_TASK(){
		@Override
		public String querySql() {
			return "update gm_dispoly.t_timer set startTime=?,endTime=?,config=? where timeName=?;";
		}
	},
	QUERY_TASK(){
		@Override
		public String querySql() {
			return "select timeName,groupName,startTime,endTime,config,clazz,statues,dsp from gm_dispoly.t_timer order by id;";
		}
	},
	ADD_TASK(){
		@Override
		public String querySql() {
			return "insert into gm_dispoly.t_timer(id,timeName,groupName,startTime,endTime,config,clazz,statues,dsp) values (null,?,?,?,?,?,?,?,?);";
		}
	}
}
