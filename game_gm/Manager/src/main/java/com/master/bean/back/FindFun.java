package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2018/4/3.
 *
 * @ClassName gm
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.cyd_zlz")
public class FindFun {
    @Id
    private int id;
    @Column("logTime")
    private String time;
    @Column("sid")
    private String server;
    @Column("channel")
    private String channel;
    @Column("stall")
    private int stall;
    @Column("roomNum")
    private int roomNum;
    @Column("roomPlayer")
    private int roomPlayer;
    @Column("actor")
    private int actor;
    @Column("integral")
    private int integral;
    @Column("refresh")
    private int refresh;
    @Column("treNum")
    private int treNum;
}
