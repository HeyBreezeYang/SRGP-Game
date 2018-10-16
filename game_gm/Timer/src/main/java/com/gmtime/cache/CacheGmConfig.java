package com.gmtime.cache;

import java.util.*;

/**
 * Created by DJL on 2016/12/7.
 *
 * @ClassName CacheGmConfig
 * @Description
 */
public class CacheGmConfig {
    private static final Set<String> CHANNEL =new HashSet<>();
    private static final Set<String> SERVER=new HashSet<>();

    public static Set<String> getChannel() {
        return CHANNEL;
    }

    public static Set<String> getServer() {
        return SERVER;
    }

    public static void setChannel(List<String> list){
        if(CHANNEL.isEmpty()){
            CHANNEL.addAll(list);
        }
    }
    public static void setServer(List<String>  list){
        if(SERVER.isEmpty()){
            SERVER.addAll(list);
        }
    }
}
