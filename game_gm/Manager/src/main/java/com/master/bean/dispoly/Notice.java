package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by HP on 2018/5/26.
 */
@Getter
@Setter
@ToString
@Table("gm_config.notice")
public class Notice {

    @Id
    private int id;
    @Column("channel")
    private String channel;
    @Column("type")
    private String type;
    @Column("msg")
    private String msg;
//    @Column("startTime")
    private long startTime;
//    @Column("endTime")
    private  long endTime;
//    @Column("intervalTime")
    private long intervalTime;
}
