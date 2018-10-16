package com.cellsgame.game.context;

import com.cellsgame.common.util.SpringBeanFactory;

public class GameConfig {
    private static final int logicIDBit = 10;

    private static GameConfig config;
    private String appId;
    private String appCharacter;
    private String signVerifyUrl;
    private String securityUrl;
    private String bootstrapConfigUrl;
    private String payServerUrl;
    private String payPrivateKey;
    private int gameServerId;
    //    与玩家操作日志头信息  每个游戏版本一个
    private String logTitle;
    private boolean newLogin;
    private boolean release;
    private int logicID;
    private String allowLoginIp;

    public static GameConfig getConfig() {
        if (config == null)
            config = SpringBeanFactory.getBean(GameConfig.class);
        return config;
    }

    public String getSecurityUrl() {
        return securityUrl;
    }

    public void setSecurityUrl(String securityUrl) {
        this.securityUrl = securityUrl;
    }

    public String getSignVerifyUrl() {
        return signVerifyUrl;
    }

    public void setSignVerifyUrl(String signVerifyUrl) {
        this.signVerifyUrl = signVerifyUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppCharacter() {
        return appCharacter;
    }

    public void setAppCharacter(String appCharacter) {
        this.appCharacter = appCharacter;
    }

    public int getGameServerId() {
        return gameServerId;
    }

    public void setGameServerId(int gameServerId) {
        this.gameServerId = gameServerId;
    }

    public String getLogTitle() {
        return logTitle;
    }

    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    public void setLogicID(int gameLogicID) {
        int max = gameLogicID << logicIDBit - 1;
        if (gameLogicID < 0 || gameLogicID > max) {
            throw new RuntimeException("GAME LOGIC ID ERROR RANGE IS [0," + max + "]");
        }
        this.logicID = gameLogicID;
    }

    public int getLogicID(){
        return this.logicID;
    }

    public boolean isNewLogin() {
        return newLogin;
    }

    public void setNewLogin(boolean newLogin) {
        this.newLogin = newLogin;
    }

    public boolean isRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public String getBootstrapConfigUrl() {
        return bootstrapConfigUrl;
    }

    public void setBootstrapConfigUrl(String bootstrapConfigUrl) {
        this.bootstrapConfigUrl = bootstrapConfigUrl;
    }

    public String getPayServerUrl() {
        return payServerUrl;
    }

    public void setPayServerUrl(String payServerUrl) {
        this.payServerUrl = payServerUrl;
    }

    public String getPayPrivateKey() {
        return payPrivateKey;
    }

    public void setPayPrivateKey(String payPrivateKey) {
        this.payPrivateKey = payPrivateKey;
    }

    public String getAllowLoginIp() {
        return allowLoginIp;
    }

    public void setAllowLoginIp(String allowLoginIp) {
        this.allowLoginIp = allowLoginIp;
    }
}
