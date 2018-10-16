package com.manager.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * Created by DJL on 2017/6/23.
 *
 * @ClassName manage
 * @Description
 */
public class PlatFormSqlCallback implements SqlCallback{
    private List<String> key;

    public PlatFormSqlCallback(List<String> column){
        this.key=column;
    }
    @Override
    public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
        List<PlatFormHashMap> list =new ArrayList<>();
        while (rs.next()){
            PlatFormHashMap map =new PlatFormHashMap();
            for(String k:this.key){
                map.put(k,rs.getObject(k));
            }
            list.add(map);
        }
        return list;
    }
}
