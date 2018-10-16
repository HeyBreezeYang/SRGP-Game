package com.cellsgame.game.util;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.http.HttpUtil;
import com.google.common.reflect.TypeToken;


public class LoginVerifyUtil {

    public static VerifyResponse doPost(String postUrl, String appId, String appCharacter, String sign) throws Exception {
        Map params = GameUtil.createSimpleMap();
        params.put("appId", appId);
        params.put("appCharacter", appCharacter);
        params.put("sign", sign);
        String postParams = "verifyMsg=" + JSONUtils.toJSONString(params);
        String result = HttpUtil.doPost(postUrl, postParams);
        return JSONUtils.fromJson(result, new TypeToken<VerifyResponse>(){}.getType());
    }



    public class VerifyResponse {
        private int co;
        private Attach attach;

        public int getCo() {
            return co;
        }

        public void setCo(int co) {
            this.co = co;
        }

        public Attach getAttach() {
            return attach;
        }

        public void setAttach(Attach attach) {
            this.attach = attach;
        }
    }

    public class Attach {
        private String accountId;
        private String token;
        private int admin;
        private Security security;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Security getSecurity() {
            return security;
        }

        public void setSecurity(Security security) {
            this.security = security;
        }

        public int getAdmin() {
            return admin;
        }

        public void setAdmin(int admin) {
            this.admin = admin;
        }
    }

    public class Security {
        private int status;
        private String psd;
        private long statusUpdateTime;
        private int errorNum;
        private long lastErrorTime;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStatusUpdateTime() {
            return statusUpdateTime;
        }

        public void setStatusUpdateTime(long statusUpdateTime) {
            this.statusUpdateTime = statusUpdateTime;
        }

        public String getPsd() {
            return psd;
        }

        public void setPsd(String psd) {
            this.psd = psd;
        }

        public int getErrorNum() {
            return errorNum;
        }

        public void setErrorNum(int errorNum) {
            this.errorNum = errorNum;
        }

        public long getLastErrorTime() {
            return lastErrorTime;
        }

        public void setLastErrorTime(long lastErrorTime) {
            this.lastErrorTime = lastErrorTime;
        }
    }

}


