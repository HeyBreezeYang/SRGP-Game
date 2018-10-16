package com.cellsgame.game.util;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.common.util.http.HttpUtil;
import com.google.common.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class AllowLoginIpUtil {

    public static List<Map> doPost(String postUrl) throws Exception {
        String result = HttpUtil.doPost(postUrl, "");
        if(StringUtil.isEmpty(result))
            return null;
        System.out.println("AllowLoginIpUtil : " + result);
        return JSONUtils.fromJson(result, new TypeToken<List<Map>>(){}.getType());
    }
}
