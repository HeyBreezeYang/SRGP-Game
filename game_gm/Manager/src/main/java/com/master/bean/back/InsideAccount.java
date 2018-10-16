package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/8/18.
 *
 * @ClassName BanBack
 * @Description 封禁记录
 */
@ToString
@Getter
@Setter
public class InsideAccount {
    private String id;
    private String sid;
    private String channel;
    private String rolename;
    private String pid;
    private String paymoney;
    private String username;
    private String userphone;
    private String ascription;
    private String applyuser;
    private String applyreason;
    private String status;
    private String remark;
    private long createtime;
    private String toexamineby;
    private long toexaminetime;

}
