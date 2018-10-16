package com.bean;

import org.springframework.data.annotation.Id;

/**
 * Created by DJL on 2017/8/18.
 *
 * @ClassName BanBack
 * @Description 封禁记录
 */

public class InsideAccount {
    private String id;
    private String sid;
    private String channel;
    private String rolename;
    private int pid;
    private int paymoney;
    private String username;
    private String userphone;
    private String ascription;
    private String applyuser;
    private String applyreason;
    private int status;
    private String remark;
    private long createtime;
    private String toexamineby;
    private long toexaminetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(int paymoney) {
        this.paymoney = paymoney;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public String getApplyuser() {
        return applyuser;
    }

    public void setApplyuser(String applyuser) {
        this.applyuser = applyuser;
    }

    public String getApplyreason() {
        return applyreason;
    }

    public void setApplyreason(String applyreason) {
        this.applyreason = applyreason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getToexamineby() {
        return toexamineby;
    }

    public void setToexamineby(String toexamineby) {
        this.toexamineby = toexamineby;
    }

    public long getToexaminetime() {
        return toexaminetime;
    }

    public void setToexaminetime(long toexaminetime) {
        this.toexaminetime = toexaminetime;
    }

    public void addTestFunc() { System.out.println("==!!");}
    // ddddd
}
