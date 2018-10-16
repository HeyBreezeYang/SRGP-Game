package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2018/2/22.
 *
 * @ClassName gm
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.base_data")
public class BaseData {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("platform")
    private String platform;
    @Column("logTime")
    private String logTime;
    @Column("createNum")
    private int num;
}
