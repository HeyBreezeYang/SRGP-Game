package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/3/2.
 *
 * @ClassName GmTimer
 * @Description 定时管理任务表
 */
@Getter
@Setter
@ToString
@Table("gm_config.t_timer")
public class GmTimer {
    @Id
    private int id;
    @Column(value = "timeName")
    private String timeName;
    @Column(value = "groupName")
    private String groupName;
    @Column(value = "startTime")
    private Long startTime;
    @Column(value = "endTime")
    private Long endTime;
    @Column(value = "config")
    private String config;
    @Column(value = "clazz")
    private String clazz;
    @Column(value = "statues")
    private Short statues;
    @Column(value = "dsp")
    private String dsp;


}
