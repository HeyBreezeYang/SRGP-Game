package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by HP on 2018/7/19.
 */
@ToString
@Getter
@Setter
@Table("analysis_log.vip_rank")
public class VipRank {

    @Id
    private int id;
    @Column("sid")
    private String sid;
    @Column("channel")
    private String channel;
    @Column("pid")
    private String pid;
    @Column("pname")
    private String pname;
    @Column("lv")
    private int lv;
    @Column("exp")
    private long exp;
    @Column("money")
    private int money;
}
