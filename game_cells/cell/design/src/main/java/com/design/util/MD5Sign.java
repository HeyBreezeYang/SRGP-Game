package com.design.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * Created by HP on 2018/6/7.
 */
public class MD5Sign {
    public static String getSign(Map<String, Object> map,String AppSecret) {

        String result = "";
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {

                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    Object val = item.getValue();
                    sb.append(key + "=" + val);
                    /*if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val);
                    }else {
                        sb.append(key + "=");
                    }*/
                }

            }
//          sb.append(PropertyManager.getProperty("SIGNKEY"));
            result = sb.toString().concat(AppSecret);
            System.out.println("加密前的原串:"+result);

            //进行MD5加密
            result = DigestUtils.md5Hex(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
