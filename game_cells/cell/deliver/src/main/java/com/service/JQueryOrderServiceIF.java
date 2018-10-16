package com.service;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
public interface JQueryOrderServiceIF {
    List<Map<String,Object>> queryAllSetMeal();

    List<Map<String,Object>> queryOrder(String msg);
}
