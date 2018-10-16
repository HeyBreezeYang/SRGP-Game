package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/8/17.
 *
 * @ClassName GmLog
 * @Description GM 日志
 */
@Getter
@Setter
@ToString
@Table("gm_config.t_log")
public class GmLog {
    @Id
    private int id;
    @Column("type")
    private String type;
    @Column("userName")
    private String name;
    @Column("context")
    private String context;
    @Column("logTime")
    private Long time;
}
