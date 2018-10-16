package com.gmtime.util;

import java.util.*;

/**
 * Created by DJL on 2016/12/13.
 *
 * @ClassName InitWithUse
 * @Description
 */
public class InitWithUse {

    public static void queryKV(Map<String, Set<String>> map, List<Map<String,Object>> list, String keyK, String valueK){
        for (Map<String, Object> aList : list) {
            Set<String> pids = map.get(aList.get(keyK).toString());
            if (pids == null) {
                pids = new HashSet<>();
            }
            pids.add(aList.get(valueK).toString());
            map.put(aList.get(keyK).toString(), pids);
        }
    }
    public static void queryKV2(Map<String,Map<String,Set<String>>> map, List<Map<String,Object>> list,String keyK1,String keyK2, String valueK){
        for (Map<String, Object> aList : list) {
            Map<String,Set<String>> m1=map.get(aList.get(keyK1).toString());
            if(m1==null){
                m1=new HashMap<>();
            }
            Set<String> pids = m1.get(aList.get(keyK2).toString());
            if(pids==null){
                pids = new HashSet<>();
            }
            pids.add(aList.get(valueK).toString());
            m1.put(aList.get(keyK2).toString(), pids);
            map.put(aList.get(keyK1).toString(),m1);
        }
    }
    public static <T> Set<T>  difference(Collection<T> set,Collection<T> setL){
        Set<T> t =new HashSet<>(set);
        Set<T> y =new HashSet<>(set);
        t.removeAll(setL);
        y.removeAll(t);
        return y;
    }
}
