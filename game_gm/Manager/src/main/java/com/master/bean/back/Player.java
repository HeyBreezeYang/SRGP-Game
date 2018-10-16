package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/7/5.
 *
 * @ClassName Player
 * @Description
 */
@Getter
@Setter
@ToString
@Table("analysis_log.player")
public class  Player {
    @Name
    private String pid;
    @Column("name")
    private String name;
    @Column("uid")
    private String uid;
    @Column("createTime")
    private String createTime;
    @Column("platform")
    private String platform;
    @Column("sid")
    private String sid;
    @Column("payment")
    private Integer payment;
    @Column("treasure")
    private Integer treasure;
    @Column("lv")
    private Integer lv;
    @Column("vip")
    private Integer vip;
    @Column("money")
    private Integer money;
    @Column("fight")
    private Integer fight;
    @Column("lastPayTime")
    private String lastPayTime;
    @Column("lastLoginTime")
    private String lastLoginTime;
}
