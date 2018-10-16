package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/12/14.
 *
 * @ClassName SnapshotBean
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.data_snapshot")
public class SnapshotBean {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("createNum")
    private int create;
    @Column("loginCount")
    private int login;
    @Column("money")
    private int pay;
    @Column("payNum")
    private int payNum;
    @Column("olNum")
    private int online;
    @Column("logTime")
    private String time;
}
