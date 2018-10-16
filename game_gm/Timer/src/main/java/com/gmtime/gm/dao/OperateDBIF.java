package com.gmtime.gm.dao;


import java.sql.Connection;
import java.util.List;
import java.util.Map;
import com.gmdesign.exception.GmException;
import com.gmtime.gm.sql.SqlIF;

public interface OperateDBIF {
	List<Map<String, Object>> queryResultForLocal(SqlIF sql,Object... params) throws GmException;
	boolean OperateTable(SqlIF sql, Object... params) throws GmException;
	boolean BatchOperate(SqlIF sql, List<Object[]> parm)throws GmException;
	List<Map<String, Object>> findOtherDB(Connection connection, SqlIF sql, Object... data)throws GmException;
	List<Map<String, Object>> callOtherDB(Connection connection, SqlIF sql, Object... data)throws GmException;
	List<Object> callResult(Connection connection, String dbName, SqlIF sql, Object[] in, int[] out)throws GmException;
}
