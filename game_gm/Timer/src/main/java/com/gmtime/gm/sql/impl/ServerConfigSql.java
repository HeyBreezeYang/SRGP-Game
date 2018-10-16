package com.gmtime.gm.sql.impl;

import com.gmtime.gm.sql.SqlIF;

public enum ServerConfigSql implements SqlIF{
	QUERY_SERVER(){
		@Override
		public String querySql() {
			return "select * from gm_dispoly.game_server ";
		}
	},
	QUERY_DB(){
		@Override
		public String querySql() {
			return "select * from gm_dispoly.db_config ";
		}
	},
	QUERY_CHANNEL(){
		@Override
		public String querySql() {
			return "select * from gm_dispoly.channl ";
		}
	},
	QUERY_PARAMETER_TWO(){
		@Override
		public String querySql() {
			return "select prams from gm_dispoly.individual_parameter where id =? ;";
		}
	}
}
