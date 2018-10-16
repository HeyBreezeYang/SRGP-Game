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
@Table("analysis_log.cyd_disport")
public class Disport {
    @Id
    private int id;
    @Column("logTime")
    private String time;
    @Column("sid")
    private String server;
    @Column("channel")
    private String channel;
    @Column("type")
    private int type;
    @Column("recType")
    private int recType;
    @Column("recNum")
    private int recNum;
    @Column("actor")
    private int actor;
    @Column("maxLevel")
    private int maxLevel;
}
