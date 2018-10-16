package com.gmtime.cache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.dao.bean.DataBaseConfig;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.commons.dbutils.DbUtils;

public class GmDataConnectionCache {
	private static final int MAX_LINK_EVERYONE=8;
	private static final int MIN_LINK_EVERYONE=4;
	private static final Map<String,BoneCP> connections=new HashMap<>();
	private static void convertUrl(DataBaseConfig config, StringBuilder url){
		url.append("jdbc:mysql://").append(config.getIp()).append(":").append(config.getPort())
		.append("?autoReconnect=true&characterEncoding=utf-8");
	}

	public static void initConnection(List<DataBaseConfig> config) throws SQLException{
		String jdbcDriver = "com.mysql.jdbc.Driver";
		DbUtils.loadDriver(jdbcDriver);
		DriverManager.setLoginTimeout(5000);
		StringBuilder url=new StringBuilder();
		for (DataBaseConfig m : config) {
			BoneCPConfig cpConfig = new BoneCPConfig();
			convertUrl(m, url);
			cpConfig.setJdbcUrl(url.toString());
			cpConfig.setUsername(m.getAccount());
			cpConfig.setPassword(m.getPassword());
			cpConfig.setMaxConnectionsPerPartition(MAX_LINK_EVERYONE);
			cpConfig.setMinConnectionsPerPartition(MIN_LINK_EVERYONE);
			cpConfig.setPartitionCount(1);
			url.delete(0, url.length());
			connections.put(m.getId(), new BoneCP(cpConfig));
		}
	}	
	public static void closeConnection(Connection... conn) throws GmException {
		List<Throwable> manyThrow=new ArrayList<>();
		for (Connection aConn : conn) {
			if (aConn != null) {
				try {
					aConn.close();
				} catch (SQLException e) {
					manyThrow.add(e);
				}
			}
		}
		if (!manyThrow.isEmpty()){
			throw new GmException(manyThrow);
		}
	}
	public static void closeConnectionPool(){
		for(String k :connections.keySet()){
			connections.get(k).shutdown();
		}
	}

	public static Connection getConnection(String dbId){
		Connection conn=null;
		try {
			conn=connections.get(dbId).getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
