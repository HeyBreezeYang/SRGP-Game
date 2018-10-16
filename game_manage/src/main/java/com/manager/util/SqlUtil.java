package com.manager.util;

import com.manager.bean.PlatFormSqlCallback;
import com.manager.cache.PlatFormSql;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

/**
 * Created by DJL on 2017/6/23.
 *
 * @ClassName SqlUtil
 * @Description 每个SQL
 */
public class SqlUtil {
    public static Sql getSql(PlatFormSql sql,Object... params){
        Sql s = Sqls.create(sql.getSql());
        for (int i=0;i<params.length;i++){
            s.params().set(sql.getParams().get(i),params[i]);
        }
        s.setCallback(new PlatFormSqlCallback(sql.getColumn()));
        return s;
    }
}
