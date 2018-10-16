package com.cellsgame.game.context;

import com.cellsgame.common.util.DateUtil;

import java.util.Date;

/**
 * Created by yfzhang on 2017/8/10.
 */
public class BootstrapConfig {

    private String gameHost;
    private int gamePort;
    private String httpHost;
    private int httpPort;
    private int gameLogicID;
    private String payHost;
    private int payPort;
    private Date serverOpenTime;

    public int getGamePort() {
        return gamePort;
    }

    public void setGamePort(int gamePort) {
        this.gamePort = gamePort;
    }

    public String getHttpHost() {
        return httpHost;
    }

    public void setHttpHost(String httpHost) {
        this.httpHost = httpHost;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getGameLogicID() {
        return gameLogicID;
    }

    public void setGameLogicID(int gameLogicID) {
        this.gameLogicID = gameLogicID;
    }

    public String getPayHost() {
        return payHost;
    }

    public void setPayHost(String payHost) {
        this.payHost = payHost;
    }

    public int getPayPort() {
        return payPort;
    }

    public void setPayPort(int payPort) {
        this.payPort = payPort;
    }

    public Date getServerOpenTime() {
        return serverOpenTime;
    }

    public String getGameHost() {
        return gameHost;
    }

    public void setGameHost(String gameHost) {
        this.gameHost = gameHost;
    }

    public void setServerOpenTime(String serverOpenTime) {
        this.serverOpenTime = DateUtil.stringToDate(serverOpenTime);
    }
}
