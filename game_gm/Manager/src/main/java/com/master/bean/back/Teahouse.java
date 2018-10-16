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
 * @ClassName Teahouse
 * @Description 茶楼
 */
@ToString
@Getter
@Setter
@Table("analysis_log.cyd_cl")
public class Teahouse {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("logTime")
    private String time;
    @Column("channel")
    private String channel;
    @Column("challenge")
    private int challenge;
    @Column("challengeNum")
    private int challengeNum;
    @Column("vengeance")
    private int vengeance;
    @Column("negotiate")
    private int negotiate;
    @Column("fight")
    private int fight;
    @Column("integral")
    private int integral;
}
