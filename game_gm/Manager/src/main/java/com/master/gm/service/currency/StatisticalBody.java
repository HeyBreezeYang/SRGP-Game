package com.master.gm.service.currency;

import java.util.Collection;
import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import org.nutz.dao.Dao;

/**
 * Created by DJL on 2018/3/30.
 *
 * @ClassName gm
 * @Description
 */
public abstract class StatisticalBody {
    public abstract Collection<GmHashMap> countToday(List<GmHashMap> res,String todayString);
    public abstract void countDbOne(List<GmHashMap> count,Dao dao,GmHashMap cnd,String...param);
    public abstract void countDbTwo(List<GmHashMap> count,Dao dao,GmHashMap cnd,String...param);
}
