package com.master.util;

import java.util.List;

import com.master.gm.bean.GmSql;
import com.master.gm.bean.GmSqlCallback;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

/**
 * Created by DJL on 2017/6/23.
 *
 * @ClassName SqlUtil
 * @Description 每个SQL
 */
public class SqlUtil {
    public static Sql getSql(String sql,String[] paramsKey,Object[] params,List<String> column){
        Sql s = Sqls.create(sql);
        if(paramsKey!=null&&paramsKey.length>0){
            for (int i=0;i<params.length;i++){
                s.params().set(paramsKey[i],params[i]);
            }
        }
        s.setCallback(new GmSqlCallback(column));
        return s;
    }

    public static Sql getSql(GmSql sql, Object... params){
        Sql s = Sqls.create(sql.getSql());
        for (int i=0;i<params.length;i++){
            System.out.println("=="+params[i]);
            s.params().set(sql.getParams().get(i),params[i]);
        }
        s.setCallback(new GmSqlCallback(sql.getColumn()));
        return s;
    }
}
