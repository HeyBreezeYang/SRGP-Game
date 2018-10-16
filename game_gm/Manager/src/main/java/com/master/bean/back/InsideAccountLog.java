package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by HP on 2018/6/29.
 */
@ToString
@Getter
@Setter
@Table("gm_config.inside_account_log")
public class InsideAccountLog {

    @Id
    private int id;
    @Column("sid")
    private String sid;
    @Column("channel")
    private String channel;
    @Column("rolename")
    private String rolename;
    @Column("money")
    private int money;
    @Column("operatorBy")
    private String operatorBy;
    @Column("logTime")
    private long logTime;

    private String logTimeS;

}
