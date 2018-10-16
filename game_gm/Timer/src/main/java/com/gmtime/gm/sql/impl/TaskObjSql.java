package com.gmtime.gm.sql.impl;

import com.gmtime.gm.sql.SqlIF;

/**
 * 
* @ClassName: TaskObjSql
* @Description: t_taskObj
* @author DJL
* @date 2015-12-24 下午5:34:23
*
 */
public enum TaskObjSql implements SqlIF{
	SAVE_TO(){
		@Override
		public String querySql() {
			return "insert into gm_back.t_taskobj (taskName,obj) values(?,?) ";
		}
	},
	QUERY_TO(){
		@Override
		public String querySql() {
			return "select taskName,obj from gm_back.t_taskobj ";
		}
	},
	DEL_TO(){
		@Override
		public String querySql() {
			return "delete from gm_back.t_taskobj ";
		}
	}
}
