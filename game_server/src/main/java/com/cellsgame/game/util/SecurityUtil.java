package com.cellsgame.game.util;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.http.HttpUtil;

import java.util.Map;

/**
 * Created by yfzhang on 2017/7/20.
 */
public class SecurityUtil {

        public static Response doPost(String postUrl, Object[] objs) throws Exception {
            Map params = GameUtil.createSimpleMap();
            params.put("action", objs[0]);
            params.put("accountId", objs[1]);
            params.put("params", objs[2]);
            String result = HttpUtil.doPost(postUrl, JSONUtils.toJSONString(params));
            return JSONUtils.fromJson(result, Response.class);
        }

        public class Response {
            private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
