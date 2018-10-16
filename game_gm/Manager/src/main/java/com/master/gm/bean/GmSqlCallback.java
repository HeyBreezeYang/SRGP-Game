package com.master.gm.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * Created by DJL on 2017/7/24.
 *
 * @ClassName GM
 * @Description
 */
public class GmSqlCallback implements SqlCallback {
    private List<String> key;

    public GmSqlCallback(List<String> column){
        this.key=column;
    }
    @Override
    public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
        List<GmHashMap> list =new ArrayList<>();
        while (rs.next()){
            GmHashMap map =new GmHashMap();
            for(String k:this.key){
                map.put(k,rs.getObject(k));
            }
            list.add(map);
        }
        return list;
    }

}
