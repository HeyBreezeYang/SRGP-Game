package com.gmtime.gm.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.dao.OperateDBIF;
import com.gmtime.gm.sql.SqlIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("operateDB")
public class OperateDB implements OperateDBIF {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public OperateDB(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean OperateTable(SqlIF sql, Object... params)
			throws GmException {
		if(params!=null&&params.length>0){
			return jdbcTemplate.update(sql.querySql(), params)>=1;
		}else{
			return jdbcTemplate.update(sql.querySql())>=1;
		}
	}
	@Override
	public List<Map<String,Object>> queryResultForLocal(SqlIF sql, Object... params)
			throws GmException {
		if(params!=null&&params.length>0){
			return jdbcTemplate.queryForList(sql.querySql(), params);
		}else{
			return jdbcTemplate.queryForList(sql.querySql());
		}
	}

	
	private void result(List<Map<String, Object>> objList,ResultSet resultSet) throws SQLException{
		ResultSetMetaData res = resultSet.getMetaData();
		// 获得结果集列数
		int columnCount = res.getColumnCount();
		// 将ResultSet的结果保存到List中
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<>();
			for (int i = 1; i <= columnCount; i++) {
				map.put(res.getColumnLabel(i), resultSet.getObject(i));
			}
			objList.add(map);
		}
		
	}
	
	private void getResult(List<Map<String, Object>> objList,PreparedStatement statement,Object[] params) throws GmException, SQLException{
		// 执行SQL获得结果集
		ResultSet resultSet = executeQueryRS(statement, params);
		result(objList, resultSet);
		statement.close();
	}
	@Override
	public List<Map<String, Object>> findOtherDB(Connection connection,
			SqlIF sql, Object... data) throws GmException {
		List<Map<String, Object>> objList = new ArrayList<>();
		try {
			getResult(objList, connection.prepareStatement(sql.querySql()),data);
		} catch (SQLException e) {
			throw new GmException(e.getMessage());
		}
		return objList;
	}
	
	private static ResultSet executeQueryRS(PreparedStatement state, Object[] params) throws GmException {
		ResultSet resultSet;
		try {
			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					state.setObject(i + 1, params[i]);
				}
			}
			// 执行
			resultSet = state.executeQuery();
		} catch (SQLException e) {
			throw new GmException(e.getMessage());
		} 
		return resultSet;
	}
	@Override
	public boolean BatchOperate(SqlIF sql, List<Object[]> parm)
			throws GmException {
		int[]i =jdbcTemplate.batchUpdate(sql.querySql(),parm);
		return i.length>0;
	}

	@Override
	public List<Map<String, Object>> callOtherDB(Connection connection,
			SqlIF sql, Object... data) throws GmException {
		List<Map<String, Object>> objList = new ArrayList<>();
		try {
			getResult(objList, connection.prepareCall(sql.querySql()),data);
		} catch (SQLException e) {
			throw new GmException(e.getMessage());
		}
		return objList;
	}

	@Override
	public List<Object> callResult(Connection connection,String dbName,SqlIF sql,
			Object[] in, int[] out) throws GmException {
		List<Object> res =new ArrayList<>();
		try {
			CallableStatement statement=connection.prepareCall(sql.querySql());
			for(int i=1;i<=in.length;i++){
				statement.setObject(i, in[i-1]);
			}
			for(int i=in.length+1,j=0;i<=in.length+out.length;i++,j++){
				statement.registerOutParameter(i,out[j]);
			}
			statement.execute();
			for(int i=in.length+1;i<=in.length+out.length;i++){
				res.add(statement.getObject(i));
			}
			statement.close();
		} catch (SQLException e) {
			throw new GmException(e.getMessage());
		}
		return res;
	}
}
