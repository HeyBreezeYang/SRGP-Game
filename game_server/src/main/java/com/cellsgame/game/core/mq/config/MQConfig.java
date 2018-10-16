package com.cellsgame.game.core.mq.config;

import com.cellsgame.common.util.SpringBeanFactory;

import java.util.Map;

/**
 * Created by yfzhang on 2017/8/22.
 */
public class MQConfig {

    private static MQConfig config;

    public static MQConfig getConfig(){
        if(config == null)
            config = SpringBeanFactory.getBean(MQConfig.class);
        return config;
    }


    private Map<String,String> brokers;

    private String serverId;

    private String groupId;

    private String fightServerId;

    private String fightGroupId;

    public Map<String, String> getBrokers() {
        return brokers;
    }

    public void setBrokers(Map<String, String> brokers) {
        this.brokers = brokers;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFightServerId() {
        return fightServerId;
    }

    public void setFightServerId(String fightServerId) {
        this.fightServerId = fightServerId;
    }

    public String getFightGroupId() {
        return fightGroupId;
    }

    public void setFightGroupId(String fightGroupId) {
        this.fightGroupId = fightGroupId;
    }
}
