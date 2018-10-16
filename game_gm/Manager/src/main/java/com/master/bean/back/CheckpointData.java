package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2018/4/16.
 *
 * @ClassName gm
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.checkpoint")
public class CheckpointData {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("logTime")
    private String logTime;
    @Column("channel")
    private String channel;
    @Column("task")
    private String task;
    @Column("passNum")
    private String passNum;
    @Column("passFre")
    private String passFre;
}
