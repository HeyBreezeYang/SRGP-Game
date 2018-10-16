package com.manager.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DJL on 2017/6/21.
 *
 * @ClassName ContextCache
 * @Description 全局缓存数据
 */
public class ContextCache {
    private ContextCache(){}
    public static final Map<String,Boolean> LOGIN_MARK=new HashMap<>();

}
