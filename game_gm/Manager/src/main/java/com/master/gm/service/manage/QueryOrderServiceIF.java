package com.master.gm.service.manage;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
public interface QueryOrderServiceIF {
    Map queryQuerySelect();

    List<Map> queryOrder(String sid, String channel, String paySetMeal, String status, String startDate, String endDate, String pid, String orderNum);
}
