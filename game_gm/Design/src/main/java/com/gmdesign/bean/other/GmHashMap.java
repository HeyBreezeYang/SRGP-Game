package com.gmdesign.bean.other;

import java.util.HashMap;

/**
 * Created by DJL on 2017/6/10.
 *
 * @ClassName GmHashMap
 * @Description 公共map结构
 */
public class GmHashMap extends HashMap<String,Object>{
    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }
}
