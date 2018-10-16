package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/9/30.
 *
 * @ClassName PayBean
 * @Description
 */
@Getter
@Setter
@ToString
@Table("analysis_log.pay")
public class PayBean {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("channel")
    private String platform;
    @Column("logTime")
    private String time;
    @Column("pid")
    private String pid;
    @Column("price")
    private int price;
    @Column("gid")
    private String goodsId;
    @Column("lv")
    private int lv;

}
