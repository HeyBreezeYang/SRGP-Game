package com.master.gm.service.currency;

import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2017/7/5.
 *
 * @ClassName CurrencyQueryServiceIF
 * @Description 通用查询
 */
public interface CurrencyQueryServiceIF {
    List<GmHashMap> queryMassage(String start, String end, String server, Object[] other)throws GmException;

}
