package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2018/3/28.
 *
 * @ClassName WorldBossNoon
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.boss_noon")
public class WorldBossNoon {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("logTime")
    private String time;
    @Column("nop")
    private String numOf;
    @Column("num")
    private String num;
}
