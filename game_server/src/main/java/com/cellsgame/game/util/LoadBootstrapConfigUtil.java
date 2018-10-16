package com.cellsgame.game.util;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.common.util.http.HttpUtil;
import com.google.common.reflect.TypeToken;


public class LoadBootstrapConfigUtil {

    public static LoadConfigResponse doPost(String postUrl, String serverId) throws Exception {
        String postParams = "serverId=" + serverId;
        String result = HttpUtil.doPost(postUrl, postParams);
        if(StringUtil.isEmpty(result))
            return null;
        System.out.println("LoadConfigResponse : " + result);
        return JSONUtils.fromJson(result, new TypeToken<LoadConfigResponse>(){}.getType());
    }



    public class LoadConfigResponse {
        private int httpPort;
        private String serverIP;
        private int serverPort;
        private int logicID;
        private int state;
        private String extranetIP;
        private int deliverPort;
        private String openTime;

        public int getHttpPort() {
            return httpPort;
        }

        public void setHttpPort(int httpPort) {
            this.httpPort = httpPort;
        }

        public String getServerIP() {
            return serverIP;
        }

        public void setServerIP(String serverIP) {
            this.serverIP = serverIP;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        public int getLogicID() {
            return logicID;
        }

        public void setLogicID(int logicID) {
            this.logicID = logicID;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getExtranetIP() {
            return extranetIP;
        }

        public void setExtranetIP(String extranetIP) {
            this.extranetIP = extranetIP;
        }

        public int getDeliverPort() {
            return deliverPort;
        }

        public void setDeliverPort(int deliverPort) {
            this.deliverPort = deliverPort;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }
    }


}


