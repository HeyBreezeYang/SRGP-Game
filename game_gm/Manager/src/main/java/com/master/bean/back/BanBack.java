package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/8/18.
 *
 * @ClassName BanBack
 * @Description 封禁记录
 */
@ToString
@Getter
@Setter
@Table("analysis_log.ban_back")
public class BanBack {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("playerName")
    private String name;
    @Column("type")
    private int type;
    @Column("logTime")
    private long logTime;
}
